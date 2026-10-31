

public class PrivateRide extends Ride implements IPrivateRide{
	
	private IRider rider; // one rider only
	
	public PrivateRide (String pickL ,  IDateTime pickup , String dropL , IDateTime dropoff , IDriver d , int id, IRider r) {
		super (pickL ,  pickup , dropL , dropoff , d , id);
		rider = r;
	}


	public IRider getRider() {
		return rider;
	}

	
	public void setRider(IRider rider) {
		this.rider = rider;
	}

	
	public boolean hasRider(int riderId) {
		
		return rider != null && rider.getId() == riderId ;
	}
	
	public String toString() {
		return "[Private Ride]\n" + super.toString() + "\nRider: " + rider.getName() +"\nRider ID: "+ rider.getId() +"\n";
	}

}
