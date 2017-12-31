package lu.mkremer.webprogsga.managers.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lu.mkremer.webprogsga.managers.MessageManager;
import lu.mkremer.webprogsga.persistence.Channel;
import lu.mkremer.webprogsga.persistence.Tweed;
import lu.mkremer.webprogsga.persistence.User;

@Stateless
public class MessageManagerImpl implements MessageManager{
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Tweed> loadMessagesFor(User user) {
		return em.createQuery("select msg from User u join u.subscriptions sub join sub.messages msg where u.username = :id order by msg.date desc", Tweed.class)
				.setParameter("id", user.getUsername()).getResultList();
	}

	@Override
	public Tweed postMessage(String name, String content, User sender, Channel channel) {
		Tweed msg = new Tweed(name, content, sender, channel);
		em.persist(msg);
		return msg;
	}

	@Override
	public List<Tweed> loadMessagesOf(User user) {//TODO: Restrict messages to the channels the viewer (other user) has subscribed?
		return em.createQuery("select m from Tweed m where m.sender.username = :sender order by m.date desc", Tweed.class)
				.setParameter("sender", user.getUsername()).getResultList();
	}

	@Override
	public List<Tweed> loadMessagesFrom(Channel channel) {
		return em.createQuery("select m from Tweed m where m.channel.id = :id order by m.date desc", Tweed.class).setParameter("id", channel.getId()).getResultList();
	}

	@Override
	public Tweed findMessage(String id) {
		if(!id.startsWith("#")) id = "#" + id;
		return em.find(Tweed.class, id);
	}

	@Override
	public List<Tweed> findAnswersFor(String id) {
		if(!id.startsWith("#")) id = "#" + id;
		return em.createQuery("select m from Tweed m where m.content like :name order by m.date asc", Tweed.class)
				.setParameter("name", "%" + id + "%").getResultList();//TODO: Make sure to look for subsequential a whitespace character or that the name stands at the end
	}

}
