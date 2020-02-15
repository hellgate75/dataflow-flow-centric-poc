/**
 * 
 */
package com.dataflow.flow.centric.lib.stream;

import java.util.Collection;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public interface Transformer<T, K> {
	/**
	 * @param dataBean
	 * @return
	 */
	T transform(K dataBean);

	/**
	 * @param dataBeanList
	 * @return
	 */
	Collection<T> transformAll(Collection<K> dataBeanList);
	
	/**
	 * @return
	 */
	Collection<T> complete();
}
