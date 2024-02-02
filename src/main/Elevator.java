package src.main;

import java.util.ArrayList;

public class Elevator {
    private int currentFloor;
    private static int floorsPassed;
    private int id;
    private ArrayList<Button> ElevatorButton;

    private boolean goingUp = true;

    public Elevator(int id, int numButtons){
        this.id = id;
        this.currentFloor = 1;
        floorsPassed = 0;
        this.ElevatorButton = new ArrayList<Button>(numButtons);
        for (int i = 0; i < numButtons; i++) {
            Lamp floorLamp = new Lamp(i);
            Button floorButton = new Button(i, floorLamp);
            this.ElevatorButton.add(floorButton);
        }

    }

    public void gotToNextFloor(){
        floorsPassed += 1;
    }


    public static int getFloorsPassed() {
        return floorsPassed;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public boolean isGoingUp() { return goingUp; }

    public void setGoingUp(boolean goingUp) { this.goingUp = goingUp; }
}
