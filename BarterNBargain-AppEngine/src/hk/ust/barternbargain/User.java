package hk.ust.barternbargain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	private String id;
	
	private String description;
	
	private Date registrationTime;

	public User(String id) {
		super();
		this.id = id;
		this.description = "";
		this.registrationTime = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(Date registrationTime) {
		this.registrationTime = registrationTime;
	}
	
}
