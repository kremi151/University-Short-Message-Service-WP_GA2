package lu.mkremer.webprogsga.beans;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.mindrot.jbcrypt.BCrypt;

import lu.mkremer.webprogsga.managers.EventManager;
import lu.mkremer.webprogsga.managers.UserManager;
import lu.mkremer.webprogsga.persistence.User;
import lu.mkremer.webprogsga.util.MessageHelper;

@SessionScoped
@ManagedBean(name="usession")
public class UserSession implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6591517985681627665L;
	
	@EJB private UserManager um;
	@EJB private EventManager eventManager;
	
	private long listenerId;
	
	private User user;
	
	private String username;
	private String password;
	
	@PostConstruct
	public void init() {
		listenerId = eventManager.addAccountUpdatedListener(account -> {
			synchronized(UserSession.this) {
				if(user.getUsername().equals(account.getUsername())) {
					if(!account.isEnabled()) {
						user = null;
					}else {
						user = um.findUser(account.getUsername());
					}
				}
			}
		});
	}
	
	@PreDestroy
	public void preDestroy() {
		eventManager.removeAccountUpdatedListener(listenerId);
	}

	public synchronized String login() {
		User user = um.findUser(username);

		if(user != null) {
			if(BCrypt.checkpw(password, user.getPassword())) {
				username = null;
				password = null;
				if(user.isEnabled()) {
					this.user = user;
				}else {
					MessageHelper.throwDangerMessage("Your account has been disabled. Please contact a responsible staff member if you think this is an error.");
				}
				return "index";
			}else {
				MessageHelper.throwDangerMessage("Invalid username or password");
			}
		}else {
			MessageHelper.throwDangerMessage("Invalid username or password");
		}
		password = null;
		return "login";
	}
	
	public synchronized void logout() throws IOException {
		System.out.println("Logging out " + getDisplayName());
		user = null;
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.invalidateSession();
	    ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
	}
	
	public synchronized User getUser() {
		return user;
	}
	
	public synchronized String getUsername() {
		return username;
	}
	
	public synchronized String getPassword() {
		return password;
	}
	
	public synchronized void setUsername(String username) {
		this.username = username;
	}
	
	public synchronized void setPassword(String password) {
		this.password = password;
	}
	
	public synchronized String getDisplayName() {
		return user != null ? user.getFirstName() + " " + user.getLastName() : "MissingNo.";
	}
	
	public synchronized boolean isLoggedIn() {
		return user != null;
	}
	
	public synchronized boolean isElevated() {
		return user != null && user.isPriviledged();
	}
	
}
