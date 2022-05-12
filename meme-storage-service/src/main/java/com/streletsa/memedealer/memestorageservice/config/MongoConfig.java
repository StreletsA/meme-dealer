package com.streletsa.memedealer.memestorageservice.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.streletsa.memedealer.memestorageservice")
public class MongoConfig extends AbstractMongoClientConfiguration {

    private static final String MONGODB_DATABASE_NAME = ConstantsConfig.MONGODB_DATABASE;
    private static final String MONGODB_HOST = ConstantsConfig.MONGODB_HOST;
    private static final String MONGODB_PORT = ConstantsConfig.MONGODB_PORT;

    @Override
    protected String getDatabaseName() {
        return MONGODB_DATABASE_NAME;
    }

    @Override
    public MongoClient mongoClient() {

        ConnectionString connectionString = createMongodbConnectionString();
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

    private ConnectionString createMongodbConnectionString(){
        String mongodbConnectionString = "mongodb://" +
                MONGODB_HOST + ':' +
                MONGODB_PORT + '/' +
                MONGODB_DATABASE_NAME;

        return new ConnectionString(mongodbConnectionString);
    }

}
