package com.vol.pub;

import com.vol.common.BaseEntity;

public class PromotionHostBalance extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private int balanceId;
	
	private String host;
	
	private long startup;
	
	/**
	 * The promotion id.
	 */
	private int promotionId;
	
	/**
	 * The reserved.
	 */
	private long reserved;	
}
