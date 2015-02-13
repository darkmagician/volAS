/**
 * 
 */
package com.vol.rest.result;

import com.vol.common.user.Bonus;

/**
 * The Class BunosResult.
 *
 * @author scott
 */
public class BunosResult extends OperationResult{
	private Bonus bonus;

	/**
	 * @return the bonus
	 */
	public Bonus getBonus() {
		return bonus;
	}

	/**
	 * @param bonus the bonus to set
	 */
	public void setBonus(Bonus bonus) {
		this.bonus = bonus;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BunosResult [bonus=");
		builder.append(bonus);
		builder.append(", code=");
		builder.append(code);
		builder.append(", message=");
		builder.append(message);
		builder.append("]");
		return builder.toString();
	}
}
