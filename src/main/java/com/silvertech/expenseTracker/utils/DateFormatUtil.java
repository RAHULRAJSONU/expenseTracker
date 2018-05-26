package com.silvertech.expenseTracker.utils;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class DateFormatUtil {

    public Date getCurrentPstDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        Date date = null;
        try {
            date = formatter.parse(new Date().toString());
        } catch (ParseException e) {
            log.error("Error while parsing Date" + e);
        }
        SimpleDateFormat sdfPst = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        DateTime dt = new DateTime(date);
        DateTimeZone dtZone = DateTimeZone.forID("America/Los_Angeles");
        DateTime dtus = dt.withZone(dtZone);
        Date dateInPst = dtus.toLocalDateTime().toDate();

        return dateInPst;
    }

    public Date getCurrentPstDate() {
        Calendar c = Calendar.getInstance();

        TimeZone z = c.getTimeZone();
        int offset = z.getRawOffset();
        if (z.inDaylightTime(new Date())) {
            offset = offset + z.getDSTSavings();
        }
        int offsetHrs = offset / 1000 / 60 / 60;
        int offsetMins = offset / 1000 / 60 % 60;

        c.add(Calendar.HOUR_OF_DAY, (-offsetHrs));
        c.add(Calendar.MINUTE, (-offsetMins));

        return c.getTime();
    }

    public Date parseDate(String dateString) {
        Date parsed;
        try {
            SimpleDateFormat format =
                    new SimpleDateFormat("dd/MM/yyyy");
            parsed = format.parse(dateString);
        } catch (ParseException pe) {
            throw new IllegalArgumentException(pe);
        }
        return parsed;
    }


}
