package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import lu.mkremer.webprogsga.managers.ChannelManager;
import lu.mkremer.webprogsga.persistence.Channel;
import lu.mkremer.webprogsga.util.MessageHelper;

@ManagedBean(name="avchannels")
@ViewScoped
public class ChannelsController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8098291993060343223L;
	
	@EJB private ChannelManager cm;
	
	@ManagedProperty("#{usession}")
	private UserSession session;

	public void setSession(UserSession session) {
		this.session = session;
	}
	
	public List<Channel> getChannels(){
		return session.isLoggedIn() ? cm.getInverseChannelSubscriptions(session.getUser()) : Collections.emptyList();
	}
	
	public void subscribe(long channelId) {
		if(session.isLoggedIn()) {
			Channel channel = cm.findChannel(channelId);
			if(channel != null) {
				cm.subscribe(session.getUser(), channel);
				MessageHelper.throwInfoMessage("You have subscribed the channel " + channel.getName());
			}else {
				MessageHelper.throwWarningMessage("The requested channel has not been found");
			}
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public String generateClassList(Channel channel) {
		if(channel.getClasses().size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(channel.getClasses().get(0).getTitle());
			for(int i = 1 ; i < channel.getClasses().size() ; i++) {
				sb.append(", ");
				sb.append(channel.getClasses().get(i).getTitle());
			}
			return sb.toString();
		}
		return "None. (Impossible)";
	}

}
