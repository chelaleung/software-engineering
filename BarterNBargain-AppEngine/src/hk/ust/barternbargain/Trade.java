package hk.ust.barternbargain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Trade {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long itemId;
	
	private String sellerId;
	
	private String buyerId;
	
	private String status;
	
	private Date postingTime;
	
	public Trade(Long itemId, String sellerId, String buyerId) {
		super();
		this.itemId = itemId;
		this.sellerId = sellerId;
		this.buyerId = buyerId;
		this.status = "Pending";
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
	
	public String getBuyerId() {
		return buyerId;
	}
	
	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getPostingTime() {
		return postingTime;
	}
	
	public void setPostingTime(Date postingTime) {
		this.postingTime = postingTime;
	}
	
}
