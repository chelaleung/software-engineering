package hk.ust.barternbargain;


public class User {
	
	private String username;
	private String phone_number;
	private String email;
	private int pictureID;
	
	
	public User(String username, String phone_number, String email, int pictureID){
	this.username = username;
	this.phone_number = phone_number;
	this.email = email;
	this.pictureID = pictureID;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public void setPictureID(int pictureID){
		this.pictureID = pictureID;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public int getPictureID(){
		return pictureID;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public String getEmail(){
		return email;
	}

}
