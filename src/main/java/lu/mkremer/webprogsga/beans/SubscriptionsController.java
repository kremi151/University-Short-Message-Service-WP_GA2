package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.NotNull;

import lu.mkremer.webprogsga.managers.ChannelManager;
import lu.mkremer.webprogsga.persistence.Channel;
import lu.mkremer.webprogsga.util.MessageHelper;

@ManagedBean(name="subs")
@ViewScoped
public class SubscriptionsController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6679225513907067209L;
	
	@NotNull(message="No channel provided for unsubscription")
	private long unsubscribeChannelId;
	
	@EJB private ChannelManager cm;
	
	@ManagedProperty("#{usession}")
	private UserSession session;
	
	public void setSession(UserSession session) {
		this.session = session;
	}
	
	public long getUnsubscribeChannelId() {
		return unsubscribeChannelId;
	}

	public void setUnsubscribeChannelId(long unsubscribeChannelId) {
		this.unsubscribeChannelId = unsubscribeChannelId;
	}

	public List<Channel> getSubscriptions(){
		try {
			if(session.isLoggedIn()) {
				return cm.getChannelSubscriptions(session.getUser());
			}else {
				return Collections.emptyList();
			}
		}catch(Throwable t) {
			t.printStackTrace();
			MessageHelper.throwDangerMessage("An internal error occured");
			return Collections.emptyList();
		}
	}
	
	public void unsubscribe() {
		try {
			if(session.isLoggedIn()) {
				Channel channel = cm.findChannel(unsubscribeChannelId);
				if(channel != null) {
					cm.unsubscribe(session.getUser(), channel);
					MessageHelper.throwInfoMessage("You have successfully unsubscribed from the channel " + channel.getName());
				}else {
					MessageHelper.throwWarningMessage("The specified channel was not found");
				}
				unsubscribeChannelId = 0;
			}
		}catch(Throwable t) {
			t.printStackTrace();
			MessageHelper.throwDangerMessage("An internal error occured");
		}
	}

}
