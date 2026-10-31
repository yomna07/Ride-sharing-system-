

public class Driver extends Person implements IDriver{
	
	private String vehiclePlate;
	private VehicleType vehicleType;
	

	public Driver( int id, String n , String pn , String vp, VehicleType vt) {
		super( id, n, pn );
		setVehiclePlate(vp);
		this.vehicleType = vt;
		
		
	}

	public String getVehiclePlate() {
		return vehiclePlate;
	}


	public void setVehiclePlate(String vehiclePlate) {
		
		if (vehiclePlate == null || vehiclePlate.length() != 7) //message to user if plate length is not exactly 7
			throw new IllegalArgumentException ("Vehicle plate should be 7 charachters.\n");
		
		for (int i = 0; i < 3 ; i ++) {
			if (!Character.isUpperCase(vehiclePlate.charAt(i)))  //checking if the first 3 characters are uppercase letters
				throw new IllegalArgumentException ("First 3 characters must be upper case letters.\n");
		}
		
		for (int i = 3 ; i < 7 ; i++) {
			if (!Character.isDigit(vehiclePlate.charAt(i))) //checking if the last 4 characters are numbers
				throw new IllegalArgumentException("Last 4 characters must be numbers.\n");
		}
					
		
		this.vehiclePlate = vehiclePlate;
	}


	public VehicleType getVehicleType() {
		return vehicleType;
	}


	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
		
	}

	public int compareTo(IDriver other) { //compare drivers by their id
		
		if (ID == other.getId())
			return 0;
		
		return ID > other.getId()? 1 : -1;
	
	}
	
	public String toString() {
		
		return super.toString() + "\nVehicle Plate: " + vehiclePlate + "\nVehicle Type: " + vehicleType;
	}


}
