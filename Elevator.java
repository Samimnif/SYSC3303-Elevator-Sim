public class Elevator {
    private int currentFloor;
    private static int floorsPassed;
    private int id;


    public Elevator(){
        this.currentFloor = 1;
        floorsPassed = 0;
    }
    public Elevator(int id){
        this.id = id;
        this.currentFloor = 1;
        floorsPassed = 0;
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
}
