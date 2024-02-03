import java.util.ArrayList;

import static java.lang.Math.abs;

public class ElevatorSubsystem implements Runnable {
    private Scheduler scheduler;
    //private Elevator elevator;
    private ArrayList<Elevator> elevatorsList= new ArrayList<>();
    public Job currentJob;

    public ElevatorSubsystem(int numElevators, int numFloors, Scheduler scheduler){
        this.scheduler = scheduler;
        this.elevatorsList= new ArrayList<Elevator>(numElevators);
        for (int i = 0; i < numElevators; i++) {
            Elevator elevator = new Elevator(i+1, numFloors);
            this.elevatorsList.add(elevator);
        }
        System.out.println(elevatorsList);
        //this.elevator = elevatorObj;
    }
    @Override
    public void run() {
        scheduler.putElevators(elevatorsList);
        Elevator serviceElevator;
        while(!scheduler.getProgramStatus()){
            this.currentJob =  scheduler.get();
            if (this.currentJob != null){
                System.out.println(Thread.currentThread().getName()+": Received Job @"+currentJob.getTimeStamp()+" for floor #"+currentJob.getPickupFloor()+" Pressed the Button "+currentJob.getButton() + " going to " + currentJob.getDestinationFloor());
                //for now we assume one elevator is available
                serviceElevator = elevatorsList.get(0); //Here we have to rewrite to account for multiple elevators and check which one is available to use
                if ((currentJob.getPickupFloor() > serviceElevator.getCurrentFloor())){
                    System.out.println(Thread.currentThread().getName()+": Elevator "+serviceElevator.getId()+" is going UP to floor #"+currentJob.getPickupFloor());
                }else {
                    System.out.println(Thread.currentThread().getName()+": Elevator "+serviceElevator.getId()+" is going DOWN to floor #"+currentJob.getPickupFloor());
                }
                serviceElevator.goToFloor(currentJob.getPickupFloor());
                scheduler.putElevators(elevatorsList);
                System.out.println(Thread.currentThread().getName()+": Elevator "+serviceElevator.getId()+" arrived to floor #"+currentJob.getPickupFloor() + " and now is going "+ currentJob.getButton() +" to "+currentJob.getDestinationFloor());

                serviceElevator.goToFloor(currentJob.getDestinationFloor());
                scheduler.putElevators(elevatorsList);
                System.out.println(Thread.currentThread().getName()+": Elevator "+serviceElevator.getId()+" arrived to destination floor #"+currentJob.getDestinationFloor());
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
        scheduler.setElevatorProgram(true);
        System.out.println(Thread.currentThread().getName() + "Elevator Subsystem Job ended");
    }
}
