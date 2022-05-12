package com.streletsa.memedealer.memestorageservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Configuration
public class ConstantsConfig {

    private static final String PROPERTIES_FILE_NAME = "application.properties";

    private static Properties systemProps;
    private static Properties resourceProps;

    public static final String PUBLISH_WITHOUT_APPROVING;

    public static final String AUTO_SAVE_IMAGES;
    public static final String SAVING_IMAGES_PATH;

    public static final String RABBITMQ_HOST;
    public static final String RABBITMQ_QUEUE_NAME;

    public static final String MONGODB_HOST;
    public static final String MONGODB_PORT;
    public static final String MONGODB_DATABASE;

    static {

        setProperties();

        RABBITMQ_HOST = findConstantByPropertyName("rabbitmq.host");
        RABBITMQ_QUEUE_NAME = findConstantByPropertyName("rabbitmq.queue.name");

        PUBLISH_WITHOUT_APPROVING = findConstantByPropertyName("publish.without.approving");
        AUTO_SAVE_IMAGES = findConstantByPropertyName("auto.save.images");
        SAVING_IMAGES_PATH = findConstantByPropertyName("saving.images.path");

        MONGODB_HOST = findConstantByPropertyName("mongodb.host");
        MONGODB_PORT = findConstantByPropertyName("mongodb.port");
        MONGODB_DATABASE = findConstantByPropertyName("mongodb.database");

    }

    private static void setProperties(){

        setSystemProps();
        setResourceProps();

    }

    private static void setSystemProps(){
        systemProps = new Properties(System.getProperties());
    }

    private static void setResourceProps(){
        try {
            InputStream is = ConstantsConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
            resourceProps = new Properties();
            resourceProps.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String findConstantByPropertyName(String propertyName){

        String constant = null;
        String environmentVariableName = propertyNameToEnvironmentName(propertyName);

        Optional<String> constantOptional = findEnvironmentVariable(environmentVariableName);

//        if (constantOptional.isEmpty()){
//            constantOptional = findSystemProperty(propertyName);
//        }
//
//        if (constantOptional.isEmpty()){
//            constantOptional = findPropertyFromResourceFile(propertyName);
//        }


        if (constantOptional.isPresent()){
            constant = constantOptional.get();
        }
        else{
            log.error("propertyName '{}' not found", propertyName);
        }

        return constant;

    }

    private static String propertyNameToEnvironmentName(String propertyName){

        return propertyName.toUpperCase().replace('.', '_');

    }

    private static Optional<String> findEnvironmentVariable(String environmentVariableName){

        Optional<String> environmentVariableOptional = Optional.empty();
        String environmentVariable = null;

        try {
            environmentVariable = System.getenv(environmentVariableName);
        } catch (Exception e){
            e.printStackTrace();
        }

        if (environmentVariable == null){
            log.info("Environment variable with name " + environmentVariableName + " not found");
        }
        else{
            environmentVariableOptional = Optional.of(environmentVariable);
        }

        return environmentVariableOptional;

    }

    private static Optional<String> findSystemProperty(String propertyName){

        Optional<String> propertyOptional = findProperty(systemProps, propertyName);

        if (propertyOptional.isEmpty()){
            log.info("System property with name " + propertyName + " not found");
        }

        return propertyOptional;

    }

    private static Optional<String> findPropertyFromResourceFile(String propertyName){

        Optional<String> propertyOptional = findProperty(resourceProps, propertyName);

        if (propertyOptional.isEmpty()){
            log.info("Property with name " + propertyName + " not found in resource file");
        }

        return propertyOptional;

    }

    private static Optional<String> findProperty(Properties properties, String propertyName){

        Optional<String> propertyOptional = Optional.empty();
        String property = null;

        if (properties != null){
            property = properties.getProperty(propertyName);
        }

        if (property != null){
            propertyOptional = Optional.of(property);
        }

        return propertyOptional;

    }

}