package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lu.mkremer.webprogsga.managers.ChannelManager;
import lu.mkremer.webprogsga.managers.MessageManager;
import lu.mkremer.webprogsga.persistence.Channel;

@ManagedBean(name="newtweed")
@ViewScoped
public class ComposeMessageController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1540326122417015573L;
	
	@NotNull(message="No title provided")
	@Size(min=2, message="Title must have at least {min} characters")
	@Pattern(regexp="^[a-zA-Z0-9\\-\\_\\s]+$", message="Tweed name must only contain letters, numbers, hiphens and/or underscores. Whitespaces will be converted to underscores.")
	private String title;
	
	@NotNull(message="No message provided")
	@Size(min=2, message="Message must have at least {min} characters")
	private String message;
	
	@NotNull(message="No channel provided")
	private Channel channel;
	
	@ManagedProperty("#{usession}")
	private UserSession session;

	@EJB private ChannelManager cm;
	@EJB private MessageManager mm;
	
	public void setSession(UserSession session) {
		this.session = session;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public List<Channel> getChannels(){
		if(session.isLoggedIn()) {
			return cm.getChannelSubscriptions(session.getUser());
		}else {
			return Collections.emptyList();
		}
	}
	
	public String post() {
		if(session.isLoggedIn()) {
			title = title.replace(' ', '_');
			mm.postMessage("#"+title, message, session.getUser(), channel);
		}
		return "index.xhtml";
	}
}
