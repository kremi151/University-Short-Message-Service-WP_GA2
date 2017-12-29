package lu.mkremer.webprogsga.beans;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.mindrot.jbcrypt.BCrypt;

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
	
	private User user;
	
	private String username;
	private String password;

	public String login() {
		User user = um.findUser(username);

		if(user != null) {
			if(BCrypt.checkpw(password, user.getPassword())) {
				username = null;
				password = null;
				this.user = user;
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
	
	public void logout() throws IOException {
		System.out.println("Logging out " + getDisplayName());
		user = null;
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.invalidateSession();
	    ec.redirect(ec.getRequestContextPath() + "/index.xhtml");
	}
	
	public User getUser() {
		return user;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getNotificationCount() {
		return 0; //TODO: Adjust notification count?
	}
	
	public String getDisplayName() {
		return user != null ? user.getFirstName() + " " + user.getLastName() : "MissingNo.";
	}
	
	public boolean isLoggedIn() {
		return user != null;
	}
	
	public boolean isElevated() {
		return user != null && user.isPriviledged();
	}
	
}
