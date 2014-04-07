package hk.ust.barternbargain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String userId;
	
	private Date postingTime;

	private String name;

	private String category;
	
	private String description;
	
	private Collection<String> imageUrl;

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getPostingTime() {
		return postingTime;
	}

	public void setPostingTime(Date postingTime) {
		this.postingTime = postingTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public Collection<String> getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(Collection<String> imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
