package lu.mkremer.webprogsga.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lu.mkremer.webprogsga.managers.UserManager;
import lu.mkremer.webprogsga.persistence.User;

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
	
	private User user;
	
	@NotNull(message="No first name supplied")
	@Size(min=2, max=32, message="First name must be between {min} and {max} characters long") 
	private String firstName;
	
	@NotNull(message="No last name supplied")
	@Size(min=2, max=32, message="Last name must be between {min} and {max} characters long") 
	private String lastName;
	
	@PostConstruct
	public void init() {
		if(session.isLoggedIn()) {
			String uid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			if(uid != null) {
				user = um.findUser(uid);
				firstName = user.getFirstName();
				lastName = user.getLastName();
			}
		}
	}
	
	public void setSession(UserSession session) {
		this.session = session;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public User getUser() {
		return user;
	}
	
	/*public void saveModifications() {
		if(user != null && session.canModifyUsers()) {
			UserGroup group = user.getGroup();
			if(user.getGroup().getId() != groupId) {
				group = um.getGroupById(groupId);
				if(group == null) {
					MessageHelper.throwWarningMessage("Requested user group has not been found");
				}
			}
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setGroup(group);
			um.update(user);
			MessageHelper.throwInfoMessage("User information have been updated");
		}else {
			MessageHelper.throwDangerMessage("You are not allowed to do this");
		}
	}*/

}
