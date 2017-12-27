package lu.mkremer.webprogsga.managers;

import java.util.List;

import javax.ejb.Local;

import lu.mkremer.webprogsga.persistence.Channel;
import lu.mkremer.webprogsga.persistence.Tweed;
import lu.mkremer.webprogsga.persistence.User;

@Local
public interface MessageManager {
	
	List<Tweed> loadMessages(User user);
	Tweed postMessage(String name, String content, User sender, Channel channel);
	
}
