public class Job {
    private String timeStamp;
    private int elevatorID;
    private int floor;
    private String button;

    public Job(String timeStamp, int elevatorID, int floor, String button) {
        this.timeStamp = timeStamp;
        this.elevatorID = elevatorID;
        this.floor = floor;
        this.button = button;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getElevatorID() {
        return elevatorID;
    }

    public int getFloor() {
        return floor;
    }

    public String getButton() {
        return button;
    }

}
