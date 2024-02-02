public class Main {
    public static void main(String args[]){
        Scheduler scheduler = new Scheduler(5);
        scheduler.info();
        Elevator elevator = new Elevator(1, 3);
        Thread Scheduler = new Thread(scheduler, "Scheduler Thread");
        Thread Elevator = new Thread(new ElevatorSubsystem(elevator,scheduler), "Elevator Thread");
        Thread Floor = new Thread(new FloorSubsystem(1, scheduler), "Floor Thread");

        Scheduler.start();
        Elevator.start();
        Floor.start();
    }
}