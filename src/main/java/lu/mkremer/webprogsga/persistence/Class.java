package lu.mkremer.webprogsga.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
}
