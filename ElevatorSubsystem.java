import java.util.Scanner;

public class ElevatorSubsystem implements Runnable {
    private Scheduler scheduler;
    private Elevator elevator;
    public Job currentJob;

    public ElevatorSubsystem(Elevator elevatorObj, Scheduler scheduler){
        this.scheduler = scheduler;
        this.elevator = elevatorObj;
    }
    @Override
    public void run() {
        while(!scheduler.getProgramStatus()){
            this.currentJob =  scheduler.get();
            if (this.currentJob != null){
                System.out.println(Thread.currentThread().getName()+": Received Job @"+currentJob.getTimeStamp()+" for floor #"+currentJob.getPickupFloor()+" Pressed the Button "+currentJob.getButton() + " going to " + currentJob.getDestinationFloor());
            } else {
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.currentJob = null;
        }
        System.out.println(Thread.currentThread().getName() + "Elevator Subsystem Job ended");
    }
}
