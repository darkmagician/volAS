package com.vol.rest.result;

import java.util.List;

import com.vol.common.mgmt.PagingResult;

public class PagingOperationResult<T> extends OperationResult{


	private int total;
	private List<T> rows;
	
	public PagingOperationResult(){
		
	}
	
	public PagingOperationResult(PagingResult<T> result){
		total = result.getTotal();
		rows = result.getRows();
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
