import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Elevator implements Serializable, Runnable {

    public enum elevatorStates{
        IDLE,
        STOP,
        MOVING,
        LOAD,
        UNLOAD
    }
    private int currentFloor;
    private Job currentJob;
    private elevatorStates currentState;
    private static int floorsPassed;
    private int id;
    private ArrayList<Button> ElevatorButton;
    private Motor mainMotor;
    private Door mainDoor;

    private boolean goingUp = true, isLoaded = false;
    private boolean idle = true;

    public Elevator(int id, int numButtons){
        this.id = id;
        this.currentState = elevatorStates.IDLE;
        this.currentJob = null;
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

    public void printThreadInfo(){
        System.out.printf("\033[45m\033[1;30m%s - %s:\033[0m ", new Timestamp(System.currentTimeMillis()), Thread.currentThread().getName());
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

    public elevatorStates currentState(){
        return currentState;
    }
    public elevatorStates getCurrentState() {
        return currentState;
    }

    public boolean isGoingUp() { return goingUp; }

    public void setGoingUp(boolean goingUp) { this.goingUp = goingUp; }

    public boolean isIdle() { return idle; }

    public void setIdle(boolean idle) { this.idle = idle; }

    public synchronized void setJob(Job assignedJob){
        this.currentJob = assignedJob;
    }

    public Job getCurrentJob() {
        return currentJob;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void moveElevator(){
        if ((currentJob.getPickupFloor() > currentFloor)){
            System.out.println(Thread.currentThread().getName()+": Elevator "+this.getId()+" is going UP to floor #"+currentJob.getPickupFloor());
            currentFloor++;
        }else if (currentJob.getPickupFloor() < currentFloor){
            System.out.println(Thread.currentThread().getName()+": Elevator "+this.getId()+" is going DOWN to floor #"+currentJob.getPickupFloor());
            currentFloor--;
        }
    }

    @Override
    public void run(){
        while (true) {
            if (currentJob != null) {
                //printThreadInfo();
                //System.out.println("Current Job : " + currentJob.getTimeStamp() + " IS Idle: "+idle);
            }

            switch (currentState){
                // Idle State
                case IDLE:
                    idle = true;
                    // If there is a current job and the elevator is not at the destination floor then state transition
                    // into the moving state
                    if(currentJob != null && currentFloor != currentJob.getDestinationFloor()){
                        currentState = elevatorStates.MOVING;
                        printThreadInfo();
                        System.out.println("Moving");
                        idle = false;
                    // If there is a current job and the elevator is at the pickup floor then state transition into the
                    // loading state
                    }else if(currentJob != null && currentFloor == currentJob.getPickupFloor()){
                        currentState = elevatorStates.LOAD;
                        printThreadInfo();
                        System.out.println("Loading");
                        idle = false;
                    }

                    break;
                // Moving State
                case MOVING:
                    // If the elevator is current;y at the destination floor and has passengers then transition to the stop state
                    // If the elevator is currently at the pickup floor and has no passengers then transition to the stop state
                    if((currentFloor == currentJob.getDestinationFloor() && isLoaded) || (currentFloor == currentJob.getPickupFloor() && !isLoaded)){
                        currentState = elevatorStates.STOP;
                        printThreadInfo();
                        System.out.println("Stopping at floor "+ currentFloor);
                    // If the elevator doesn't have passengers and is above the pickup floor then move the elevator down and
                    // stay in the moving state
                    }else if(!isLoaded && currentFloor > currentJob.getPickupFloor()){
                        currentFloor -= 1;
                        printThreadInfo();
                        System.out.println("Moving DOWN to floor: "+ currentFloor);
                    // If the elevator doesn't have passengers and is below the pickup floor then move the elevator up and
                    // stay in the moving state
                    }else if(!isLoaded && currentFloor < currentJob.getPickupFloor()){
                        currentFloor += 1;
                        printThreadInfo();
                        System.out.println("Moving UP to floor: "+ currentFloor);
                    // If the elevator has passengers and is above the destination floor then move the elevator down and
                    // stay in the moving state
                    }else if(isLoaded && currentFloor > currentJob.getDestinationFloor()){
                        currentFloor -= 1;
                        printThreadInfo();
                        System.out.println("Moving DOWN to floor: "+ currentFloor);
                    // If the elevator has passengers and is below the destination floor then move the elevator up and
                    // stay in the moving state
                    }else if(isLoaded && currentFloor < currentJob.getDestinationFloor()){
                        currentFloor += 1;
                        printThreadInfo();
                        System.out.println("Moving UP to floor: "+ currentFloor);
                    }
                    break;
                // Stop State
                case STOP:
                    // If the elevator is at the destination floor then transition into the unload state
                    if(currentFloor == currentJob.getDestinationFloor()){
                        currentState = elevatorStates.UNLOAD;
                        printThreadInfo();
                        System.out.println("Unloading");
                    // If the elevator is at the pickup floor then transition in the load state
                    }else if(currentFloor == currentJob.getPickupFloor()){
                        currentState = elevatorStates.LOAD;
                        printThreadInfo();
                        System.out.println("Loading");
                    }
                    break;
                // Unload State
                // Make currentJob null, and transition into the idle state
                case UNLOAD:
                    isLoaded = false;
                    currentJob = null;
                    currentState = elevatorStates.IDLE;
                    printThreadInfo();
                    System.out.println("Idle");
                    break;
                // Load State
                // Transition into the moving state
                case LOAD:
                    isLoaded = true;
                    currentState = elevatorStates.MOVING;
                    printThreadInfo();
                    System.out.println("Moving");
                    break;
            }
//            System.out.println(currentJob==null);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
