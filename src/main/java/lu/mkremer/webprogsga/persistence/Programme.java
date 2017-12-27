package lu.mkremer.webprogsga.persistence;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Programme {//TODO: Complete
	
	@Id
	@GeneratedValue
	private long id;
	
	@Column(nullable=false)
	private String name;

	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="programme")
	private List<Class> classes;
	
	public Programme() {}
	
	public Programme(String name) {
		this.name = name;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}
	
}
