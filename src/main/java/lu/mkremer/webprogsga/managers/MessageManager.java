package lu.mkremer.webprogsga.managers;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import lu.mkremer.webprogsga.persistence.Channel;
import lu.mkremer.webprogsga.persistence.Tweed;
import lu.mkremer.webprogsga.persistence.User;

@Local
public interface MessageManager {

	List<Tweed> loadMessagesFor(User user);
	List<Tweed> loadMessagesOf(User user);
	List<Tweed> loadMessagesFrom(Channel channel);
	Tweed postMessage(String name, String content, User sender, Channel channel, Date postingDate);
	Tweed findMessage(String id);
	List<Tweed> findAnswersFor(String id);
	
	default Tweed postMessage(String name, String content, User sender, Channel channel) {
		return postMessage(name, content, sender, channel, null);
	}
}
