package com.vol.rating;

import java.util.List;

/**
 * @author scott
 *
 */
public class RatingPlan {
	public static final char T='T',G='G',M='M',K='K',B='B';
	
	public static class RatingTier{
		private double from;
		private double to;
		private double rate;
		/**
		 * @return the from
		 */
		public double getFrom() {
			return from;
		}
		/**
		 * @param from the from to set
		 */
		public void setFrom(double from) {
			this.from = from;
		}
		/**
		 * @return the to
		 */
		public double getTo() {
			return to;
		}
		/**
		 * @param to the to to set
		 */
		public void setTo(double to) {
			this.to = to;
		}
		/**
		 * @return the rate
		 */
		public double getRate() {
			return rate;
		}
		/**
		 * @param rate the rate to set
		 */
		public void setRate(double rate) {
			this.rate = rate;
		}
		
		
	}

	
	
	private char unit;
	private List<RatingTier> tiers;
	

	/**
	 * @return the unit
	 */
	public char getUnit() {
		return unit;
	}



	/**
	 * @param unit the unit to set
	 */
	public void setUnit(char unit) {
		this.unit = unit;
	}



	/**
	 * @return the tiers
	 */
	public List<RatingTier> getTiers() {
		return tiers;
	}



	/**
	 * @param tiers the tiers to set
	 */
	public void setTiers(List<RatingTier> tiers) {
		this.tiers = tiers;
	}
}
