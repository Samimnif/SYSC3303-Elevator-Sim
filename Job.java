import java.io.Serializable;

public class Job implements Serializable {
    private String timeStamp;
    private int destinationFloor;
    private int pickupFloor;
    private String button;
    private int fault;


    public Job(String timeStamp, int destinationFloor, int pickupFloor, String button, int fault) {
        this.timeStamp = timeStamp;
        this.destinationFloor = destinationFloor;
        this.pickupFloor = pickupFloor;
        this.button = button;
        this.fault = fault;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getDestinationFloor() { return destinationFloor; }

    public int getPickupFloor() { return pickupFloor; }

    public String getButton() {
        return button;
    }

    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

    public void setDestinationFloor(int destinationFloor) { this.destinationFloor = destinationFloor; }

    public void setPickupFloor(int pickupFloor) { this.pickupFloor = pickupFloor; }

    public void setButton(String button) { this.button = button; }

    public void setFault(int fault) {
        this.fault = fault;
    }

    public int getFault() {
        return fault;
    }
}
