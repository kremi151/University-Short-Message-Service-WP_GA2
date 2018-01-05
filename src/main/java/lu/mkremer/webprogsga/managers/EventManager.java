package lu.mkremer.webprogsga.managers;

import javax.ejb.Local;

import lu.mkremer.webprogsga.events.OnUserAccountUpdatedListener;
import lu.mkremer.webprogsga.persistence.User;

@Local
public interface EventManager {

	long addAccountUpdatedListener(OnUserAccountUpdatedListener listener);
	boolean removeAccountUpdatedListener(long id);
	void dispatchAccountUpdated(User user);
}
