import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Properties;

public class Main {
    private static int MAX_JOB, NUM_ELEVATOR, NUM_FLOORS, SERVER_PORT, SCHEDULER_PORT;
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
            SERVER_PORT = Integer.parseInt(prop.getProperty("SERVER_PORT"));
            SCHEDULER_PORT = Integer.parseInt(prop.getProperty("SCHEDULER_PORT"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Output to check and display info at the start of program
        System.out.println("\033[1;96mELEVATOR SIMULATOR \n\n\033[1;32mCONFIG FILE Input:");
        System.out.println("\033[1;34mMAX Job: \033[0m" + MAX_JOB);
        System.out.println("\033[1;34mTotal Elevators: \033[0m" + NUM_ELEVATOR);
        System.out.println("\033[1;34mTotal Floors: \033[0m" + NUM_FLOORS);
        System.out.println("\033[1;34mServer Port: \033[0m" + SERVER_PORT);
        System.out.println("\033[1;34mScheduler Port: \033[0m" + SCHEDULER_PORT);
        System.out.println();


        Scheduler scheduler = new Scheduler(MAX_JOB, SCHEDULER_PORT);
        SchedulerStateMachine schedulerStateMachine = new SchedulerStateMachine(scheduler);
        //scheduler.info();
        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(NUM_ELEVATOR, NUM_FLOORS, scheduler, SCHEDULER_PORT);
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