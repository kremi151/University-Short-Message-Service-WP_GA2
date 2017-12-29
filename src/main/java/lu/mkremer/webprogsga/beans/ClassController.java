package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.webprogsga.managers.ChannelManager;
import lu.mkremer.webprogsga.managers.ClassManager;
import lu.mkremer.webprogsga.persistence.Channel;
import lu.mkremer.webprogsga.persistence.Class;
import lu.mkremer.webprogsga.persistence.Programme;
import lu.mkremer.webprogsga.util.MessageHelper;

@ManagedBean(name="viewclass")
@ViewScoped
public class ClassController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3557111055682521191L;
	
	@EJB private ClassManager clm;
	@EJB private ChannelManager cm;
	
	private Class clazz;
	
	@NotNull(message="No channel name provided")
	@Size(min=5, message="Channel name must have at least {min} characters")
	private String channelName;
	
	@ManagedProperty("#{usession}")
	private UserSession session;

	public void setSession(UserSession session) {
		this.session = session;
	}

	@PostConstruct
	public void init() {
		reload();
	}
	
	public void reload() {
		if(session.isLoggedIn()) {
			String cid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			if(cid != null) {
				try {
					clazz = clm.findClass(Long.parseLong(cid), true);
				}catch(NumberFormatException e) {
					MessageHelper.throwWarningMessage("The requested class has not been found");
				}
			}
		}else {
			clazz = null;
		}
	}
	
	public String getClassName() {
		return clazz.getTitle();
	}
	
	public Programme getProgramme() {
		return clazz.getProgramme();
	}
	
	public List<Channel> getChannels(){
		return clazz.getChannels();
	}
	
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public void create() {
		if(clazz != null && session.isElevated()) {
			cm.createChannel(channelName, session.getUser(), channel -> channel.getClasses().add(clazz));
			reload();
		}
	}
	
}
