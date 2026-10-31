

import java.util.*;

public class Main {

	static Scanner input = new Scanner(System.in);

	public static void main(String args[]) {

		RideSharingSystem rideSharing = new RideSharingSystem();

		// reading files

		rideSharing.loadRidersFromCSV("riders_100.csv");
		rideSharing.loadDriversFromCSV("drivers_30.csv");
		rideSharing.loadRidesFromCSV("rides_40.csv");

		while (true) {
			System.out.println("* * * * Main Menu * * * *");

			System.out.println("1) List all riders.");
			System.out.println("2) Search for all riders by home city.");
			System.out.println("3) Search for riders by name.");
			System.out.println("4) Add a rider.");
			System.out.println("5) List all drivers.");
			System.out.println("6) Search for drivers by vehicle type.");
			System.out.println("7) Add a driver.");
			System.out.println("8)List all rides alphabatically by pick up location");
			System.out.println("9) Search for rides by pickup location.");
			System.out.println("10) Search for rides by rider name.");
			System.out.println("11) List the participants of a shared rides by pickup location.");
			System.out.println("12) Add a ride.");
			System.out.println("0) Exit the program.");

			System.out.println("Enter choice");
			int choice = input.nextInt();
			input.nextLine();

			switch (choice) {

			case 0:
				System.out.println("Exiting program.");
				return;

			case 1:
				printList(rideSharing.getAllRiders());
				break;

			///////////////////////////////////////////////

			case 2:
				System.out.println("Enter rider home city to search");
				String city = input.nextLine();
				printList(rideSharing.searchRidersByHomeCity(city));
				break;

			///////////////////////////////////////////

			case 3:
				System.out.println("Enter rider name to search");
				String cityName = input.nextLine();
				;
				printList(rideSharing.searchRidersByName(cityName));
				break;

			//////////////////////////////////////////

			case 4:

				System.out.println("Enter rider name: ");
				String riderName = input.nextLine();

				System.out.println("Enter rider ID: ");
				int riderID = input.nextInt();
				input.nextLine();

				System.out.println("Enter rider phone number: ");
				String riderPhone = input.nextLine();

				System.out.println("Enter rider email: ");
				String riderEmail = input.nextLine();

				System.out.println("Enter rider home city: ");
				String riderCity = input.nextLine();

				try {
					Rider rider = new Rider(riderID, riderName, riderPhone, riderEmail, riderCity);
					if (rideSharing.addRider(rider))
						System.out.println("Rider added successfully");
					else
						System.out.println("Rider not added");

				} catch (IllegalArgumentException e) {
					System.out.println (e.getMessage());

				}

				break;

			////////////////////////////////////////

			case 5:
				printList(rideSharing.getAllDrivers());
				break;

			/////////////////////////////////////////

			case 6:
				System.out.println("Enter driver vehicle type to search");

				try {
					VehicleType vType = VehicleType.valueOf(input.nextLine());
					printList(rideSharing.searchDriversByVehicleType(vType));

				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}

				break;

			////////////////////////////////////////////

			case 7:
				System.out.println("Enter driver name: ");
				String driverName = input.nextLine();

				System.out.println("Enter driver ID: ");
				int driverID = input.nextInt();
				input.nextLine();

				System.out.println("Enter driver phone number: ");
				String driverPhone = input.nextLine();

				System.out.println("Enter driver vehicle plate: ");
				String driverPlate = input.nextLine();

				
				try {
					System.out.println("Enter driver vehicle type: SEDAN, LUXURY_SEDAN, SUV, VAN) ");
					VehicleType vType = VehicleType.valueOf( input.nextLine());
					Driver driver = new Driver(driverID, driverName, driverPhone, driverPlate, vType);
					if (rideSharing.addDriver(driver))
						System.out.println("Driver added successfully");

					else
						System.out.println("Driver not added");

				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}

				break;

			///////////////////////////////////////////

			case 8:
				printList(rideSharing.getAllRidesAlphabetically());
				break;

			////////////////////////////////////////////

			case 9:
				System.out.println("Enter pickup location to search");
				String pickup = input.nextLine();
				printList(rideSharing.searchRidesByPickupLocation(pickup));
				break;

			//////////////////////////////////////////////

			case 10:
				System.out.println("Enter rider name to search");
				String riderNameSearch = input.nextLine();
				printList(rideSharing.searchRidesByRiderName(riderNameSearch));
				break;

			///////////////////////////////////////////

			case 11:
				System.out.println("Enter pickup location to list participants: ");
				String pickupParticipants = input.nextLine();
				printList(rideSharing.getSharedRideParticipants(pickupParticipants));
				break;

			//////////////////////////////////////

			case 12:

				System.out.println("Enter pickup location: ");
				String pickupL = input.nextLine();

				System.out.println("Enter dropoff location: ");
				String dropoffL = input.nextLine();

				System.out.println("Enter pickup date and time: DD/MM/YYYY HH:MM"); // pick up date/time
				DateTime pickupDT = DateTime.parse(input.nextLine());
				

				System.out.println("Enter dropoff date and time: DD/MM/YYYY HH:MM" );  // drop off date/time
				DateTime dropoffDT = DateTime.parse(input.nextLine());


				System.out.println("Enter pickup driver ID: ");
				int rideDriverID = input.nextInt();

				System.out.println("a) Private ride , b) Shared ride:");
				char rideType = input.next().charAt(0);

				if (rideType == 'a') {

					System.out.println("Enter rider id:");
					int rideRiderID = input.nextInt();

					if (rideSharing.schedulePrivateRide(pickupL, pickupDT, dropoffDT, dropoffL, rideRiderID, rideDriverID))
						System.out.println("Private ride is scheduled successfully");
					else 
						System.out.println("Private ride was not scheduled, please try again");
				}

				if (rideType == 'b') {

					System.out.println("How many riders? (at least 2): ");
					int count = input.nextInt();

					if (count < 2) {
						System.out.println("Shared ride needs more than one rider.");
						break;
					}

					int[] riderIds = new int[count];
					for (int i = 0; i < count; i++) {
						System.out.println("Enter rider ID: ");
						riderIds[i] = input.nextInt();

					}

					if (rideSharing.scheduleSharedRide(pickupL, pickupDT, dropoffDT, dropoffL, riderIds, rideDriverID))
						System.out.println("Shared ride is scheduled successfully");
					else 
						System.out.println("Shared ride was not scheduled, please try again");

				}
			}
		}
	}

	public static <T> void printList(LinkedList<T> L) {

		if (L == null || L.empty()) {
			System.out.println("No records found.");
			return;
		}
		L.findFirst();
		while (!L.last()) {
			System.out.println(L.retrieve());
			L.findNext();
		}
		System.out.println(L.retrieve());

	}

}
