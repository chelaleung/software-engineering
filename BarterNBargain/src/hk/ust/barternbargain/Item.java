package hk.ust.barternbargain;

public class Item {
	

	
	public Item(int pictureID, String name, int price, int views,
			String condition, int transaction_method, Long key) {
		super();
		this.pictureID = pictureID;
		this.name = name;
		this.price = price;
		this.views = views;
		this.condition = condition;
		this.transaction_method = transaction_method;
		this.key = key;
	}
	public int getPictureID() {
		return pictureID;
	}
	public String getName() {
		return name;
	}
	public int getPrice() {
		return price;
	}
	public int getViews() {
		return views;
	}
	
	public void incrementViews(){
		views += 1;
	}
	
	public String getCondition() {
		return condition;
	}
	public int getTransaction_method() {
		return transaction_method;
	}
	public Long getKey() {
		return key;
	}
	
	private int pictureID;
	private String name;
	private int price;
	private int views;
	private String condition;
	private int transaction_method;
	private Long key;

}
