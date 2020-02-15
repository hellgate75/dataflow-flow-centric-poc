/**
 * 
 */
package com.dataflow.flow.centric.lib.sink.helper;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientSettings.Builder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * @author Administrator
 *
 */
public final class BsonHelper {

	/**
	 * 
	 */
	private BsonHelper() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static final BsonDocument jsonTextToBSON(String text) {
		return BsonDocument.parse(text);
	}
	
	public static final String bsonToJsonText(BsonDocument object) {
		return object.toJson();
	}
	
	public static final MongoClient createMongoClient(String host, String port, String username, String password, String database) {
    	ConnectionString url = new ConnectionString(String.format("mongodb://%s:%s", host, port));
    	Builder builder = MongoClientSettings.builder()
    						.applicationName("FlowCentrixMicroservice")
    						.applyConnectionString(url);
    	if ( username != null && ! username.isEmpty() && password != null && ! password.isEmpty() )
    		builder = builder.credential(MongoCredential.createPlainCredential(username, database, password.toCharArray()));
        return MongoClients.create(builder.build());
	}
	
	public static final List<String> getMongoDbCollections(MongoDatabase db) {
		List<String> collections = new ArrayList<String>(0);
		db.listCollectionNames().iterator().forEachRemaining(name -> collections.add(name.toString()));
		return collections;
	}

}
