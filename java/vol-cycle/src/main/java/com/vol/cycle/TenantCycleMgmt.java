package com.vol.cycle;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.vol.common.DAO;
import com.vol.common.tenant.Tenant;
import com.vol.common.tenant.TenantUsage;
import com.vol.common.user.QuotaHistory;
import com.vol.dao.AbstractService;

public class TenantCycleMgmt extends AbstractService<Integer,Tenant> {
	@Resource(name="tenantDao")
	protected DAO<Integer,Tenant> tenantDAO;
	
	@Resource(name="tenantUsageDao")
	protected DAO<Integer,TenantUsage> tenantUsageDAO;
	
	
	@Resource(name="quotaHistoryDao")
	protected DAO<Long,QuotaHistory> quotaHisDao;
	
	@Override
	protected DAO<Integer, Tenant> getDAO() {
		return tenantDAO;
	}

	
	public boolean renew(Integer id){
		final long current = System.currentTimeMillis();
		int rc=0;
		try {
			txBegin();
			Tenant tenant = tenantDAO.get(id);
			if(tenant.getCycleStart() <= current && tenant.getCycleEnd() >= current){
				txRollback();
				return false;
			}
			long nextEnd = nextCycleEndTime(current, tenant);
			long preEnd = tenant.getCycleEnd();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("id", id);
			map.put("end", nextEnd);
			map.put("current", current);
			map.put("preEnd", preEnd);
			rc = tenantDAO.batchUpdate("tenant.renew", map);
			txCommit();
		} catch (Exception e) {
			txRollback(e);
		}
		return rc==1;
	}
	
	public boolean summaryUsage(Integer id,Short volumeType){
		
		try {
			txBegin();
			Tenant tenant = tenantDAO.get(id);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("tenantId", id);
			map.put("volumeType", volumeType);
			List<TenantUsage> last = tenantUsageDAO.query("tenantUsage.last", map, 1);
			long start=0;
			if(last!=null && !last.isEmpty()){
				start = last.get(0).getEnd();
			}
			long end = tenant.getCycleStart();
			if(start>=end){
				txRollback();
				return false;
			}
			Map<String, Object> parameters=  new HashMap<String,Object>();
			parameters.put("tenantId", id);
			parameters.put("start", start);
			parameters.put("end", end);
			parameters.put("volumeType", volumeType);
			Long sum = quotaHisDao.queryAggregate("quotaHis.sum", parameters);
			TenantUsage usage = new TenantUsage();
			usage.setTenantId(id);
			usage.setEnd(end);
			usage.setStart(start);
			usage.setUsage(sum);
			usage.setVolumeType(volumeType);
			initEntity(usage);
			tenantUsageDAO.create(usage);
			txCommit();
			return true;
		} catch (Exception e) {
			txRollback(e);
			return false;
		}
	}
	
	
	
	
	private long nextCycleEndTime(long now, Tenant tenant){
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
