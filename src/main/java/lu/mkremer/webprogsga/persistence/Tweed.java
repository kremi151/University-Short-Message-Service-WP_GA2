package lu.mkremer.webprogsga.persistence;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Tweed {

	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false)
	private String name;//TODO: Must start with '#'
	
	@Column(nullable=false, length=200)
	private String content;//TODO: Max length -> 200
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date = Calendar.getInstance().getTime();
	
	@ManyToOne(optional=false)
	private User sender;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public long getId() {
		return id;
	}
}
