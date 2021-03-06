/**
 * 
 */
package com.dataflow.flow.centric.lib.bson;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.bson.RawBsonDocument;

import com.dataflow.flow.centric.lib.helper.JMSHelper;

/**
 * @author Administrator
 *
 */
public class RawBsonDocumentWrapper implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4097523956947277945L;

	private String compressedContent;
	
	private boolean customCompressed = false;
	
	private String compressionNote;

	/**
	 * 
	 */
	public RawBsonDocumentWrapper() {
		super();
	}
	
	/**
	 * @param doc
	 */
	public RawBsonDocumentWrapper(RawBsonDocument doc) {
		super();
		compressedContent=new String(JMSHelper.gZip(doc.toJson()), StandardCharsets.UTF_8);
	}
	
	/**
	 * Create wrapper with custom compressed bytes, decompression and parse is on your 
	 * responsibility. Please a reminder for the compression format.
	 * @param compressedBytes
	 * @param compressionNote
	 */
	public RawBsonDocumentWrapper(byte[] compressedBytes, String compressionNote) {
		super();
		this.compressedContent=new String(compressedBytes, StandardCharsets.UTF_8);
		this.customCompressed = true;
		this.compressionNote=compressionNote;
	}
	
	/**
	 * @return
	 */
	public RawBsonDocument getDocument() {
		if ( isCustomCompressed()  ) {
			throw new IllegalStateException("RawBsonDocumentWrapper::getDocument -> wrapper created with custom byte compression. Get compressed bytes instead.");
		}
		return RawBsonDocument.parse(JMSHelper.gUnzip(compressedContent.getBytes(StandardCharsets.UTF_8)));
	}

	/**
	 * @return the compressionNote
	 */
	public String getCompressionNote() {
		return compressionNote;
	}

	/**
	 * @return the customCompressed
	 */
	public boolean isCustomCompressed() {
		return customCompressed;
	}

	/**
	 * @return the compressedContent
	 */
	public byte[] getCompressedContent() {
		return compressedContent.getBytes(StandardCharsets.UTF_8);
	}

	/**
	 * @param compressedContent the compressedContent to set
	 */
	public void setCompressedContent(byte[] compressedContent) {
		this.compressedContent = new String(compressedContent, StandardCharsets.UTF_8);
	}

	/**
	 * @param customCompressed the customCompressed to set
	 */
	public void setCustomCompressed(boolean customCompressed) {
		this.customCompressed = customCompressed;
	}

	/**
	 * @param compressionNote the compressionNote to set
	 */
	public void setCompressionNote(String compressionNote) {
		this.compressionNote = compressionNote;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(compressedContent.getBytes(StandardCharsets.UTF_8));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RawBsonDocumentWrapper other = (RawBsonDocumentWrapper) obj;
		if (!compressedContent.equals(other.compressedContent))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RawBsonWrapper [document=" + getDocument().toJson() + "]";
	}
	
	
}
