import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Properties;

public class Main {
    private static int MAX_JOB, NUM_ELEVATOR, NUM_FLOORS, ELEVATOR_PORT, SCHEDULER_PORTF, SCHEDULER_PORTE, FLOOR_PORT, ELEVATOR_CAPACITY;
    private static final String file = "config.properties";

    public static void main(String args[]){
        /* This section helps us to retrieve the Config Attributes from the config file */
        try {
            FileInputStream propsInput = new FileInputStream(file);
            Properties prop = new Properties();
            prop.load(propsInput);

            MAX_JOB = Integer.parseInt(prop.getProperty("MAX_JOB"));
            NUM_ELEVATOR = Integer.parseInt(prop.getProperty("NUM_ELEVATOR"));
            NUM_FLOORS = Integer.parseInt(prop.getProperty("NUM_FLOORS"));
            ELEVATOR_PORT = Integer.parseInt(prop.getProperty("ELEVATOR_PORT"));
            SCHEDULER_PORTF = Integer.parseInt(prop.getProperty("SCHEDULER_PORTF"));
            SCHEDULER_PORTE = Integer.parseInt(prop.getProperty("SCHEDULER_PORTE"));
            FLOOR_PORT = Integer.parseInt(prop.getProperty("FLOOR_PORT"));
            ELEVATOR_CAPACITY = Integer.parseInt(prop.getProperty("ELEVATOR_CAPACITY"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Output to check and display info at the start of program
        System.out.println("\033[1;96mELEVATOR SIMULATOR \n\n\033[1;32mCONFIG FILE Input:");
        System.out.println("\033[1;34mMAX Job: \033[0m" + MAX_JOB);
        System.out.println("\033[1;34mTotal Elevators: \033[0m" + NUM_ELEVATOR);
        System.out.println("\033[1;34mTotal Floors: \033[0m" + NUM_FLOORS);
        System.out.println("\033[1;34mElevator Port: \033[0m" + ELEVATOR_PORT);
        System.out.println("\033[1;34mScheduler Elevator Port: \033[0m" + SCHEDULER_PORTE);
        System.out.println("\033[1;34mScheduler Floor Port: \033[0m" + SCHEDULER_PORTF);
        System.out.println("\033[1;34mFloor Port: \033[0m" + FLOOR_PORT);


        Scheduler scheduler = new Scheduler(MAX_JOB, NUM_ELEVATOR, SCHEDULER_PORTE, SCHEDULER_PORTF);
        SchedulerStateMachine schedulerStateMachine = new SchedulerStateMachine(scheduler);

        //ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(NUM_ELEVATOR, NUM_FLOORS, scheduler, ELEVATOR_PORT, SCHEDULER_PORTE);
        Thread Elevator = new Thread(new ElevatorSubsystem(NUM_ELEVATOR, ELEVATOR_CAPACITY, NUM_FLOORS, ELEVATOR_PORT, SCHEDULER_PORTE), "ElevatorSub");

        Thread Scheduler = new Thread(scheduler, "Scheduler Thread");
//        Thread Elevator = new Thread(new ElevatorSubsystem(NUM_ELEVATOR, NUM_FLOORS, scheduler), "Elevator Thread");
        Thread Floor = new Thread(new FloorSubsystem(NUM_FLOORS, FLOOR_PORT, SCHEDULER_PORTF), "Floor Thread");
        //Thread ElevatorSubsystemStateMachine = new Thread(new ElevatorSubsystemStateMachine(elevatorSubsystem), "ElevatorSubsystem StateMachine Thread");

        Scheduler.start();
        //ElevatorSubsystemStateMachine.start();
        Elevator.start();
        Floor.start();
    }
}