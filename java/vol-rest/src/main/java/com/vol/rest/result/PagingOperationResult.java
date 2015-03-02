package com.vol.rest.result;

import java.util.List;

import com.vol.common.mgmt.PagingResult;

public class PagingOperationResult extends OperationResult{


	private int total;
	@SuppressWarnings("rawtypes")
	private List rows;
	
	public PagingOperationResult(){
		
	}
	@SuppressWarnings("rawtypes")
	public PagingOperationResult(PagingResult result){
		total = result.getTotal();
		rows = result.getRows();
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@SuppressWarnings("rawtypes")
	public List getRows() {
		return rows;
	}
	@SuppressWarnings("rawtypes")
	public void setRows(List rows) {
		this.rows = rows;
	}
}
