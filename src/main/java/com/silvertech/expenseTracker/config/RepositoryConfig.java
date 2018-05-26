package com.silvertech.expenseTracker.config;

import com.silvertech.expenseTracker.domain.entity.CategoryProjection;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.getProjectionConfiguration().addProjection(CategoryProjection.class);
    }
}