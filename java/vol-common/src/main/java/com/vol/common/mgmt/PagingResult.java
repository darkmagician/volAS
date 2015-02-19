/**
 * 
 */
package com.vol.common.mgmt;

import java.util.List;

/**
 * @author scott
 *
 */
public class PagingResult<T> {

	private int total;
	private List<T> rows;
	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}
	/**
	 * @return the rows
	 */
	public List<T> getRows() {
		return rows;
	}
	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PagingResult [total=");
		builder.append(total);
		builder.append(", rows=");
		builder.append(rows);
		builder.append("]");
		return builder.toString();
	}
	
}
