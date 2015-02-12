/**
 * 
 */
package com.vol.mgmt;

import java.util.Calendar;

import com.vol.common.tenant.Tenant;

/**
 * @author scott
 *
 */
public class CycleHandler {

	
	public long calculateCycleEndTime(long now, Tenant tenant){
		int cycle = tenant.getCycleType();

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(now);
		
		int cycleType = getCycleType(cycle);
		switch(cycleType){
			case 0:// month
				int startDay=getStartDay(cycle);
				int startHour=getStartHour(cycle);
				int startMinute=getStartMinute(cycle);
				cal.add(Calendar.MONTH, 1);
				cal.set(Calendar.DAY_OF_MONTH, startDay);
				cal.set(Calendar.HOUR_OF_DAY, startHour);
				cal.set(Calendar.MINUTE, startMinute);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				return cal.getTimeInMillis();
				
		}
		throw new IllegalArgumentException("Unknown Cycle Definition "+cycle);
	}

	/**
	 * @param cycle
	 * @return
	 */
	private int getStartMinute(int cycle) {
		int number = cycle%100;
		return number>=60?59:number;
	}

	/**
	 * @param cycle
	 * @return
	 */
	private int getStartHour(int cycle) {
		int number = cycle%10000/100;
		return number>=24?23:number;
	}

	/**
	 * @param cycle
	 * @return
	 */
	private int getStartDay(int cycle) {
		int number = cycle%1000000/10000;
		if(number == 0){
			number=1;
		}
		return number>=29?28:number;
	}

	/**
	 * @param cycle
	 * @return
	 */
	private int getCycleType(int cycle) {
		return cycle%100000000/1000000;
	} 
}
