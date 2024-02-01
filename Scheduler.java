import java.util.ArrayList;

public class Scheduler {
    private boolean empty = true;
    private ArrayList<Job> jobList= new ArrayList<>();
    private int MAX_SIZE;

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

    public static void main(String args[]){

        Thread Elevator = new Thread(new ElevatorSubsystem(1));
        Thread Floor = new Thread(new FloorSubsystem());



    }
}
