package com.silvertech.expenseTracker.annotation;

import com.silvertech.expenseTracker.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StatelessSession;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SequenceListener implements PreInsertEventListener {
    private static final long serialVersionUID = 7946581162328559098L;
    private final Map<String, CacheEntry> cache = new HashMap<>();
    private SessionFactoryImpl sessionFactory;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @PostConstruct
    public void selfRegister() {
        // As you might expect, an EventListenerRegistry is the place with which event listeners are registered
        // It is a service so we look it up using the service registry
        sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        final EventListenerRegistry eventListenerRegistry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        // add the listener to the end of the listener chain
        eventListenerRegistry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(this);

    }

    @Override
    public boolean onPreInsert(PreInsertEvent p_event) {
        updateSequenceValue(p_event.getEntity(), p_event.getState(), p_event.getPersister().getPropertyNames());

        return false;
    }

    private void updateSequenceValue(Object p_entity, Object[] p_state, String[] p_propertyNames) {
        try {
            List<Field> fields = ReflectionUtils.getFields(p_entity.getClass(), null, Sequence.class);

            if (!fields.isEmpty()) {
                if (log.isDebugEnabled()) {
                    log.debug("Intercepted custom sequence entity.");
                }

                for (Field field : fields) {
                    log.info("Intercepted custom sequence entity for : " + p_entity.getClass().getName());
                    String value = generateAccountCode(getSequenceNumber(p_entity.getClass().getName())).toString();
                    field.setAccessible(true);
                    field.set(p_entity, value);
                    setPropertyState(p_state, p_propertyNames, field.getName(), value);

                    if (log.isDebugEnabled()) {
                        //Log.debug(log, "Set {0} property to {1}.", new Object[] { field, value });
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to set sequence property.", e);
        }
    }

    private Integer generateAccountCode(Integer sequenceNumber) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        Integer yearInt = calendar.get(Calendar.YEAR);
        yearInt *= 1000;
        return yearInt += sequenceNumber;
    }

    private Integer getSequenceNumber(String p_className) {
        synchronized (cache) {
            log.info("checking Cache for sequence number for : " + p_className);
            CacheEntry current = cache.get(p_className);

            // not in cache yet => load from database
            if ((current == null) || current.isEmpty()) {
                boolean insert = false;
                StatelessSession session = sessionFactory.openStatelessSession();
                session.beginTransaction();
                SequenceNumber sequenceNumber = (SequenceNumber) session.get(SequenceNumber.class, p_className);
                /*Optional<SequenceNumber> sequenceNumberOptional = sequenceNumberRepository.findById(p_className);
                if(sequenceNumberOptional.isPresent()){
                    sequenceNumber = sequenceNumberOptional.get();
                }*/
                log.info("sequenceNumber found for : " + p_className + " : " + sequenceNumber);
                // not in database yet => create new sequence
                if (sequenceNumber == null) {
                    log.info("sequence number not found in cache for : " + p_className);
                    sequenceNumber = new SequenceNumber();
                    sequenceNumber.setClassName(p_className);
                    insert = true;
                }

                current = new CacheEntry(sequenceNumber.getNextValue() + sequenceNumber.getIncrementValue(), sequenceNumber.getNextValue());
                cache.put(p_className, current);
                log.info("new Cache entry for : " + p_className + " : " + current);
                sequenceNumber.setNextValue(sequenceNumber.getNextValue() + sequenceNumber.getIncrementValue());

                if (insert) {
                    log.info("persisting new sequenceNumber");
                    session.insert(sequenceNumber);
                    //sequenceNumberRepository.save(sequenceNumber);
                } else {
                    log.info("updating sequenceNumber");
                    session.update(sequenceNumber);
                    //sequenceNumberRepository.save(sequenceNumber);
                }
                session.getTransaction().commit();
                session.close();
            }

            return current.next();
        }
    }

    private void setPropertyState(Object[] propertyStates, String[] propertyNames, String propertyName, Object propertyState) {
        for (int i = 0; i < propertyNames.length; i++) {
            if (propertyName.equals(propertyNames[i])) {
                propertyStates[i] = propertyState;
                return;
            }
        }
    }

    private static class CacheEntry {
        private final int limit;
        private int current;

        public CacheEntry(final int p_limit, final int p_current) {
            current = p_current;
            limit = p_limit;
        }

        public Integer next() {
            return current++;
        }

        public boolean isEmpty() {
            return current >= limit;
        }

        @Override
        public String toString() {
            return "CacheEntry{" +
                    "limit=" + limit +
                    ", current=" + current +
                    '}';
        }
    }
}