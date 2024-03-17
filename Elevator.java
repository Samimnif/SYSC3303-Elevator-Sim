import java.io.Serializable;
import java.util.ArrayList;

public class Elevator implements Serializable, Runnable {

    private enum elevatorStates{
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
                //System.out.println(Thread.currentThread().getName() + ": Current Job : " + currentJob.getTimeStamp() + " IS Idle: "+idle);
            }

            switch (currentState){
                case IDLE:
                    idle = true;
                    if(currentJob != null && currentFloor != currentJob.getDestinationFloor()){
                        currentState = elevatorStates.MOVING;
                        System.out.println(Thread.currentThread().getName() + " Moving");
                        idle = false;
                    }else if(currentJob != null && currentFloor == currentJob.getPickupFloor()){
                        currentState = elevatorStates.LOAD;
                        System.out.println(Thread.currentThread().getName() + " Loading");
                        idle = false;
                    }

                    break;
                case MOVING:
                    if((currentFloor == currentJob.getDestinationFloor() && isLoaded) || (currentFloor == currentJob.getPickupFloor() && !isLoaded)){
                        currentState = elevatorStates.STOP;
                        System.out.println(Thread.currentThread().getName() + " Stopping at floor "+ currentFloor);
                    }else if(!isLoaded && currentFloor > currentJob.getPickupFloor()){
                        currentFloor -= 1;
                        System.out.println(Thread.currentThread().getName() + " Moving DOWN to floor: "+ currentFloor);

                    }else if(!isLoaded && currentFloor < currentJob.getPickupFloor()){
                        currentFloor += 1;
                        System.out.println(Thread.currentThread().getName() + " Moving UP to floor: "+ currentFloor);

                    }else if(isLoaded && currentFloor > currentJob.getDestinationFloor()){
                        currentFloor -= 1;
                        System.out.println(Thread.currentThread().getName() + " Moving DOWN to floor: "+ currentFloor);

                    }else if(isLoaded && currentFloor < currentJob.getDestinationFloor()){
                        currentFloor += 1;
                        System.out.println(Thread.currentThread().getName() + " Moving UP to floor: "+ currentFloor);
                    }
                    break;
                case STOP:
                    if(currentFloor == currentJob.getDestinationFloor()){
                        currentState = elevatorStates.UNLOAD;
                        System.out.println(Thread.currentThread().getName() + " Unloading");
                    }else if(currentFloor == currentJob.getPickupFloor()){
                        currentState = elevatorStates.LOAD;
                        System.out.println(Thread.currentThread().getName() + " Loading");
                    }
                    break;
                case UNLOAD:
                    isLoaded = false;
                    currentJob = null;
                    if(currentJob == null){

                        currentState = elevatorStates.IDLE;
                        System.out.println(Thread.currentThread().getName() + " Idle");
                    }
                    break;
                case LOAD:
                    isLoaded = true;
                    currentState = elevatorStates.MOVING;
                    System.out.println(Thread.currentThread().getName() + " Moving");
                    break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
