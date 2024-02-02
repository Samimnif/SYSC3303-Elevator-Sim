public class main{
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler(1);
        Elevator elevator = new Elevator(1, 3);
        Thread Scheduler = new Thread(scheduler, "Scheduler");
        Scheduler.info();
        Thread Elevator = new Thread(new ElevatorSubsystem(elevator,scheduler), "Elevator");
        Thread Floor = new Thread(new FloorSubsystem(1, scheduler));
    }
}
