

public class SharedRide extends Ride implements ISharedRide{
	
	private LinkedList<IRider> riders = new LinkedList<IRider>(); // several riders
	
	
	public SharedRide (String pickL , IDateTime pickup , String dropL , IDateTime dropoff , IDriver d, int id) {
		super (pickL , pickup , dropL , dropoff , d , id);
		
	}

	public LinkedList<IRider> getParticipants() {
		return riders;
	}

	
	public boolean addParticipant(IRider rider) { //adds rider if id doesnt exist already
		if (rider == null || hasRider(rider.getId()))
			return false;
	
		riders.insert(rider);
		return true;
	}

	
	public boolean removeParticipantById(int riderId) {  //searches for a rider by id and removes if found
		
		if (riders.empty())
			return false;
			
		riders.findFirst();
		while ( !riders.last() ) {
			if (riders.retrieve().getId() == riderId) {
				riders.remove();
				return true; 
				}
			riders.findNext();
		}
		
		if (riders.retrieve().getId() == riderId) {
			riders.remove();
			return true;
			}
		return false;
	}

	public boolean isEmpty() {
		return riders.empty();
	}


	public boolean hasRider(int riderId) { //looks through the participants of the ride searching for a specific rider by id
		
		if ( riders.empty())
			return false;
		riders.findFirst();
		while (! riders.last() ) {
			if (riders.retrieve().getId() == riderId)
				return true;
			riders.findNext();
		}
		
		return riders.retrieve().getId() == riderId;
		
	}
	
	public String toString() {
		
		if (riders.empty())
			return "No riders\n";
		
		riders.findFirst();
		String str = super.toString();
		while (!riders.last()){
			str = str + riders.retrieve().toString() +"\n";
			riders.findNext(); 
		}
		str = str + riders.retrieve() +"\n";
			
		return str;
		
		
	}

}
