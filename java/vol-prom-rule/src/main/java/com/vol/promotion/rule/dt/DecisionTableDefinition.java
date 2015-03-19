/**
 * 
 */
package com.vol.promotion.rule.dt;

import java.io.Serializable;
import java.util.Arrays;


/**
 * @author scott
 *
 */
public class DecisionTableDefinition implements Serializable{

	private ColumnDefinition[] cols;
	private String[][] data;
	/**
	 * @return the cols
	 */
	public ColumnDefinition[] getCols() {
		return cols;
	}
	/**
	 * @param cols the cols to set
	 */
	public void setCols(ColumnDefinition[] cols) {
		this.cols = cols;
	}
	/**
	 * @return the data
	 */
	public String[][] getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String[][] data) {
		this.data = data;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DecisionTableDefinition [cols=");
		builder.append(Arrays.toString(cols));
		builder.append(", data=");
		builder.append(Arrays.deepToString(data));
		builder.append("]");
		return builder.toString();
	}
	
	public static interface Checker<T> {
		boolean ok(T value);
	}
	
	private transient Checker[][] checkers;
	
	public Checker[][] getChecker() {
		return checkers;
	}
	
	public void setChecker(Checker[][] checkers) {
		this.checkers = checkers;
	}
	
}
