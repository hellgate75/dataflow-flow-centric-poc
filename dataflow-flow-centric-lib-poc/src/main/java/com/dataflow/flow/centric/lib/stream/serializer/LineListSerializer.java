/**
 * 
 */
package com.dataflow.flow.centric.lib.stream.serializer;

import java.util.List;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class LineListSerializer extends StdConverter<List<String>, String> {

	public LineListSerializer() {
		super();
	}

	@Override
	public String convert(List<String> value) {
		StringBuilder builder = new StringBuilder("[");

		for (String line : value) {
			if (builder.length() > 1) {
				builder.append(", ");
			}
			builder.append("\"").append(line).append("\"");
		}

		builder.append("]");

		return builder.toString();
	}

}
