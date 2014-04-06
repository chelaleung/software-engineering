package hk.ust.barternbargain;

public class Item {
	

	
	public Item(int pictureID, String name, int price, int views,
			String condition, String transaction_method) {
		super();
		this.pictureID = pictureID;
		this.name = name;
		this.price = price;
		this.views = views;
		this.condition = condition;
		this.transaction_method = transaction_method;
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
	public String getCondition() {
		return condition;
	}
	public String getTransaction_method() {
		return transaction_method;
	}
	
	private int pictureID;
	private String name;
	private int price;
	private int views;
	private String condition;
	private String transaction_method;

}
