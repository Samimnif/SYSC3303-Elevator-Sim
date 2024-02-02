import java.util.ArrayList;

public class Scheduler implements Runnable{
    private boolean empty = true;
    private ArrayList<Job> jobList= new ArrayList<>();
    private ArrayList<Elevator> elevatorsList= new ArrayList<>();
    private int MAX_SIZE;
    private boolean endProgram = false;

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

        // This Box is empty, so store obj.
        if (jobList.size() < MAX_SIZE){
            this.jobList.add(newJob);
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
        // Mark the box as empty.
        Job returnedJob = jobList.get(0); //save the first
        jobList.remove(0);
        empty = jobList.isEmpty();
        notifyAll();
        return returnedJob;
    }

    public boolean getProgramStatus(){
        return this.endProgram;
    }

    public void info(){
        if (getProgramStatus() == true){
            System.out.println("program is running");
        }
        else{
            System.out.println("Program is offline");
        }

        System.out.println("\n Max Size: "+ MAX_SIZE + "\n");
        System.out.println(jobList);
    }

    public ArrayList<Job> getJobList() {
        return jobList;
    }

    @Override
    public void run() {

    }

    /*
    public static void main(String args[]){
        Scheduler scheduler = new Scheduler(1);
        Elevator elevator = new Elevator(1, 3);
        Thread Elevator = new Thread(new ElevatorSubsystem(elevator,scheduler));
        Thread Floor = new Thread(new FloorSubsystem(1, scheduler));
    }

     */
}
