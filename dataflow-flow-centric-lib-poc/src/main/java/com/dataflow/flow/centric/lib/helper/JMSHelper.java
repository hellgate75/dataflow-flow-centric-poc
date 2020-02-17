/**
 * 
 */
package com.dataflow.flow.centric.lib.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.dataflow.flow.centric.lib.domain.metadata.Metadata;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Administrator
 *
 */
public final class JMSHelper {
	
	
    /**
	 * 
	 */
	private JMSHelper() {
		throw new IllegalStateException("JMSHelper::Unable to make instance of helper class");
	}

	public static final byte[] gZip(final String str) {
        if ((str == null) || (str.length() == 0)) {
            throw new IllegalArgumentException("Cannot zip null or empty string");
        }
 
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
                gzipOutputStream.write(str.getBytes(StandardCharsets.UTF_8));
            }
            return byteArrayOutputStream.toByteArray();
        } catch(IOException e) {
            throw new RuntimeException("Failed to zip content", e);
        }
    }
 
	public static final byte[] gZip(final byte[] str) {
        if ((str == null) || (str.length == 0)) {
            throw new IllegalArgumentException("Cannot zip null or empty array");
        }
 
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
                gzipOutputStream.write(str);
            }
            return byteArrayOutputStream.toByteArray();
        } catch(IOException e) {
            throw new RuntimeException("Failed to zip content", e);
        }
    }
 
    public static final String gUnzip(final byte[] compressed) {
        if ((compressed == null) || (compressed.length == 0)) {
            throw new IllegalArgumentException("Cannot unzip null or empty bytes");
        }
        if (!isGzipped(compressed)) {
            return new String(compressed);
        }
 
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressed)) {
            try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
                try (InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream, StandardCharsets.UTF_8)) {
                    try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                        StringBuilder output = new StringBuilder();
                        String line;
                        while((line = bufferedReader.readLine()) != null){
                            output.append(line);
                        }
                        return output.toString();
                    }
                }
            }
        } catch(IOException e) {
            throw new RuntimeException("Failed to unzip content", e);
        }
    }
 
    public static final boolean isGzipped(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }
    
    public static final byte[] gzipMetadata(Metadata meta) {
    	try {
			ObjectMapper mapper = new ObjectMapper();
			return gZip(mapper.writeValueAsBytes(meta));
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Metadata", e);
		}
    }
    public static final Metadata gUnzipMetadata(byte[] metaBytes) {
    	try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(gUnzip(metaBytes).getBytes(StandardCharsets.UTF_8), Metadata.class);
		} catch (Exception e) {
			throw new RuntimeException("Unable to convert Metadata", e);
		}
    }

    public static final <T> byte[] gzipTemplate(T element) {
    	try {
			ObjectMapper mapper = new ObjectMapper();
			return gZip(mapper.writeValueAsBytes(element));
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Element <type: " + (element!=null ? element.getClass().getName() : "Unknown") + ">", e);
		}
    }

    public static final byte[] gzipText(String text) {
    	try {
			return gZip(text.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Text: <" + text + ">", e);
		}
    }
   
    public static final <T> T gUnzipTemplate(byte[] metaBytes, Class<T> clazz) {
    	try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(gUnzip(metaBytes).getBytes(StandardCharsets.UTF_8), clazz);
		} catch (Exception e) {
			throw new RuntimeException("Unable to convert Element <type: " + (clazz!=null ? clazz.getName() : "Unknown") + ">", e);
		}
    }
    
    public static final String gUnzipText(byte[] metaBytes) {
    	try {
			return gUnzip(metaBytes);
		} catch (Exception e) {
			throw new RuntimeException("Unable to convert Text bytes <bytes: " + (metaBytes!=null ? Arrays.toString(metaBytes) : "Unknown") + ">", e);
		}
    }

    public static final <T> byte[] jsonfyTemplate(T element) {
    	try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsBytes(element);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Element <type: " + (element!=null ? element.getClass().getName() : "Unknown") + ">", e);
		}
    }
    
    public static final <T> T templateFromJson(byte[] metaBytes, Class<T> clazz) {
    	try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(metaBytes, clazz);
		} catch (Exception e) {
			throw new RuntimeException("Unable to convert Element <type: " + (clazz!=null ? clazz.getName() : "Unknown") + ">", e);
		}
    }
    
    public static final String escapeJsonString(String json) {
    	return json.replaceAll(": ", ":").replaceAll(", ", ",").replaceAll("\\\"", "\\\\\"");
    }
    
    public static final boolean isJsonStringEscaped(String json) {
    	return json.contains("\\\"");
    }
    
    public static final String unescapeJsonString(String json) {
    	return json.replaceAll(":", ": ").replaceAll(":  ", ": ").replaceAll(",", ", ").replaceAll(",  ", ", ").replaceAll("\\\\\"", "\\\"");
    }
    
    public static final void main(String[] args) {
    	String json = "{\"name\":\"John\", \"surname\":\"Smith\", \"age\": 5}";
    	String escapedJson = escapeJsonString(json);
    	System.out.println("escapedJson=<" + escapedJson+">");
    	System.out.println("escapedJson is escaped =<" + isJsonStringEscaped(escapedJson) +">");
    	String unescapedJson = unescapeJsonString(json);
    	System.out.println("unescapedJson=<" + unescapedJson+">");
    	System.out.println("unescapedJson is escaped =<" + isJsonStringEscaped(unescapedJson) +">");
    }
    
    
}
