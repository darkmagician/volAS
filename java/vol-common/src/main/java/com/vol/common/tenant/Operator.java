/**
 * 
 */
package com.vol.common.tenant;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.vol.common.BaseEntity;

/**
 * The Class Operator.
 *
 * @author scott
 */
@JsonIgnoreProperties("password")
public class Operator extends BaseEntity {
	
	/**
	 * The id.
	 */
	private int id;
	
	/**
	 * The name.
	 */
	private String name;
	
	/**
	 * The tenant id.
	 */
	private int tenantId;
	
	/**
	 * 
	 */
	private String description;
	
	/**
	 * The password.
	 */
	private String password;
	
	
	/**
	 * 
	 */
	private String email;
	
	
	/**
	 * 
	 */
	private String phone;
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the tenant id.
	 *
	 * @return the tenantId
	 */
	public int getTenantId() {
		return tenantId;
	}
	
	/**
	 * Sets the tenant id.
	 *
	 * @param tenantId
	 *            the tenantId to set
	 */
	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}
	
	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the password.
	 *
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Operator [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", tenantId=");
		builder.append(tenantId);
		builder.append(", description=");
		builder.append(description);
		builder.append(", password=");
		builder.append(password);
		builder.append(", email=");
		builder.append(email);
		builder.append(", phone=");
		builder.append(phone);
		builder.append(", creationTime=");
		builder.append(creationTime);
		builder.append(", updateTime=");
		builder.append(updateTime);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}
}
