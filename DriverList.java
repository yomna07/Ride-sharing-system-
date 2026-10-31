package codes;

public class DriverList implements IDriverList {
    private Node<IDriver> head;
    private int size;

    public DriverList() {
        head = null;
        size = 0;
    }

    public boolean add(IDriver driver) {
    	if (driver == null) {
            return false;
        }

        String newPlate = driver.getVehiclePlate();

        if (newPlate == null) {
            return false;
        }

        Node<IDriver> check = head;

        while (check != null) {

            if (check.data.getId() == driver.getId() || newPlate.equals(check.data.getVehiclePlate())) {
                return false;
            }

            check = check.next;
        }

        if (head == null) {
            head = new Node<IDriver>(driver);
            size++;
            return true;
        }

        if (driver.compareTo(head.data) < 0) {
            Node<IDriver> newNode = new Node<IDriver>(driver);
            newNode.next = head;
            head = newNode;
            size++;
            return true;
        }

        Node<IDriver> current = head;

        while (current.next != null && current.next.data.compareTo(driver) < 0) {
            current = current.next;
        }

        Node<IDriver> newNode = new Node<IDriver>(driver);
        newNode.next = current.next;
        current.next = newNode;
        size++;
        return true;
    }

    public IDriver findById(int driverId) {
    	Node<IDriver> current = head;

        while (current != null) {

            int currentId = current.data.getId();

            if (currentId == driverId) {
                return current.data;
            }

            if (currentId > driverId) {
                return null;
            }

            current = current.next;
        }
    	return null;
    }

    public LinkedList<IDriver> findByName(String fullName) {
    	LinkedList<IDriver> matches = new LinkedList<IDriver>();

        if (fullName == null) {
            return matches;
        }

        Node<IDriver> current = head;

        while (current != null) {

            if (fullName.equals(current.data.getName())) {
                matches.insert(current.data);
            }

            current = current.next;
        }

        return matches;
    }

    public IDriver findByVehiclePlate(String vehiclePlate) {
    	if (vehiclePlate == null) {
            return null;
        }

        Node<IDriver> current = head;

        while (current != null) {

            if (vehiclePlate.equals(current.data.getVehiclePlate())) {
                return current.data;
            }

            current = current.next;
        }
    	return null;
    }

    public LinkedList<IDriver> findByVehicleType(VehicleType vehicleType) {
    	LinkedList<IDriver> matches = new LinkedList<IDriver>();

        if (vehicleType == null) {
            return matches;
        }

        Node<IDriver> current = head;

        while (current != null) {

            if (current.data.getVehicleType() == vehicleType) {
                matches.insert(current.data);
            }

            current = current.next;
        }

        return matches;
    }

    public LinkedList<IDriver> getAll() {
    	LinkedList<IDriver> allDrivers = new LinkedList<IDriver>();
        Node<IDriver> current = head;

        while (current != null) {
            allDrivers.insert(current.data);
            current = current.next;
        }

        return allDrivers;
    }

    public boolean removeById(int driverId) {
    	if (head == null) {
            return false;
        }

        if (head.data.getId() == driverId) {
            head = head.next;
            size--;
            return true;
        }

        if (head.data.getId() > driverId) {
            return false;
        }

        Node<IDriver> current = head;

        while (current.next != null) {

            int nextId = current.next.data.getId();

            if (nextId == driverId) {
                current.next = current.next.next;
                size--;
                return true;
            }

            if (nextId > driverId) {
                return false;
            }

            current = current.next;
        }

    	return false;
    }

    public boolean removeByVehiclePlate(String vehiclePlate) {
        if (vehiclePlate == null || head == null) {
            return false;
        }

        if (vehiclePlate.equals(head.data.getVehiclePlate())) {
            head = head.next;
            size--;
            return true;
        }

        Node<IDriver> current = head;

        while (current.next != null) {

            if (vehiclePlate.equals(current.next.data.getVehiclePlate())) {
                current.next = current.next.next;
                size--;
                return true;
            }

            current = current.next;
        }
        return false;
    }

    public int removeByName(String fullName) {
    	if (fullName == null) {
            return 0;
        }

        int removedCount = 0;

        while (head != null && fullName.equals(head.data.getName())) {
            head = head.next;
            removedCount++;
        }

        Node<IDriver> current = head;

        while (current != null && current.next != null) {

            if (fullName.equals(current.next.data.getName())) {
                current.next = current.next.next;
                removedCount++;
            } else {
                current = current.next;
            }
        }

        size -= removedCount;
        return removedCount;
    }

    public int removeByVehicleType(VehicleType vehicleType) {
    	if (vehicleType == null) {
            return 0;
        }

        int removedCount = 0;

        while (head != null &&
               head.data.getVehicleType() == vehicleType) {

            head = head.next;
            removedCount++;
        }

        Node<IDriver> current = head;

        while (current != null && current.next != null) {

            if (current.next.data.getVehicleType() == vehicleType) {
                current.next = current.next.next;
                removedCount++;
            } else {
                current = current.next;
            }
        }

        size -= removedCount;

        return removedCount;
    }

    public int size() {
        return size;
    }
}

