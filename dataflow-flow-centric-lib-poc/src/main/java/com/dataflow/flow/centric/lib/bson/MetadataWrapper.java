/**
 * 
 */
package com.dataflow.flow.centric.lib.bson;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import com.dataflow.flow.centric.lib.domain.metadata.Metadata;
import com.dataflow.flow.centric.lib.helper.JMSHelper;

/**
 * @author Administrator
 *
 */
public class MetadataWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7904650752742684966L;
	
	private String compressedContent;
	
	private boolean customCompressed = false;
	
	private String compressionNote;
	
	/**
	 * 
	 */
	public MetadataWrapper() {
		super();
	}

	/**
	 * @param doc
	 */
	public MetadataWrapper(Metadata meta) {
		super();
		compressedContent=new String(JMSHelper.gzipMetadata(meta), StandardCharsets.UTF_8);
	}
	
	/**
	 * Create wrapper with custom compressed bytes, decompression and parse is on your 
	 * responsibility. Please a reminder for the compression format.
	 * @param compressedBytes
	 * @param compressionNote
	 */
	public MetadataWrapper(byte[] compressedBytes, String compressionNote) {
		super();
		this.compressedContent=new String(compressedBytes, StandardCharsets.UTF_8);
		this.customCompressed = true;
		this.compressionNote=compressionNote;
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

	/**
	 * @return
	 */
	public Metadata getMetadata() {
		if ( isCustomCompressed()  ) {
			throw new IllegalStateException("MetadataWrapper::getDocument -> wrapper created with custom byte compression. Get compressed bytes instead.");
		}
		return JMSHelper.gUnzipMetadata(compressedContent.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * @return the compressedContent
	 */
	public byte[] getCompressedContent() {
		return compressedContent.getBytes(StandardCharsets.UTF_8);
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
		MetadataWrapper other = (MetadataWrapper) obj;
		if (!compressedContent.equals(other.compressedContent))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MetadataWrapper [metadata=" + getMetadata() + "]";
	}
	
	
}
