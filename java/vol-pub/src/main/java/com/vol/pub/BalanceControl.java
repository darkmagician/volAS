package com.vol.pub;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.vol.common.user.Bonus;

public class BalanceControl {
	private int hostBalance;
	private long balance = 0;
	private final AtomicLong maximum = new AtomicLong();
	private List<Bonus> pendingBonus = new LinkedList<Bonus>();
	private final Lock lock = new ReentrantLock();
	private boolean isFinal;
	
	public Bonus grant(int userId, String userName, long size){
		lock.lock();
		try{
			if(balance <= 0){
				return null;
			}
			long balance2 = balance - size;
			if(balance2 < 0){
				if(isFinal){
					size = balance;
					balance = 0;
				}else{
					return null;
				}
			}
		
			Bonus bonus = new Bonus();
			bonus.setSize(size);
			pendingBonus.add(bonus);
			return bonus;
		}finally{
			lock.unlock();
		}		
	
	}
	
	
	public Runnable flushTask(){
		final List<Bonus> pendingBonus;
		lock.lock();
		try{
			pendingBonus = this.pendingBonus;
			this.pendingBonus = new LinkedList<Bonus>();
		}finally{
			lock.unlock();
		}
		
		long used=0;
		for(Bonus bonus: pendingBonus){
			used+=bonus.getSize();
		}
		maximum.addAndGet(-used);
		Runnable task = new Runnable(){

			@Override
			public void run() {
				for(Bonus bonus: pendingBonus){
					//insert bonus;
				}
				// update host balance
				// update promotion balance;
			}};
		
		return task;
	}
	
	
	public Runnable reserveTask(final long expect){
		Runnable task = new Runnable(){

			@Override
			public void run() {
				long toReserve = expect - maximum.get();
				if(toReserve > 0){
					// update host balance
					// update promotion balance;
				}
			}};
			
			return task;	
	}
}
