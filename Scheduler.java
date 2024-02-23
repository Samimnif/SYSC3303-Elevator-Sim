import java.util.ArrayList;

public class Scheduler implements Runnable{
    private boolean empty = true;
    private boolean emptyElevator = true;
    private ArrayList<Job> jobList= new ArrayList<>();
    private ArrayList<Elevator> elevatorsList= new ArrayList<>();
    private int MAX_SIZE;
    private boolean endProgram = false;
    private boolean floorProgram = false;
    private boolean elevatorProgram = false;

    public Scheduler(int maxJobs) {
        this.jobList = new ArrayList<Job>(maxJobs); //sets the max jobs depending on number of elevators
        this.MAX_SIZE = maxJobs;
    }

    public synchronized void put(Job newJob) {
        // Wait for the Box to be empty
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        if (this.jobList.size() < MAX_SIZE){
            this.jobList.add(newJob);
        }

        if (newJob == null) {
            setFloorProgram(true);
            setElevatorProgram(true);
            //System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Putting in Box Job @"+newJob.getTimeStamp()+" for floor #"+newJob.getPickupFloor()+" Pressed the Button "+newJob.getButton() + " going to " + newJob.getDestinationFloor());
        }

        empty = jobList.isEmpty(); // Mark the box as empty if ArrayList isn't filled
        notifyAll();
    }
    public synchronized Job get() {
        // Wait for the Box to full (not empty)
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        //printJobList();
        // Mark the box as empty.
        //Job returnedJob = jobList.get(0); //save the first // we just use remove below
        Job returnedJob = jobList.remove(0);
        empty = jobList.isEmpty();
        notifyAll();
        return returnedJob;
    }

    public synchronized void putElevators(ArrayList<Elevator> elevatorList) {
        // Wait for the Box to be empty
        while (!emptyElevator) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        this.elevatorsList = elevatorList;
        emptyElevator = false;

        notifyAll();
    }
    public synchronized ArrayList<Elevator> getElevators() {
        // Wait for the Box to full (not empty)
        while (emptyElevator) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        emptyElevator = true;
        notifyAll();
        return this.elevatorsList;
    }


    public boolean getProgramStatus(){
        return this.endProgram;
    }

    public void info(){
        if (!endProgram){
            System.out.println("program is running");
        }
        else{
            System.out.println("Program is offline");
        }

        System.out.println("Max Job list Size: "+ MAX_SIZE + "\n");
        //System.out.println(jobList);
    }

    private void printJobList(){
        for (int i = 0; i < MAX_SIZE; i++) {
            try {
                System.out.print(jobList.get(i)+" -> ");
            }catch (IndexOutOfBoundsException e)
            {
                System.out.print("NULL -> ");
            }
        }
        System.out.print("END\n");
    }

    public ArrayList<Job> getJobList() {
        return jobList;
    }

    @Override
    public void run() {
        /*while(!endProgram){
            if (elevatorProgram){
                this.endProgram = true;
            }
        }
        System.out.println("Scheduler ended");*/
    }

    public boolean isEmpty() { return empty; }

    public void setFloorProgram(boolean floorProgram) {
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Set End Floor");
        this.floorProgram = floorProgram;
    }

    public void setElevatorProgram(boolean elevatorProgram) {
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Set End Elevator");
        this.elevatorProgram = elevatorProgram;
        notifyAll();
    }

    public boolean isElevatorProgram() {
        return elevatorProgram;
    }

    public void notified(Elevator elevator){
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Scheduler is notified that elevator " + elevator.getId() + " is at floor " + elevator.getCurrentFloor());
    }
}
