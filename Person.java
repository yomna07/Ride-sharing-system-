

public abstract class Person implements IPerson {
	
	protected String name;
	protected final int ID; // id set only once at creation
	protected String PhoneNumber;
	protected LinkedList<IRide> rideHistory;
	
	
	public  Person ( int id , String n , String pn) {
		name = n;
		ID = id;
		setPhoneNumber(pn); //phone number format validation
		rideHistory = new LinkedList<IRide>();
		
		}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public int getId() {
		return  ID;
	}


	public void setPhoneNumber(String phoneNumber) {
		
		if ( phoneNumber == null || phoneNumber.length() != 10 ) // message to user if phone number is empty or not 10 characters
			throw new IllegalArgumentException ("Phone number must be 10 digits.");
			
		for (int i = 0; i < phoneNumber.length(); i++) {
			if ( ! Character.isDigit(phoneNumber.charAt(i)) ) // phone number characters should be all digits
				throw new IllegalArgumentException("Phone number must contain numbers only.");
			}
		PhoneNumber = phoneNumber;
		
	}
	
	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public LinkedList<IRide> getRideHistory() {
		return rideHistory;
	}
	
	public String toString() {
		return "Name: " + name + "\nID: " + ID + "\nPhone Number: " + PhoneNumber + "\nRides: " + LinkedList.length(rideHistory) + "\n";
	}
	
	
}
