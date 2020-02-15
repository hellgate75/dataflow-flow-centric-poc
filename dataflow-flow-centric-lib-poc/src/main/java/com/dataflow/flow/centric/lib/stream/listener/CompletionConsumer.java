/**
 * 
 */
package com.dataflow.flow.centric.lib.stream.listener;

import java.util.List;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public interface CompletionConsumer<T> {

	/**
	 * @param t
	 */
	void accept(T t);

	/**
	 * @param list
	 */
	void acceptAll(List<T> list);

	/**
	 * @param elements
	 */
	@SuppressWarnings("unchecked")
	void acceptAll(T ...elements);
	
	/**
	 * 
	 */
	void complete();
}
