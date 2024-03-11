import java.util.ArrayList;

public class Elevator {
    private int currentFloor;
    private static int floorsPassed;
    private int id;
    private ArrayList<Button> ElevatorButton;
    private Motor mainMotor;
    private Door mainDoor;

    private boolean goingUp = true;
    private boolean idle = true;

    public Elevator(int id, int numButtons){
        this.id = id;
        this.currentFloor = 1;
        this.mainDoor = new Door();
        this.mainMotor = new Motor();
        floorsPassed = 0;
        this.ElevatorButton = new ArrayList<Button>(numButtons);
        for (int i = 0; i < numButtons; i++) {
            Lamp floorLamp = new Lamp(i);
            Button floorButton = new Button(i, floorLamp);
            this.ElevatorButton.add(floorButton);
        }

    }

    public void goToNextFloor(){
        floorsPassed += 1;
    }

    public void goToFloor(int floorNum) {
        if (currentFloor != floorNum) {
            this.currentFloor = floorNum;
            System.out.println(System.currentTimeMillis() + " - " + Thread.currentThread().getName() + ": Elevator " + id + " arrived at floor #" + currentFloor);
        }
    }

    public int getId() {
        return id;
    }

    public static int getFloorsPassed() {
        return floorsPassed;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public boolean isGoingUp() { return goingUp; }

    public void setGoingUp(boolean goingUp) { this.goingUp = goingUp; }

    public boolean isIdle() { return idle; }

    public void setIdle(boolean idle) { this.idle = idle; }
}
