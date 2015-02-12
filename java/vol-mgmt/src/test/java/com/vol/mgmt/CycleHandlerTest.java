/**
 * 
 */
package com.vol.mgmt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import com.vol.common.tenant.Tenant;


/**
 * @author scott
 *
 */
public class CycleHandlerTest extends BaseTest{

	@Resource
	CycleHandler cycleHandler;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	@Test
	public void testCycle() throws ParseException{
		Date date = format.parse("2001-01-04T12:08:56.235");
		Tenant tenant = new Tenant();
		tenant.setCycleType(0);
		long result = cycleHandler.calculateCycleEndTime(date.getTime(), tenant);
		Date date2 = format.parse("2001-02-01T00:00:00.000");
		System.out.println(""+new Date(result));
		Assert.assertEquals(date2.getTime(), result);
		
		
	}
	
	@Test
	public void testCycle2() throws ParseException{
		Date date = format.parse("2001-01-15T12:08:56.235");
		Tenant tenant = new Tenant();
		tenant.setCycleType(0);
		long result = cycleHandler.calculateCycleEndTime(date.getTime(), tenant);
		Date date2 = format.parse("2001-02-01T00:00:00.000");
		System.out.println(""+new Date(result));
		Assert.assertEquals(date2.getTime(), result);
	}
}
