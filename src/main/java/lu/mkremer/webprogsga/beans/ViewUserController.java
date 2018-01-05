package lu.mkremer.webprogsga.beans;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import lu.mkremer.webprogsga.managers.MessageManager;
import lu.mkremer.webprogsga.managers.UserManager;
import lu.mkremer.webprogsga.persistence.Tweed;
import lu.mkremer.webprogsga.persistence.User;
import lu.mkremer.webprogsga.util.MessageHelper;

@ManagedBean(name="vuser")
@ViewScoped
public class ViewUserController implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5800770152969247617L;

	@ManagedProperty("#{usession}")
	private UserSession session;
	
	@EJB private UserManager um;
	@EJB private MessageManager mm;
	
	private User user;
	
	private List<Tweed> messages = new LinkedList<>();
	
	@PostConstruct
	public void init() {
		if(session.isLoggedIn()) {
			String uid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			if(uid != null) {
				user = um.findUser(uid);
				if(user != null) {
					messages.clear();
					messages.addAll(mm.loadMessagesOf(user));
				}
			}
		}
	}
	
	public boolean isExistent() {
		return user != null;
	}
	
	public void setSession(UserSession session) {
		this.session = session;
	}

	public User getUser() {
		return user;
	}

	public List<Tweed> getMessages() {
		return messages;
	}
	
	public void enableAccount() {
		if(user != null && session.isElevated()) {
			user.setEnabled(true);
			um.update(user);
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}
	
	public void disableAccount() {
		if(user != null && session.isElevated()) {
			user.setEnabled(false);
			um.update(user);
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}

}
