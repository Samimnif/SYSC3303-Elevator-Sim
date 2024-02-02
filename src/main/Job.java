package src.main;

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

    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

    public void setElevatorID(int elevatorID) { this.elevatorID = elevatorID; }

    public void setFloor(int floor) { this.floor = floor; }

    public void setButton(String button) { this.button = button; }
}
