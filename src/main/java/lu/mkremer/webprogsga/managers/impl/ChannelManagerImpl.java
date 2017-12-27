package lu.mkremer.webprogsga.managers.impl;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lu.mkremer.webprogsga.managers.ChannelManager;
import lu.mkremer.webprogsga.managers.UserManager;
import lu.mkremer.webprogsga.persistence.Channel;
import lu.mkremer.webprogsga.persistence.User;

@Singleton
public class ChannelManagerImpl implements ChannelManager{

	@PersistenceContext
	private EntityManager em;
	
	@EJB private UserManager um;

	@Override
	public List<Channel> getAvailableChannels() {
		return em.createQuery("select c from Channel c", Channel.class).getResultList();
	}

	@Override
	public void subscribe(User user, Channel channel) {
		List<Channel> subs = user.getSubscriptions();
		if(subs == null) {
			subs = getChannelSubscriptions(user);
			user.setSubscriptions(subs);
		}
		subs.add(channel);
		em.merge(user);
	}

	@Override
	public void unsubscribe(User user, Channel channel) {
		List<Channel> subs = user.getSubscriptions();
		if(subs == null) {
			subs = getChannelSubscriptions(user);
			user.setSubscriptions(subs);
		}
		Iterator<Channel> it = subs.iterator();
		while(it.hasNext()) {
			if(it.next().getId() == channel.getId()) {
				it.remove();
			}
		}
		em.merge(user);
	}

	@Override
	public Channel createChannel(String name, User creator, Consumer<Channel> prePersistCallback) {
		Channel channel = new Channel(name, creator);
		if(prePersistCallback != null) {
			prePersistCallback.accept(channel);
		}
		em.persist(channel);
		return channel;
	}

	@Override
	public List<Channel> getChannelSubscriptions(User user) {
		return em.createQuery("select sub from User u join u.subscriptions sub where u.username = :id", Channel.class)
				.setParameter("id", user.getUsername()).getResultList();
	}
}
