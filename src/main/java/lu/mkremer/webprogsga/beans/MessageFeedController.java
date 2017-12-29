package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lu.mkremer.webprogsga.managers.ChannelManager;
import lu.mkremer.webprogsga.managers.MessageManager;
import lu.mkremer.webprogsga.persistence.Channel;
import lu.mkremer.webprogsga.persistence.Tweed;
import lu.mkremer.webprogsga.util.MessageHelper;

@ManagedBean(name="msgfeed")
@ViewScoped
public class MessageFeedController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3499568050887970561L;
	
	private List<Tweed> messages;
	private String channelName;
	
	@EJB private MessageManager mm;
	@EJB private ChannelManager cm;

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
		if(session.isLoggedIn()) {
			String channelFilter = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("channel");
			if(channelFilter == null) {
				messages = mm.loadMessagesFor(session.getUser());
			}else {
				Channel channel = null;
				try {
					channel = cm.findChannel(Long.parseLong(channelFilter));
					messages = mm.loadMessagesFrom(channel);
					channelName = channel.getName();
				}catch(NumberFormatException e) {}
				if(channel == null) {
					MessageHelper.throwWarningMessage("The requested channel has not been found");
				}
			}
		}
	}

	public List<Tweed> getMessages() {
		return messages;
	}

	public String getChannelName() {
		return channelName;
	}
	
}
