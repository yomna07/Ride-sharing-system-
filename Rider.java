

public class Rider extends Person implements IRider {

	private String email;
	private String city;
	
	public Rider (int id , String n, String pn , String e, String c) {
		super (id, n , pn);
		email = e;
		city = c;
	}
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHomeCity() {
		return city;
	}

	public void setHomeCity(String homeCity) {
		city = homeCity;
		
	}

	public int compareTo(IRider other) {
		
		if (ID == other.getId())
			return 0;
		
		return ID > other.getId()? 1 : -1;
			
	}
	
	public String toString() {
		return super.toString() + "\nRider Email: " + email + "\nRider Home City: " + city; 
	}

}
