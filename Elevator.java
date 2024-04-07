/**
 *
 */

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Elevator implements Serializable, Runnable {

    public enum elevatorStates{
        IDLE,
        STOP,
        MOVING,
        LOAD,
        UNLOAD
    }
    private int currentFloor, id, capacity;
    private Job currentJob;
    private elevatorStates currentState;
    private static int floorsPassed;
    private static int movements = 0;
    private ArrayList<Button> ElevatorButton;
    private Motor mainMotor;
    private Door mainDoor;
    private HashMap<Integer, Lamp> lamps;
    private boolean goingUp = true, isLoaded = false, idle = true;
    private final static double TRIP_TIME=5.22, SPEED=1.37, ACCELERATION=0.26;

    public Elevator(int id, int numButtons, int capacity){
        this.id = id;
        this.capacity = capacity;
        this.currentState = elevatorStates.IDLE;
        this.currentJob = null;
        this.currentFloor = 1;
        this.mainDoor = new Door();
        this.mainMotor = new Motor();
        this.lamps = new HashMap<>();
        floorsPassed = 0;
        this.ElevatorButton = new ArrayList<Button>(numButtons);
        for (int i = 0; i < numButtons; i++) {
            Lamp floorLamp = new Lamp(i);
            lamps.put(i, floorLamp);
            Button floorButton = new Button(i, floorLamp);
            this.ElevatorButton.add(floorButton);
        }
    }

    public void sensorFault(int fault){
        if (fault == 2){
            throw new RuntimeException("Sensor at floor: "+getCurrentFloor()+" has failed");
        }
    }

    public String printThreadInfo(){
        //System.out.printf("\033[45m\033[1;30m%s - %s:\033[0m ", new Timestamp(System.currentTimeMillis()), Thread.currentThread().getName());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        return "\033[45m\033[1;30m"+ sdf.format(new Timestamp(System.currentTimeMillis()))+ " - "+ Thread.currentThread().getName()+":\033[0m ";
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
    public void triggerTransientFault(){
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> future = service.schedule(() -> {
            mainDoor.toggleFault();
            service.shutdown();
        }, 15, TimeUnit.SECONDS);
    }

    @Override
    public void run(){
        System.out.println(printThreadInfo() + "initiated");
        while (true) {
            switch (currentState){
                // Idle State
                case IDLE:
                    //If the door is open and idle then close the door until there is another request
                    idle = true;
                    // If there is a current job and the elevator is not at the destination floor then state transition
                    // into the moving state
                    if(currentJob != null && currentFloor != currentJob.getDestinationFloor()){
                        currentState = elevatorStates.MOVING;
                        System.out.println(printThreadInfo()+"Moving");
                        idle = false;
                    // If there is a current job and the elevator is at the pickup floor then state transition into the
                    // loading state
                    }else if(currentJob != null && currentFloor == currentJob.getPickupFloor()){
                        // When the elevator transitions to the loading state, it will open the door as an entry action
                        try{
                            System.out.print(printThreadInfo());
                            mainDoor.openDoor();
                        } catch (Exception e){
                            System.out.println("Fault: " + e);
                            try{
                                Thread.sleep(5000);
                                mainDoor.toggleFault();
                            } catch (Exception ie){
                                ie.printStackTrace();
                            }
                        }
                        currentState = elevatorStates.LOAD;
                        System.out.println(printThreadInfo()+"Loading");
                        idle = false;
                    }

                    break;
                // Moving State
                case MOVING:
                    // If the elevator is currently at the destination floor and has passengers then transition to the stop state
                    // If the elevator is currently at the pickup floor and has no passengers then transition to the stop state
                    if((currentFloor == currentJob.getDestinationFloor() && isLoaded) || (currentFloor == currentJob.getPickupFloor() && !isLoaded)){
                        currentState = elevatorStates.STOP;
                        sensorFault(currentJob.getFault());
                        System.out.println(printThreadInfo()+"Stopping at floor "+ currentFloor);
                        System.out.println("Total Movements Of Elevators: " + movements);
                    // If the elevator doesn't have passengers and is above the pickup floor then move the elevator down and
                    // stay in the moving state

                    }else if(!isLoaded && currentFloor > currentJob.getPickupFloor()){
                        currentFloor -= 1;
                        movements += 1;
                        System.out.println(printThreadInfo()+"Moving DOWN to floor: "+ currentFloor);
                        try {
                            Thread.sleep((long) (TRIP_TIME*1000));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    // If the elevator doesn't have passengers and is below the pickup floor then move the elevator up and
                    // stay in the moving state
                    }else if(!isLoaded && currentFloor < currentJob.getPickupFloor()){
                        currentFloor += 1;
                        movements += 1;
                        System.out.println(printThreadInfo()+"Moving UP to floor: "+ currentFloor);
                        try {
                            Thread.sleep((long) (TRIP_TIME*1000));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    // If the elevator has passengers and is above the destination floor then move the elevator down and
                    // stay in the moving state
                    }else if(isLoaded && currentFloor > currentJob.getDestinationFloor()){
                        currentFloor -= 1;
                        movements += 1;
                        System.out.println(printThreadInfo()+"Moving DOWN to floor: "+ currentFloor);
                        try {
                            Thread.sleep((long) (TRIP_TIME*1000));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                    // If the elevator has passengers and is below the destination floor then move the elevator up and
                    // stay in the moving state
                    }else if(isLoaded && currentFloor < currentJob.getDestinationFloor()){
                        currentFloor += 1;
                        movements +=1;
                        System.out.println(printThreadInfo()+"Moving UP to floor: "+ currentFloor);
                        try {
                            Thread.sleep((long) (TRIP_TIME*1000));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                // Stop State
                case STOP:
                    // If the elevator is at the destination floor then transition into the unload state
                    if(currentFloor == currentJob.getDestinationFloor()){
                        // When the elevator transitions to the unloading state, it will open the door as an entry action
                        try{
                            System.out.print(printThreadInfo());
                            mainDoor.openDoor();
                        } catch (Exception e){
                            System.out.println("Fault: " + e);
                            try{
                                Thread.sleep(5000);
                                mainDoor.toggleFault();
                            } catch(Exception ie){
                                ie.printStackTrace();
                            }
                        }
                        currentState = elevatorStates.UNLOAD;
                        System.out.println(printThreadInfo()+"Unloading");
                    // If the elevator is at the pickup floor then transition in the load state
                    }else if(currentFloor == currentJob.getPickupFloor()){
                        // When the elevator transitions to the loading state, it will open the door as an entry action
                        try{
                            System.out.print(printThreadInfo());
                            mainDoor.openDoor();
                        } catch (Exception e){
                            System.out.println("Fault: " + e);
                            try{
                                Thread.sleep(5000);
                                mainDoor.toggleFault();
                            } catch(Exception ie){
                                ie.printStackTrace();
                            }
                        }
                        currentState = elevatorStates.LOAD;
                        System.out.println(printThreadInfo()+"Loading");
                    }
                    break;
                // Unload State
                // Make currentJob null, and transition into the idle state
                case UNLOAD:
                    System.out.print(printThreadInfo());
                    // Turn the lamp off for the destination floor
                    lamps.get(currentJob.getDestinationFloor()).toggleLamp();
                    isLoaded = false;
                    currentJob = null;
                    // When the elevator transitions to the idle state, it will close the door as an entry action
                    try{
                        System.out.print(printThreadInfo());
                        mainDoor.closeDoor();
                    } catch (Exception e){
                        System.out.println("Fault: " + e);
                        try{
                            Thread.sleep(5000);
                            mainDoor.toggleFault();
                        } catch(Exception ie){
                            ie.printStackTrace();
                        }
                    }
                    currentState = elevatorStates.IDLE;
                    System.out.println(printThreadInfo()+"\033[1;33mIdle\033[0m");
                    break;
                // Load State
                // Transition into the moving state
                case LOAD:
                    System.out.print(printThreadInfo());
                    // Turn the lamp on for the destination floor
                    lamps.get(currentJob.getDestinationFloor()).toggleLamp();
                    isLoaded = true;
                    // When the elevator transitions to the moving state, it will close the door as an entry action
                    try{
                        System.out.print(printThreadInfo());
                        mainDoor.closeDoor();
                    } catch (Exception e){
                        System.out.println("Fault: " + e);
                        try{
                            Thread.sleep(5000);
                            mainDoor.toggleFault();
                        } catch(Exception ie){
                            ie.printStackTrace();
                        }
                    }
                    currentState = elevatorStates.MOVING;
                    System.out.println(printThreadInfo()+"Moving");
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
