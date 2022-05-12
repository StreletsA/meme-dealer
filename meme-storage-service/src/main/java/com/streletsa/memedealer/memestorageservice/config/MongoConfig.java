package com.streletsa.memedealer.memestorageservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    private static final String MONGODB_DATABASE_NAME = ConstantsConfig.MONGODB_DATABASE_NAME;

    @Override
    protected String getDatabaseName() {
        return MONGODB_DATABASE_NAME;
    }
}
