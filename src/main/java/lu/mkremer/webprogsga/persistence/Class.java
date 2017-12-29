package lu.mkremer.webprogsga.persistence;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Class {
	
	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private String title;

	@ManyToOne(optional = false)
	private Programme programme;

	@ManyToOne(optional = false)
	private User lecturer;
	
	
	@ManyToMany(cascade=CascadeType.REMOVE, fetch=FetchType.LAZY, mappedBy="classes")
	private List<Channel> channels;
	
	public Class() {}
	
	public Class(String title, Programme programme, User lecturer) {
		this.title = title;
		this.programme = programme;
		this.lecturer = lecturer;
	}
	

	public long getId() {
		return id;
	}

	public Programme getProgramme() {
		return programme;
	}

	public void setProgramme(Programme programme) {
		this.programme = programme;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getLecturer() {
		return lecturer;
	}

	public void setLecturer(User lecturer) {
		this.lecturer = lecturer;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}else if(obj == null || obj.getClass() != Class.class) {
			return false;
		}else {
			return ((Class)obj).id == this.id;
		}
	}
	
	@Override
	public int hashCode() {
		return (int)(id * 31);
	}
}
