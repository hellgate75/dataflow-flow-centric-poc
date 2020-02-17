/**
 * 
 */
package com.dataflow.flow.centric.lib.sink.helper;

import java.util.ArrayList;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientSettings.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
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
    						.applicationName("FlowCentricMicroservice")
    						.applyConnectionString(url);
    	if ( username != null && ! username.isEmpty() && password != null && ! password.isEmpty() )
    		builder = builder.credential(MongoCredential.createPlainCredential(username, database, password.toCharArray()));
        return MongoClients.create(builder.build());
	}
	
	private static final long MAX_RETRY = 5;
	
	public static final List<String> getMongoDbCollections(MongoClient mongoClient, MongoDatabase db) {
		return getMongoDbCollections(mongoClient, db, MAX_RETRY);
	}
	
	private static final List<String> getMongoDbCollections(MongoClient mongoClient, MongoDatabase db, long countdown) {
		List<String> collections = new ArrayList<String>(0);
		try {
			db.listCollectionNames().iterator().forEachRemaining(name -> collections.add(name.toString()));
			return collections;
		} catch (Exception e) {
			if ( countdown > 0 ) {
				db = mongoClient.getDatabase(db.getName());
				return getMongoDbCollections(mongoClient, db, countdown -1);
			}
		}
		return collections;
	}
	public static final Document saveMongoDbElement(MongoClient mongoClient, MongoDatabase db, String collectionName, Document mongoDocument) {
		return saveMongoDbElement(mongoClient, db, collectionName, mongoDocument, MAX_RETRY);
	}
	
	private static final Document saveMongoDbElement(MongoClient mongoClient, MongoDatabase db, String collectionName, Document mongoDocument, long countdown) {
		
		try {
			MongoCollection<Document> collection = db.getCollection(collectionName);
			collection.insertOne(mongoDocument);
		} catch (Exception e) {
			if ( countdown > 0 ) {
				db = mongoClient.getDatabase(db.getName());
				return saveMongoDbElement(mongoClient, db, collectionName, mongoDocument, countdown -1);
			}
		}
		return mongoDocument;
	}
	
	public static final MongoCollection<Document> createMongoDbCollection(MongoClient mongoClient, MongoDatabase db, String collectionName) {
		return createMongoDbCollection(mongoClient, db, collectionName, MAX_RETRY);
	}
	
	private static final MongoCollection<Document> createMongoDbCollection(MongoClient mongoClient, MongoDatabase db, String collectionName, long countdown) {
		
		try {
			db.createCollection(collectionName);
			return db.getCollection(collectionName);
		} catch (Exception e) {
			if ( countdown > 0 ) {
				db = mongoClient.getDatabase(db.getName());
				return createMongoDbCollection(mongoClient, db, collectionName, countdown -1);
			}
		}
		return  db.getCollection(collectionName);
	}

}
