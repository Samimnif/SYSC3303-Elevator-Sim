import java.util.ArrayList;

import static java.lang.Math.abs;

public class ElevatorSubsystem /*implements Runnable*/ {
    private Scheduler scheduler;
    //private Elevator elevator;
    private SchedulerStateMachine schedulerStateMachine;
    private ArrayList<Elevator> elevatorsList= new ArrayList<>();

    private Elevator currentElevator;
    public Job currentJob;

    public ElevatorSubsystem(int numElevators, int numFloors, Scheduler scheduler, SchedulerStateMachine schedulerStateMachine){
        this.scheduler = scheduler;
        this.schedulerStateMachine = schedulerStateMachine;
        this.elevatorsList= new ArrayList<Elevator>(numElevators);
        for (int i = 0; i < numElevators; i++) {
            Elevator elevator = new Elevator(i+1, numFloors);
            this.elevatorsList.add(elevator);
        }
        scheduler.putElevators(elevatorsList);
        System.out.println(elevatorsList);
    }

    public void receiveNewTask() { this.currentJob = schedulerStateMachine.sendTask(); }

    protected void delegateTask() { //choose the best elevator to give task to, currently just giving to first elevator
        scheduler.putElevators(elevatorsList);
        int i = scheduler.delegateTask(currentJob);

        currentElevator = elevatorsList.get(i);

        currentElevator.setGoingUp(currentJob.getPickupFloor() > currentElevator.getCurrentFloor());
        logElevatorGoingUp(currentElevator);
        currentElevator.goToFloor(currentJob.getPickupFloor());

        currentElevator.setGoingUp(currentJob.getDestinationFloor() > currentElevator.getCurrentFloor());
        logElevatorGoingUp(currentElevator);
        currentElevator.goToFloor(currentJob.getDestinationFloor());
    }

    private void logElevatorGoingUp(Elevator e) {
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName()+": Elevator "+currentElevator.getId()+ (e.isGoingUp()? " is going UP to floor #" : "is going DOWN to floor #")+currentJob.getPickupFloor());

    }

    protected void notifyScheduler() {
        //do something here with the scheduler to notify it that the elevator has arrived at its destination
        scheduler.notified(currentElevator);
    }

    protected boolean getProgramStatus() {
        return scheduler.isElevatorProgram();
    }

    protected void setProgramStatus(boolean status) {
        scheduler.setElevatorProgram(status);
    }

//    @Override
//    public void run() {
//        scheduler.putElevators(elevatorsList);
//        Elevator serviceElevator;
//        while(!scheduler.getProgramStatus()){
//            this.currentJob =  scheduler.get();
//            if (this.currentJob != null){
//                System.out.println(Thread.currentThread().getName()+": Received Job @"+currentJob.getTimeStamp()+" for floor #"+currentJob.getPickupFloor()+" Pressed the Button "+currentJob.getButton() + " going to " + currentJob.getDestinationFloor());
//                //for now we assume one elevator is available
//                serviceElevator = elevatorsList.get(0); //Here we have to rewrite to account for multiple elevators and check which one is available to use
//                if ((currentJob.getPickupFloor() > serviceElevator.getCurrentFloor())){
//                    System.out.println(Thread.currentThread().getName()+": Elevator "+serviceElevator.getId()+" is going UP to floor #"+currentJob.getPickupFloor());
//                }else {
//                    System.out.println(Thread.currentThread().getName()+": Elevator "+serviceElevator.getId()+" is going DOWN to floor #"+currentJob.getPickupFloor());
//                }
//                serviceElevator.goToFloor(currentJob.getPickupFloor());
//                scheduler.putElevators(elevatorsList);
//                System.out.println(Thread.currentThread().getName()+": Elevator "+serviceElevator.getId()+" arrived to floor #"+currentJob.getPickupFloor() + " and now is going "+ currentJob.getButton() +" to "+currentJob.getDestinationFloor());
//
//                serviceElevator.goToFloor(currentJob.getDestinationFloor());
//                scheduler.putElevators(elevatorsList);
//                System.out.println(Thread.currentThread().getName()+": Elevator "+serviceElevator.getId()+" arrived to destination floor #"+currentJob.getDestinationFloor());
//            } else {
//                break;
//            }
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            this.currentJob = null;
//        }
//        setProgramStatus(true);
//        System.out.println(Thread.currentThread().getName() + "Elevator Subsystem Job ended");
//    }
}
