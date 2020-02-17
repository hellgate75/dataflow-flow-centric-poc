/**
 * 
 */
package com.dataflow.flow.centric.ms.process.service;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.Document;
import org.springframework.stereotype.Component;

import com.mongodb.DBRef;

/**
 * This fake service should enquire and recover information to transform 
 * Models and Indexes using another MicroService
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Component
public class KeyManagerService {

	public static final String toString(BsonValue value) {
		if ( value==null || value.isNull() ) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		switch ( value.getBsonType() ) {
			case BINARY:
				return new String(value.asBinary().getData(), StandardCharsets.UTF_8);
			case BOOLEAN:
				return "" + value.asBoolean().getValue();
			case DATE_TIME:
				return df.format(new Date(value.asDateTime().getValue()));
			case TIMESTAMP:
				return df.format(new Date(value.asTimestamp().getValue()));
			case DECIMAL128:
				return value.asDecimal128().getValue().bigDecimalValue().toPlainString();
			case DOUBLE:
				return ""+value.asDouble().getValue();
			case INT32:
				return "" + value.asInt32().longValue();
			case INT64:
				return "" + value.asInt64().longValue();
			case STRING:
				return value.asString().getValue();
			case OBJECT_ID:
			    return value.asObjectId().getValue().toString();
			case DB_POINTER:
			    return new DBRef(value.asDBPointer().getNamespace(), value.asDBPointer().getId()).toString();
			case ARRAY:
				return value.asArray().stream().map(v->toString(v)).reduce( "", (p, n) -> p += (p.length() > 0 ? "-" : "") + n );
			case DOCUMENT:
				return value.asDocument().toJson();
			default:
		}
		return "";
	}
	
	public static final Object getValueOf(BsonValue value) {
		switch (value.getBsonType()) {
		  case INT32:
		    return value.asInt32().getValue();
		  case INT64:
		    return value.asInt64().getValue();
		  case STRING:
		    return value.asString().getValue();
		  case DECIMAL128:
		    return value.asDecimal128().doubleValue();
		  case DOUBLE:
		    return value.asDouble().getValue();
		  case BOOLEAN:
		    return value.asBoolean().getValue();
		  case OBJECT_ID:
		    return value.asObjectId().getValue();
		  case DB_POINTER:
		    return new DBRef(value.asDBPointer().getNamespace(), value.asDBPointer().getId());
		  case BINARY:
		    return value.asBinary().getData();
		  case DATE_TIME:
		    return new Date(value.asDateTime().getValue());
		  case SYMBOL:
		    return value.asSymbol().getSymbol();
		  case ARRAY:
		    return value.asArray().toArray();
		  case DOCUMENT:
		    return Document.parse(value.asDocument().toJson());
		  default:
		    return value;
		}
	}
	
	private static final List<String> valuesIn(BsonDocument document, List<String> keys) {
		List<String> values = new ArrayList<String>(0);
		values.addAll(
			document.entrySet()
						.parallelStream()
						.map( e -> {
							String key = e.getKey();
							BsonValue value = e.getValue();
							if ( !value.isDocument() && keys.contains(key) ) {
								if ( value.isArray() ) {
									if ( ! value.isNull()  )
										return Arrays.asList(toString(value));
								} else {
									if ( ! value.isNull()  )
										return Arrays.asList(toString(value));
								}
							} else if ( ! value.isNull() && value.isDocument() ) {
								return valuesIn(value.asDocument(), keys);
							}
							return Arrays.asList(new String[0]);
						})
						.flatMap(List::stream)
						.filter( k -> k != null && ! k.trim().isEmpty() )
						.collect(Collectors.toList())
		);
		return values;
	}
	
	public String collectModelFrom(BsonDocument document, List<String> keys) {
		return valuesIn(document, keys).parallelStream().reduce("", (p, n) -> p += (p.length() > 0 ? "-" : "") + n);
	}
	
	public String collectIndexesFrom(BsonDocument document, List<String> keys) {
		return valuesIn(document, keys).parallelStream().reduce("", (p, n) -> p += (p.length() > 0 ? "-" : "") + n);
	}
}
