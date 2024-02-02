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
                System.out.println("Received Job @"+currentJob.getTimeStamp()+" for floor #"+currentJob.getFloor()+" Pressed the Button "+currentJob.getButton());
            }
            this.currentJob = null;
        }
    }
}
