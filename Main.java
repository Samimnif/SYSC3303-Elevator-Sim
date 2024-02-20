public class Main {
    private static final int NUM_ELEVATOR= 1;
    private static final int NUM_FLOORS = 1;
    public static void main(String args[]){
        Scheduler scheduler = new Scheduler(5);
        SchedulerStateMachine schedulerStateMachine = new SchedulerStateMachine(scheduler);
        scheduler.info();
        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(NUM_ELEVATOR, NUM_FLOORS, scheduler, schedulerStateMachine);
        //Elevator elevator = new Elevator(1, 3);
        Thread Scheduler = new Thread(scheduler, "Scheduler Thread");
//        Thread Elevator = new Thread(new ElevatorSubsystem(NUM_ELEVATOR, NUM_FLOORS, scheduler), "Elevator Thread");
        Thread Floor = new Thread(new FloorSubsystem(NUM_FLOORS, scheduler, schedulerStateMachine), "Floor Thread");
        Thread ElevatorSubsystemStateMachine = new Thread(new ElevatorSubsystemStateMachine(elevatorSubsystem), "ElevatorSubsystem StateMachine Thread");

        Scheduler.start();
        ElevatorSubsystemStateMachine.start();
//        Elevator.start();
        Floor.start();
    }
}