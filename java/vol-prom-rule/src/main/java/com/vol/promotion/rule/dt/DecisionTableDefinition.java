/**
 * 
 */
package com.vol.promotion.rule.dt;

import java.util.List;
import java.util.Map;

/**
 * @author scott
 *
 */
public class DecisionTableDefinition {

	private List<ColumnDefinition> columns;
	private List<Map<String,String>> table;
	
	/**
	 * @return the columns
	 */
	public List<ColumnDefinition> getColumns() {
		return columns;
	}
	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<ColumnDefinition> columns) {
		this.columns = columns;
	}
	/**
	 * @return the table
	 */
	public List<Map<String, String>> getTable() {
		return table;
	}
	/**
	 * @param table the table to set
	 */
	public void setTable(List<Map<String, String>> table) {
		this.table = table;
	}
}
