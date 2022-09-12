package com.intexsoft.analytics.configuration;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
@EnableR2dbcAuditing
public class PostgresConfiguration {

    private static final String SCHEMA_PATH = "schema.sql";

    private static final String DATA_PATH = "data.sql";

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        ResourceDatabasePopulator resource = new ResourceDatabasePopulator(
                new ClassPathResource(SCHEMA_PATH),
                new ClassPathResource(DATA_PATH));
        initializer.setDatabasePopulator(resource);
        return initializer;
    }
}
