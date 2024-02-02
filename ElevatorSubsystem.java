import java.util.Scanner;

public class ElevatorSubsystem implements Runnable {
    private Scheduler scheduler;
    private Elevator elevator;
    public Job currentJob;
    Scanner inputTerminal = new Scanner(System.in);
    String userInput = "";

    public ElevatorSubsystem(Elevator elevatorObj, Scheduler scheduler){
        this.scheduler = scheduler;
        this.elevator = elevatorObj;
    }
    @Override
    public void run() {
        while(!scheduler.getProgramStatus()){
            this.currentJob =  scheduler.get();
            if (this.currentJob != null){
                System.out.println(Thread.currentThread().getName()+": Received Job @"+currentJob.getTimeStamp()+" for floor #"+currentJob.getFloor()+" Pressed the Button "+currentJob.getButton());
                System.out.println(Thread.currentThread().getName()+": Please select a floor:");
                //------------ We have to fix below, when used the scheduler doesn't sync -------------
                /*userInput = inputTerminal.nextLine();
                int floor = Integer.parseInt(userInput);
                if (floor < elevator.getCurrentFloor() && (currentJob.getButton()).equals("Down")){
                    System.out.println(Thread.currentThread().getName()+": Going Down");
                }
                else if (floor > elevator.getCurrentFloor() && (currentJob.getButton()).equals("Up")) {
                    System.out.println(Thread.currentThread().getName()+": Going Up");
                }
                else {
                    System.out.println(Thread.currentThread().getName()+": You are going "+currentJob.getButton()+". Please Select floors above you.");
                }

                 */
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.currentJob = null;
        }
    }
}
