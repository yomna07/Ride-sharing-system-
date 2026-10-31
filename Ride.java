

public abstract class Ride implements IRide{

	protected String pickUpLocation;
	protected String dropOffLocation;
	protected final IDateTime pickUpTime;
	protected final IDateTime dropOffTime;
	protected IDriver driver;
	protected final int RideID;
	
	public Ride (String pickL , IDateTime pickup , String dropL , IDateTime dropoff , IDriver d , int id) {
		pickUpLocation = pickL;
		dropOffLocation = dropL;
		pickUpTime = pickup;
		dropOffTime = dropoff;
		driver = d;
		RideID = id;
		
	}
	
	public void setPickupLocation(String pickupLocation) {
		pickUpLocation = pickupLocation;
		}
	
	public String getPickupLocation() {
		return pickUpLocation;
		}
	
	public void setDropoffLocation(String dropoffLocation) {
		dropOffLocation = dropoffLocation;
		}
		
	public String getDropoffLocation() {
		return dropOffLocation;
		}
	
	public IDateTime getPickupTime() {
		return pickUpTime;
		}

	public IDateTime getDropoffTime() {
		return dropOffTime;
		}
	
	public int getRideId() {
		return RideID;
	}

	public IDriver getDriver() {
		return driver;
		}

	public void setDriver(IDriver driver) {
		this.driver = driver;	
		}

	public abstract boolean hasRider(int riderId);

	public String toString() {
		return "Ride ID: " + RideID + "\nPickup Location: " + pickUpLocation + "\nDropoff Location: " + dropOffLocation + "\nPickup Time: " + pickUpTime.format() + "\nDropoff Time: " + dropOffTime.format() + "\nRide Driver: " + driver.getName()  + "\nDriver ID: " + driver.getId() + "\n" ;
	}

	
	public int compareTo(IRide other) { //comparing rides alphabetically by pickup location
		
		return pickUpLocation.compareTo(other.getPickupLocation());
		
	}
		
		
	}
