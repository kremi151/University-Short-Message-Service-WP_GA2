package lu.mkremer.webprogsga.events;

import lu.mkremer.webprogsga.persistence.User;

public interface OnUserAccountUpdatedListener {

	void onAccountUpdated(User user);
	
}
