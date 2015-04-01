/**
 * 
 */
package com.vol.common.tenant;

import com.vol.common.BaseEntity;

/**
 * @author scott
 *
 */
public class TenantUsage extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The id.
	 */
	private int id;
	
	private int tenantId;
	
	private long start;
	
	private long end;
	
	private long usage;
	
	private short volumeType;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the tenantId
	 */
	public int getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the start
	 */
	public long getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(long start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public long getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(long end) {
		this.end = end;
	}

	/**
	 * @return the usage
	 */
	public long getUsage() {
		return usage;
	}

	/**
	 * @param usage the usage to set
	 */
	public void setUsage(long usage) {
		this.usage = usage;
	}

	/**
	 * @return the volumeType
	 */
	public short getVolumeType() {
		return volumeType;
	}

	/**
	 * @param volumeType the volumeType to set
	 */
	public void setVolumeType(short volumeType) {
		this.volumeType = volumeType;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}
