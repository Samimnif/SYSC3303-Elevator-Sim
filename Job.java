public class Job {
    private String timeStamp;
    private int destinationFloor;
    private int pickupFloor;
    private String button;

    public Job(String timeStamp, int destinationFloor, int pickupFloor, String button) {
        this.timeStamp = timeStamp;
        this.destinationFloor = destinationFloor;
        this.pickupFloor = pickupFloor;
        this.button = button;
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

}
