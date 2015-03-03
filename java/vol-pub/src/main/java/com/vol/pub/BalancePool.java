package com.vol.pub;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.vol.common.user.Bonus;

public class BalancePool {
	private int hostBalance;
	private AtomicLong balance = new AtomicLong();
	private AtomicLong maximum = new AtomicLong();
	private List<Bonus> pendingBonus = new LinkedList<Bonus>();
	private final Lock lock = new ReentrantLock();
	private boolean isFinal;
	
	public Bonus grant(int userId, String userName, long size){
		long current=balance.get();
		long newBalance;
		while(true){// update balance
			if(current<=0){
				return null;
			}
			newBalance = current-size;
			if(newBalance<0){
				if(!isFinal){// last quota
					size=current;
					newBalance=0;
				}else{
					// quota is not enough, try next time.
					return null;
				}
			}		
			if(balance.compareAndSet(current, newBalance)){
				break;
			}
			current=balance.get();
		}
		
		Bonus bonus = new Bonus();
		bonus.setSize(size);
		lock.lock();
		try{
			pendingBonus.add(bonus);
		}finally{
			lock.unlock();
		}		
		return bonus;
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
