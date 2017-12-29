package lu.mkremer.webprogsga.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2084198050106908213L;
	
	@Id
	private String username;
	
	@Column(nullable=false)
	private String firstName;
	
	@Column(nullable=false)
	private String lastName;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false)
	private String email;
	
	@Column(nullable=false)
	private boolean priviledged = false;
	
	@Column(nullable=false)
	private boolean enabled = true;//TODO: Enable/Disable via UI
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
		      name="ChannelSubscribers",
		      joinColumns=@JoinColumn(name="user", referencedColumnName="username"),
		      inverseJoinColumns=@JoinColumn(name="channel", referencedColumnName="id"))
	private List<Channel> subscriptions;
	
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="lecturer")
	private List<Class> lecturingClasses;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="creator")
	private List<Channel> createdChannels;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="sender")
	private List<Tweed> sentTweeds;
	
	
	public User() {}
	
	public User(String username, String email, String firstName, String lastName, String passwordHash, boolean priviledged) {
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.priviledged = priviledged;
		this.password = passwordHash;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public boolean isPriviledged() {
		return priviledged;
	}

	public void setPriviledged(boolean priviledged) {
		this.priviledged = priviledged;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<Channel> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<Channel> subscriptions) {
		this.subscriptions = subscriptions;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean equals(Object other) {
		if(other == null || other.getClass() != User.class) {
			return false;
		}else {
			return other == this || ((User)other).username.equals(this.username);
		}
	}

	@Override
	public int hashCode() {
		return username.hashCode();
	}
}
