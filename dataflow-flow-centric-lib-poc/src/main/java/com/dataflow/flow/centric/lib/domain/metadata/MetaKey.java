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

import org.bson.BsonType;
import org.bson.BsonValue;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class MetaKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4080438430598287033L;
	
	private String name;
	
	private BsonType type;
	
	private List<BsonType> subTypes = new ArrayList<BsonType>(0);
	
	private boolean container = false;
	
	private List<MetaKey> metaKeyList = new ArrayList<MetaKey>(0);
	/**
	 * @param name
	 * @param type
	 * @param container
	 */
	public MetaKey(String name, BsonType type, boolean container) {
		super();
		this.name = name;
		this.type = type;
		this.container = container;
	}
	
	/**
	 * @param child
	 */
	public void addChildMetaKey(MetaKey child) {
		if ( child != null ) {
			metaKeyList.add(child);
		}
	}
	
	/**
	 * @param childs
	 */
	public void addMoreChildMetaKeyz(Collection<MetaKey> childs) {
		if ( childs != null && childs.size() > 0) {
			metaKeyList.addAll(childs.stream()
									.filter( child -> child != null )
									.collect(Collectors.toList()));
		}
	}
	
	/**
	 * @param child
	 */
	public void addChildType(BsonType child) {
		if ( child != null ) {
			subTypes.add(child);
		}
	}
	
	/**
	 * @param childs
	 */
	public void addMoreChildTypez(Collection<BsonType> childs) {
		if ( childs != null && childs.size() > 0) {
			subTypes.addAll(childs.stream()
									.filter( child -> child != null )
									.collect(Collectors.toList()));
		}
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the type
	 */
	public BsonType getType() {
		return type;
	}
	/**
	 * @return the subType
	 */
	public List<BsonType> getSubTypes() {
		return subTypes;
	}

	/**
	 * @return the container
	 */
	public boolean isContainer() {
		return container;
	}
	/**
	 * @return the metaKeyList
	 */
	public List<MetaKey> getMetaKeyList() {
		return metaKeyList;
	}
	/**
	 * @return the optional of metaKey
	 */
	public Optional<MetaKey> getMetaKeyByName(String name) {
		if ( name == null || name.isEmpty() )
			return Optional.empty();
		return metaKeyList.parallelStream().filter( entry -> name.equals(entry.getName()) ).findFirst();
	}
	/**
	 * @return the optional of metaKey
	 */
	public Optional<MetaKey> getMetaKeyByNameIgnoreCase(String name) {
		if ( name == null || name.isEmpty() )
			return Optional.empty();
		return metaKeyList.parallelStream().filter( entry -> name.equalsIgnoreCase(entry.getName())).findFirst();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (container ? 1231 : 1237);
		result = prime * result + ((metaKeyList == null) ? 0 : metaKeyList.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		MetaKey other = (MetaKey) obj;
		if (container != other.container)
			return false;
		if (metaKeyList == null) {
			if (other.metaKeyList != null)
				return false;
		} else if (!metaKeyList.equals(other.metaKeyList))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MetaKey [name=" + name + ", type=" + type + ", container=" + container + ", metaKeyList=" + metaKeyList
				+ "]";
	}
	
	/**
	 * @return
	 */
	public String toJson() {
		return "{\"name\":\"" + name + "\",\"type\":\"" + 
				type + "\",\"container\":" + 
				container + ",\"metaKeyList\":[" + 
				metaKeyList.stream()
				.map( field -> field.toJson() )
				.reduce("", (p, n) -> p += (p.length() > 0 ? ", " : "") + n ) + 
			"]}";
	}
	
	protected static final MetaKey metaOf(String name, BsonValue value) {
		if ( name == null || name.isEmpty() || value == null ) {
			return null;
		}
		BsonType type = value.getBsonType();
		boolean container = type.isContainer();
		MetaKey meta = new MetaKey(name, type, container);
		if ( container ) {
			if ( value.isArray() ) {
				meta.addMoreChildTypez(
				value.asArray()
						.parallelStream()
						.map( subValue -> subValue.getBsonType() )
						.distinct()
						.collect(Collectors.toList())
				);
			} else if ( value.isDocument() ) {
				meta.addMoreChildMetaKeyz(
				value.asDocument()
				.entrySet()
				.parallelStream()
				.map( keyValuePair -> new MetaKey(keyValuePair.getKey(), keyValuePair.getValue() != null ? keyValuePair.getValue().getBsonType() : null, container) )
				.distinct()
				.collect(Collectors.toList())
				);
			}
		}
		return meta;
	}

}
