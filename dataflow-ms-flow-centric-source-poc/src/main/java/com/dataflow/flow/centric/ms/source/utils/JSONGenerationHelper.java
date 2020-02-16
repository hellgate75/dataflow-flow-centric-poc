/**
 * 
 */
package com.dataflow.flow.centric.ms.source.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.bson.BsonDocument;


/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public final class JSONGenerationHelper {
	private static int NAMES_LENGTH=15;
	private static int VALUES_LENGTH=30;
	private static final String dict = "ABCDEFGHJKILMNOPQRSTUVWXYZabcdefghjklmnopqrstuvwxyz";
	
	private static final List<String> listOfModels = new ArrayList<String>(0);
	
	static {
		listOfModels.add("generic");
		listOfModels.add("xh546");
		listOfModels.add("th546");
		listOfModels.add("xh346");
		listOfModels.add("th346");
		listOfModels.add("st223");
		listOfModels.add("nt223");
	}
	
	private JSONGenerationHelper() {
		throw new IllegalStateException("JSONGenerationHelper:Unable to create instance of utility class");
	}
	
	private static final String pickUpAModel() {
		if ( listOfModels.size() == 0 )
			return "unknown";
		int r = (int) Math.floor(Math.random() * (listOfModels.size() - 1));
		return listOfModels.get(r);
	}
	
	private static final char pickUpAChar() {
		int r = (int) Math.floor(Math.random() * (dict.length() - 1));
		return dict.charAt(r);
	}

	private static final String pickUpAString() {
		return pickUpAString(VALUES_LENGTH);
	}
	private static final String pickUpAString(int limit) {
		int r = (int) Math.ceil(Math.random() * limit) + 5;
		return IntStream.range(1, r).mapToObj( i -> ""+JSONGenerationHelper.pickUpAChar()).reduce("", (p, n) -> p += n);
	}
	
	private static final String pickUpAField() {
		return "\"" + pickUpAString(NAMES_LENGTH) + "\": \"" + pickUpAString() + "\"";
	}
	
	private static final String pickUpAField(String name) {
		return "\"" + name + "\": \"" + pickUpAString() + "\"";
	}
	
	private static final String pickUpAnIdField() {
		int r = (int) Math.ceil(Math.random() * 999999999) + 55;
		return "\"" + pickUpAString(NAMES_LENGTH) + "\": " + r;
	}
	
	private static final String pickUpAnIdField(String name) {
		int r = (int) Math.ceil(Math.random() * 999999999) + 66;
		return "\"" + name + "\": " + r;
	}
	
	private static final void pickupAGroup(BsonDocument parent, int noSubGroups, int limit) {
		int r = (int) Math.ceil(Math.random() * limit) + 3;
		String name = pickUpAString(NAMES_LENGTH);
		final StringBuffer group=new StringBuffer("{");
		group.append(pickUpAnIdField()+", ");
		group.append(pickUpAField()+", ");
		group.append(pickUpAnIdField());
		group.append(IntStream.range(1, r).mapToObj( i -> "," + ( i % 2==0 ? pickUpAField() : pickUpAnIdField() ) ).reduce("", (p, n) -> p += n));
		group.append("}");
		BsonDocument subDoc = BsonDocument.parse(group.toString()); 
		if ( noSubGroups > 0 ) {
			IntStream.range(1, noSubGroups).forEach( i -> { pickupAGroup(subDoc, noSubGroups - 1, limit); });
		}
		parent.append(name, subDoc);
	}

	public static final BsonDocument generateJSON(String modelFields, String indeFields, int maxFields, int maxGroups) {
		int r = (int) Math.ceil(Math.random() * maxFields) + 3;
		int r2 = (int) Math.ceil(Math.random() * maxGroups) + 1;
		String model = pickUpAModel();
		List<String> modelFieldList = modelFields!= null && !modelFields.isEmpty() ? Arrays.asList(modelFields.split(",")) : Arrays.asList("_model");
		List<String> indeFieldList= indeFields!= null && !indeFields.isEmpty() ? Arrays.asList(indeFields.split(",")): Arrays.asList("_index");
		final StringBuffer outcome = new StringBuffer("{");
		final AtomicBoolean taken = new AtomicBoolean(false); 
		String models = modelFieldList.stream().map(name -> {
			String value = "";
			if ( ! taken.get() ) {
				value=" \"" + name + "\": \""+model+"\",";
			} else {
				value=" \"" + name + "\": \"\",";
			}
			taken.set(true);
			return value;
		})
		.reduce("", (p, n) -> p += n);
		models = models.substring(0, models.length() - 1);
		outcome.append(models);
		outcome.append( indeFieldList.stream().map(name -> {
							String value = "";
							if ( ! taken.get() ) {
								value=", " + pickUpAField(name);
							} else {
								value=", " + pickUpAnIdField(name);
							}
							taken.set(! taken.get());
							return value;
						})
						.reduce("", (p, n) -> p += n)
		);
		final AtomicInteger in = new AtomicInteger(0);
		IntStream.range(1, r).forEach( i -> {
			if ( i%2 == 0 ) {
				outcome.append(", " + pickUpAField());
			} else {
				in.incrementAndGet();
			}
		});
		outcome.append(" }");
		BsonDocument doc = BsonDocument.parse(outcome.toString());
		if ( in.get() > 0 ) {
			IntStream.range(1, in.get()).forEach( i -> {
				pickupAGroup(doc, r2, r);
			});
		}
		return doc;
	}
	
//	public static final void main(String[] args) {
//			for ( int i=0; i<50; i++ ) {
//				long time = System.currentTimeMillis();
//				String json = generateJSON("_model,field,type", "", 20, 5).toJson();
//				long diff = System.currentTimeMillis() - time;
//				System.out.println("Sample JSON: " + json.length());
//				System.out.println("Time: " + (((double)diff)/1000) + " sec");
//				//System.out.println(json);
//			}
//	}
}
