/**
 * 
 */
package com.dataflow.flow.centric.lib.domain.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.BsonDocument;

/**
 * @author Administrator
 *
 */
public class Metadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 697408206396014597L;
	
	private String typeName;
	
	private List<MetaKey> fieldMetaList = new ArrayList<MetaKey>(0);

	/**
	 * @param typeName
	 */
	public Metadata(String typeName) {
		super();
		this.typeName = typeName;
	}

	/**
	 * @return the metaKeyList
	 */
	public List<MetaKey> getMetaKeyList() {
		return fieldMetaList;
	}

	/**
	 * @param metaKeyList the metaKeyList to set
	 */
	public void setFieldMetaList(List<MetaKey> metaKeyList) {
		this.fieldMetaList = metaKeyList;
	}

	
	/**
	 * @param child
	 */
	public void addFieldMetaKey(MetaKey child) {
		if ( child != null ) {
			fieldMetaList.add(child);
		}
	}
	
	/**
	 * @param childs
	 */
	public void addMoreFieldMetaKeya(Collection<MetaKey> childs) {
		if ( childs != null && childs.size() > 0) {
			fieldMetaList.addAll(childs.stream()
									.filter( child -> child != null )
									.collect(Collectors.toList()));
		}
	}
	/**
	 * @return the optional of metaKey
	 */
	public Optional<MetaKey> getFieldMetaByName(String name) {
		if ( name == null || name.isEmpty() )
			return Optional.empty();
		return fieldMetaList.parallelStream().filter( entry -> name.equals(entry.getName()) ).findFirst();
	}
	/**
	 * @return the optional of metaKey
	 */
	public Optional<MetaKey> getFieldMetaByNameIgnoreCase(String name) {
		if ( name == null || name.isEmpty() )
			return Optional.empty();
		return fieldMetaList.parallelStream().filter( entry -> name.equalsIgnoreCase(entry.getName())).findFirst();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldMetaList == null) ? 0 : fieldMetaList.hashCode());
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
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
		Metadata other = (Metadata) obj;
		if (fieldMetaList == null) {
			if (other.fieldMetaList != null)
				return false;
		} else if (!fieldMetaList.equals(other.fieldMetaList))
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Metadata [typeName=" + typeName + ", fieldMetaList=" + fieldMetaList + "]";
	}

	/**
	 * @return
	 */
	public String toJson() {
		return "{\"typeName\":\"" + typeName + "\",\"fieldMetaList\":[" + 
													fieldMetaList.stream()
													.map( field -> field.toJson() )
													.reduce("", (p, n) -> p += (p.length() > 0 ? ", " : "") + n ) + 
												"]}";
	}
	
	public BsonDocument asDocument() {
		return BsonDocument.parse(toJson());
	}
	
	public static final Metadata metaOf(BsonDocument d, String typeName) {
		if ( d == null ) {
			return null;
		}
		Metadata meta = new Metadata(typeName);
		meta.setFieldMetaList(d.entrySet().stream()
				.map(entry -> MetaKey.metaOf(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList()));
		return meta;
	}
}
