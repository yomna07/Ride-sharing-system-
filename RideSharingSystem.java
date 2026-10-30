package codes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RideSharingSystem implements IRideSharingSystem {
    private RiderList riders;
    private DriverList drivers;
    private RideList rides;
    private int nextRideId = 1;

    public RideSharingSystem() {
        this.riders = new RiderList();
        this.drivers = new DriverList();
        this.rides = new RideList();
    }

public boolean loadRidersFromCSV(String ridersFilePath) {
    try {
        Scanner file = new Scanner(new File(ridersFilePath));
        while (file.hasNextLine()) {
            String[] f = file.nextLine().split(",");
            try {
                addRider(new Rider(Integer.parseInt(f[0]), f[1], f[2], f[3], f[4]));
            } catch (Exception e) {
            }
        }
        file.close();
        return true;
    } catch (FileNotFoundException e) {
        return false;
    }
}

public boolean loadDriversFromCSV(String driversFilePath) {
    try {
        Scanner file = new Scanner(new File(driversFilePath));
        while (file.hasNextLine()) {
            String[] f = file.nextLine().split(",");
            try {
                VehicleType type = VehicleType.valueOf(f[4]);
                addDriver(new Driver(Integer.parseInt(f[0]), f[1], f[2], f[3], type));
            } catch (Exception e) {
            }
        }
        file.close();
        return true;
    } catch (FileNotFoundException e) {
        return false;
    }
}

public boolean loadRidesFromCSV(String ridesFilePath) {
    try {
        Scanner file = new Scanner(new File(ridesFilePath));
        while (file.hasNextLine()) {
            String[] f = file.nextLine().split(",");
            try {
                IDateTime pickupTime = DateTime.parse(f[2]);
                IDateTime dropoffTime = DateTime.parse(f[3]);
                int driverId = Integer.parseInt(f[5]);
                String[] ids = f[6].split(";");

                if (f[0].equals("PRIVATE")) {
                    int riderId = Integer.parseInt(ids[0]);
                    schedulePrivateRide(f[1], pickupTime, dropoffTime, f[4],
                                        riderId, driverId);
                } else {
                    int[] riderIds = new int[ids.length];
                    for (int i = 0; i < ids.length; i++)
                        riderIds[i] = Integer.parseInt(ids[i]);
                    scheduleSharedRide(f[1], pickupTime, dropoffTime, f[4],
                                       riderIds, driverId);
                }
            } catch (Exception e) {
            }
        }
        file.close();
        return true;
    } catch (FileNotFoundException e) {
        return false;
    }
}    public boolean addRider(IRider rider) {
        if (rider == null)
            return false;
        if (riders.findByEmail(rider.getEmail()) != null)
            return false;
        return riders.add(rider);
    }

    public boolean addDriver(IDriver driver) {
        if (driver == null)
            return false;
        if (drivers.findByVehiclePlate(driver.getVehiclePlate()) != null)
            return false;
        return drivers.add(driver);
    }

    public IRider searchRiderById(int riderId) {
        return riders.findById(riderId);
    }

    public IRider searchRiderByEmail(String email) {
        return riders.findByEmail(email);
    }

    public LinkedList<IRider> searchRidersByName(String fullName) {
        return riders.findByName(fullName);
    }

    public LinkedList<IRider> searchRidersByHomeCity(String homeCity) {
        return riders.findByHomeCity(homeCity);
    }

    public LinkedList<IRider> getAllRiders() {
        return riders.getAll();
    }

    public IDriver searchDriverById(int driverId) {
        return drivers.findById(driverId);
    }

    public IDriver searchDriverByVehiclePlate(String vehiclePlate) {
        return drivers.findByVehiclePlate(vehiclePlate);
    }

    public LinkedList<IDriver> searchDriversByVehicleType(VehicleType vehicleType) {
        return drivers.findByVehicleType(vehicleType);
    }

    public LinkedList<IDriver> getAllDrivers() {
        return drivers.getAll();
    }

    public LinkedList<IRide> searchRidesByPickupLocation(String pickupLocation) {
        return rides.findByPickupLocation(pickupLocation);
    }

    public LinkedList<IRide> searchRidesByRiderName(String riderName) {
        return rides.findByRiderName(riderName);
    }

    public LinkedList<IRide> getAllRidesAlphabetically() {
        return rides.getAllAlphabetically();
    }

    public LinkedList<IRider> getSharedRideParticipants(String pickupLocation) {
        LinkedList<IRider> collectedList = new LinkedList<IRider>();
        LinkedList<IRide> matchingRides = rides.findByPickupLocation(pickupLocation);
        if (matchingRides == null || matchingRides.empty())
            return collectedList;
            
        matchingRides.findFirst();
        while (true) {
            copyRiders(matchingRides.retrieve(), collectedList);
            if (matchingRides.last()) {
                break;
            }
            matchingRides.findNext();
        }
        return collectedList;
    }

    // schedule private ride with unique local naming
    public boolean schedulePrivateRide(String pickupLocation, IDateTime pickupTime,
                                       IDateTime dropoffTime, String dropoffLocation,
                                       int riderId, int driverId) {
        if (pickupTime == null || dropoffTime == null) return false;
        if (pickupTime.compareTo(dropoffTime) >= 0)
            return false;
            
        IRider targetRider = riders.findById(riderId);
        IDriver targetDriver = drivers.findById(driverId);
        
        if (targetRider == null || targetDriver == null)
            return false;
        if (hasConflict(targetRider, pickupTime, dropoffTime))
            return false;
        if (hasConflict(targetDriver, pickupTime, dropoffTime))
            return false;

        IRide createdRide = new PrivateRide(pickupLocation, pickupTime,  dropoffLocation, dropoffTime, targetDriver, nextRideId++ , targetRider);
        rides.addRide(createdRide);
        targetRider.getRideHistory().insert(createdRide);
        targetDriver.getRideHistory().insert(createdRide);
        return true;
    }

    // schedule shared ride with modified structure
    public boolean scheduleSharedRide(String pickupLocation, IDateTime pickupTime,
                                      IDateTime dropoffTime, String dropoffLocation,
                                      int[] riderIds, int driverId) {
        if (pickupTime == null || dropoffTime == null) return false;
        if (pickupTime.compareTo(dropoffTime) >= 0)
            return false;
        if (riderIds == null || riderIds.length < 2)
            return false;
            
        IDriver targetDriver = drivers.findById(driverId);
        if (targetDriver == null || hasConflict(targetDriver, pickupTime, dropoffTime))
            return false;
            
        IRider[] groupList = new IRider[riderIds.length];
        for (int idx = 0; idx < riderIds.length; idx++) {
            groupList[idx] = riders.findById(riderIds[idx]);
            if (groupList[idx] == null || hasConflict(groupList[idx], pickupTime, dropoffTime))
                return false;
            for (int subIdx = 0; subIdx < idx; subIdx++) {
                if (riderIds[subIdx] == riderIds[idx])
                    return false;
            }
        }

        SharedRide sharedRideObj = new SharedRide(pickupLocation, pickupTime, dropoffLocation, dropoffTime, targetDriver , nextRideId++);
        for (int idx = 0; idx < groupList.length; idx++) {
            sharedRideObj.addParticipant(groupList[idx]);
            groupList[idx].getRideHistory().insert(sharedRideObj);
        }
        rides.addRide(sharedRideObj);
        targetDriver.getRideHistory().insert(sharedRideObj);
        return true;
    }

    // remove rider and clean associated history
    public boolean removeRider(int riderId) {
        IRider targetRider = riders.findById(riderId);
        if (targetRider == null)
            return false;
            
        LinkedList<IRide> histList = targetRider.getRideHistory();
        while (histList != null && !histList.empty()) {
            histList.findFirst();
            IRide currentRide = histList.retrieve();
            if (currentRide instanceof ISharedRide) {
                ISharedRide sharedSeg = (ISharedRide) currentRide;
                sharedSeg.removeParticipantById(riderId);
                removeFromHistory(targetRider, currentRide);
                if (sharedSeg.isEmpty())
                    deleteRide(currentRide);
            } else {
                deleteRide(currentRide);
            }
        }
        return riders.removeById(riderId);
    }

    // remove driver and wipe their history
    public boolean removeDriver(int driverId) {
        IDriver targetDriver = drivers.findById(driverId);
        if (targetDriver == null)
            return false;
            
        LinkedList<IRide> histList = targetDriver.getRideHistory();
        while (histList != null && !histList.empty()) {
            histList.findFirst();
            deleteRide(histList.retrieve());
        }
        return drivers.removeById(driverId);
    }

    // check time collision
    private boolean hasConflict(IPerson personObj, IDateTime pickup, IDateTime dropoff) {
        if (personObj == null) return false;
        LinkedList<IRide> histList = personObj.getRideHistory();
        if (histList == null || histList.empty())
            return false;
            
        histList.findFirst();
        while (true) {
            if (overlaps(histList.retrieve(), pickup, dropoff))
                return true;
            if (histList.last())
                break;
            histList.findNext();
        }
        return false;
    }

    // check overlapping boundaries
    private boolean overlaps(IRide rideObj, IDateTime pickup, IDateTime dropoff) {
        if (rideObj == null) return false;
        return pickup.compareTo(rideObj.getDropoffTime()) < 0
                && rideObj.getPickupTime().compareTo(dropoff) < 0;
    }

    // delete ride entirely from system storage
    private void deleteRide(IRide targetRide) {
        if (targetRide == null) return;
        
        rides.removeRideById(targetRide.getRideId());
        
        if (targetRide.getDriver() != null) {
            removeFromHistory(targetRide.getDriver(), targetRide);
        }
        
        if (targetRide instanceof IPrivateRide) {
            IPrivateRide privRide = (IPrivateRide) targetRide;
            if (privRide.getRider() != null) {
                removeFromHistory(privRide.getRider(), targetRide);
            }
        } else if (targetRide instanceof ISharedRide) {
            ISharedRide shRide = (ISharedRide) targetRide;
            LinkedList<IRider> partCol = shRide.getParticipants();
            if (partCol != null && !partCol.empty()) {
                partCol.findFirst();
                while (true) {
                    removeFromHistory(partCol.retrieve(), targetRide);
                    if (partCol.last())
                        break;
                    partCol.findNext();
                }
            }
        }
    }

    // helper to clean person history
    private void removeFromHistory(IPerson personObj, IRide targetRide) {
        if (personObj == null || targetRide == null) return;
        LinkedList<IRide> histCol = personObj.getRideHistory();
        if (histCol == null || histCol.empty())
            return;
            
        histCol.findFirst();
        while (true) {
            IRide checkRide = histCol.retrieve();
            if (checkRide != null && checkRide.getRideId() == targetRide.getRideId()) {
                histCol.remove();
                return;
            }
            if (histCol.last())
                break;
            histCol.findNext();
        }
    }

    // copy participants securely
    private void copyRiders(IRide rideObj, LinkedList<IRider> outputCol) {
        if (!(rideObj instanceof ISharedRide))
            return;
        ISharedRide shRide = (ISharedRide) rideObj;
        LinkedList<IRider> sourceList = shRide.getParticipants();
        if (sourceList == null || sourceList.empty())
            return;
            
        sourceList.findFirst();
        while (true) {
            addIfNew(outputCol, sourceList.retrieve());
            if (sourceList.last())
                break;
            sourceList.findNext();
        }
    }

    // prevent duplicate insertions
    private void addIfNew(LinkedList<IRider> targetCol, IRider newRider) {
        if (newRider == null || targetCol == null) return;
        if (!targetCol.empty()) {
            targetCol.findFirst();
            while (true) {
                IRider existingRider = targetCol.retrieve();
                if (existingRider != null && existingRider.getId() == newRider.getId())
                    return;
                if (targetCol.last())
                    break;
                targetCol.findNext();
            }
        }
        targetCol.insert(newRider);
    }
}
