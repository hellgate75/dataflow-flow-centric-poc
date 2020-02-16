/**
 * 
 */
package com.dataflow.flow.centric.ms.sink.service;

import org.bson.BsonDocument;
import org.springframework.stereotype.Component;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Component
public class NamesAndStandardsComponent {
	
	
	/**
	 * @param currentName
	 * @param document
	 * @return
	 */
	public String normalizeMongoDbCollectionName(String currentName, BsonDocument document) {
		if ( currentName == null || currentName.isEmpty() )
			return "unknow-category";
		// I have no service or database at this stage to enquire with given data and recover database
		// reference data, so returning the same name;
		return currentName;
	}
}
