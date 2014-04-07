package hk.ust.barternbargain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long itemId;
	
	private String userId;

	private String message;
	
	private Date postingTime;

	public Comment(Long itemId, String userId, String message) {
		super();
		this.itemId = itemId;
		this.userId = userId;
		this.message = message;
		this.postingTime = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public Date getPostingTime() {
		return postingTime;
	}

	public void setPostingTime(Date postingTime) {
		this.postingTime = postingTime;
	}
	
}
