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
	
	@Column(nullable=false)
	private String description;
	
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
	
	public Channel(String name, String description, User creator) {
		this.name = name;
		this.description = description;
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	@Override
	public boolean equals(Object other) {
		if(other == this) {
			return true;
		}else if(other == null || other.getClass() != Channel.class) {
			return false;
		}else {
			return ((Channel)other).id == this.id;
		}
	}
	
	@Override
	public int hashCode() {
		return (int)id;
	}
	
}
