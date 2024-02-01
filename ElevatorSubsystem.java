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

            }
            this.currentJob = null;
        }
    }
}
