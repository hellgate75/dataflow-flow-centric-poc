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
import org.springframework.stereotype.Component;

/**
 * This fake service should enquire and recover information to transform 
 * Models and Indexes using another MicroService
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
@Component
public class KeyManagerService {

	private static final String convert(BsonValue value) {
		if ( value==null || value.isNull() || value.isDocument() ) {
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
				return value.asDecimal128().getValue().bigDecimalValue().toString();
			case DOUBLE:
				return ""+value.asDouble().getValue();
			case INT32:
				return "" + value.asInt32().longValue();
			case INT64:
				return "" + value.asInt64().longValue();
			case STRING:
				return value.asString().toString();
			default:
		}
		return "";
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
										return Arrays.asList(value.asArray().stream().map(v->convert(v)).reduce( "", (p, n) -> p += n ));
								} else {
									if ( ! value.isNull()  )
										return Arrays.asList(convert(value));
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
