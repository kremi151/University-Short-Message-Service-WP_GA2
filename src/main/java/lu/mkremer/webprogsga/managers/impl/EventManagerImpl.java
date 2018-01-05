package lu.mkremer.webprogsga.managers.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.ejb.Singleton;

import lu.mkremer.webprogsga.events.OnUserAccountUpdatedListener;
import lu.mkremer.webprogsga.managers.EventManager;
import lu.mkremer.webprogsga.persistence.User;

@Singleton
public class EventManagerImpl implements EventManager{
	
	private final HashMap<Long, OnUserAccountUpdatedListener> accUpdatedListeners = new HashMap<>();
	private final Random rand = new Random(System.currentTimeMillis());

	@Override
	public synchronized long addAccountUpdatedListener(OnUserAccountUpdatedListener listener) {
		while(true) {
			Long key = rand.nextLong();
			if(!accUpdatedListeners.containsKey(key)) {
				accUpdatedListeners.put(key, listener);
				return key.longValue();//Inefficient but working
			}
		}
	}

	@Override
	public synchronized boolean removeAccountUpdatedListener(long id) {
		return accUpdatedListeners.remove(new Long(id)) != null;
	}

	@Override
	public synchronized void dispatchAccountUpdated(User user) {
		for(Map.Entry<Long, OnUserAccountUpdatedListener> e : accUpdatedListeners.entrySet()) {
			e.getValue().onAccountUpdated(user);
		}
	}

}
