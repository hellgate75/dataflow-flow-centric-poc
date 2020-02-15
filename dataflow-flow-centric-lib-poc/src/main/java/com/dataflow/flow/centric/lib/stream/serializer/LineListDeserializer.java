/**
 * 
 */
package com.dataflow.flow.centric.lib.stream.serializer;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class LineListDeserializer extends StdConverter<String, List<String>> {

	@Override
	public List<String> convert(String value) {
		List<String> lines = new ArrayList<>(0);
		try {
			lines.addAll(
				new ObjectMapper().readValue(value, new TypeReference<List<String>>() {})
			);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}


	
}
