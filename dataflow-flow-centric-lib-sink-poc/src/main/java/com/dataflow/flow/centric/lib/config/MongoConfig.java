/**
 * 
 */
package com.dataflow.flow.centric.lib.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.dataflow.flow.centric.lib.sink.helper.BsonHelper;
import com.mongodb.client.MongoClient;

/**
 * @author Administrator
 *
 */
@Configuration
public class MongoConfig  extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.database}")
    private String database;
    
    @Override
    protected String getDatabaseName() {
        return database;
    }
  
    @Override
    public @Bean MongoClient mongoClient() {
        return BsonHelper.createMongoClient(host, port, username, password, database);
    }
  
    @Override
    protected String getMappingBasePackage() {
        return "com.dataflow.flow.centric.lib.nosql.repository";
    }
    
//    public static final void main(String[] args) {
//    	MongoClient client = BsonHelper.createMongoClient("192.168.99.100", "27017", "", "", "flow-centric");
//    	client.listDatabaseNames().iterator().forEachRemaining(dbName -> System.out.println("Database= " + dbName.toString()));
//    	System.out.println();
//    	MongoDatabase db = client.getDatabase("flow-centric");
//    	db.listCollectionNames().iterator().forEachRemaining(collName -> System.out.println("Collection= " + collName.toString()));
//    	System.out.println();
//    	db.createCollection("UnknownTypes");
//    	client.listDatabaseNames().iterator().forEachRemaining(dbName -> System.out.println("Database= " + dbName.toString()));
//    	System.out.println();
//    	db.drop();
//    	client.listDatabaseNames().iterator().forEachRemaining(dbName -> System.out.println("Database= " + dbName.toString()));
//    	client.close();
//    }
}
