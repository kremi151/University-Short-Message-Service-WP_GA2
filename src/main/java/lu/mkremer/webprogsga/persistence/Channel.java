package lu.mkremer.webprogsga.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Channel{

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false)
	private String name;
	
	@ManyToOne(optional=false)
	private User creator;
	
	@ManyToMany(fetch=FetchType.EAGER)//Don't touch!!
	private List<Class> classes = new ArrayList<>();
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = Calendar.getInstance().getTime();
	

	@ManyToMany(fetch=FetchType.LAZY, mappedBy="subscriptions")
	private List<User> subscribers;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="channel")
	private List<Tweed> messages;
	
	public Channel() {}
	
	public Channel(String name, User creator) {
		this.name = name;
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public long getId() {
		return id;
	}

	public List<Class> getClasses() {
		return classes;
	}
	
}
