package hk.ust.barternbargain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Session {
	
	@Id
    private String id;
	
    private String userId;
    
    private Date creationTime;

	public Session(String username) {
		this.id = UUID.randomUUID().toString();
		this.userId = username;
		this.creationTime = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
    	this.userId = userId;
    }

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

}