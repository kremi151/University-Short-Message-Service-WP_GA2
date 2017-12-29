package lu.mkremer.webprogsga.managers;

import java.util.List;
import java.util.function.Consumer;

import javax.ejb.Local;

import lu.mkremer.webprogsga.persistence.Channel;
import lu.mkremer.webprogsga.persistence.User;

@Local
public interface ChannelManager {

	List<Channel> getAvailableChannels();
	void subscribe(User user, Channel channel);
	void unsubscribe(User user, Channel channel);
	Channel createChannel(String name, User creator, Consumer<Channel> prePersistCallback);
	List<Channel> getChannelSubscriptions(User user);
	Channel findChannel(long id);
	
	default Channel createChannel(String name, User creator) {
		return createChannel(name, creator, null);
	}
	
}
