package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import lu.mkremer.webprogsga.managers.MessageManager;
import lu.mkremer.webprogsga.persistence.Tweed;

@ManagedBean(name="msgfeed")
@ViewScoped
public class MessageFeedController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3499568050887970561L;
	
	private List<Tweed> messages = new LinkedList<>();
	
	@EJB private MessageManager mm;

	@ManagedProperty("#{usession}")
	private UserSession session;
	
	public void setSession(UserSession session) {
		this.session = session;
	}

	@PostConstruct
	public void init() {
		loadFeed();
	}
	
	public void loadFeed() {
		messages.clear();
		if(session.isLoggedIn()) {
			messages.addAll(mm.loadMessagesFor(session.getUser()));
		}
	}

	public List<Tweed> getMessages() {
		return messages;
	}
	
}
