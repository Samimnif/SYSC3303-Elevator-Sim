import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * JUnit test class for validating the functionality of various components in the elevator control system.
 */
public class UnitTests {

    /**
     * Test case to verify the correctness of reading input file by the floor subsystem.
     */
    @Test
    public void checkReadInputFileTest(){
        System.out.println("Test 1");
        Job job;

        // Initializing necessary components
        View newView = new View();
        Scheduler scheduler = new Scheduler(4, 1,24, 23,newView);
        SchedulerStateMachine schedulerStateMachine = new SchedulerStateMachine(scheduler);
        FloorSubsystem floor = new FloorSubsystem(1, 100, 23);

        // Getting the next job from the floor subsystem
        job = floor.getNextJob();

        // Assertions to validate the job attributes
        assertEquals(18, job.getDestinationFloor());
        assertEquals("02:22:00:15", job.getTimeStamp());
        assertEquals(2, job.getPickupFloor());
        assertEquals("up", job.getButton());
    }

    /**
     * Test case to verify passing data to the scheduler and its functionality.
     */
    @Test
    public void passDataToSchedulerTest(){
        Job job;

        // Initializing necessary components
        View newView = new View();
        Scheduler scheduler = new Scheduler(4, 1,64, 63, newView);
        FloorSubsystem floor = new FloorSubsystem(1, 150, 63);

        // Getting the next job from the floor subsystem
        job = floor.getNextJob();

        // Asserting job is not null
        assertNotEquals(job, null);

        // Putting the job into scheduler
        scheduler.put(job);

        // Retrieving jobs from the scheduler
        ArrayList<Job> jobs = scheduler.getJobList();

        // Assertions to validate the correctness of the job stored in scheduler
        assertEquals(job.getDestinationFloor(), jobs.getFirst().getDestinationFloor());
        assertEquals(job.getPickupFloor(), jobs.getFirst().getPickupFloor());
        assertEquals(job.getButton(), jobs.getFirst().getButton());
        assertEquals(job.getTimeStamp(), jobs.getFirst().getTimeStamp());
    }

    /**
     * Test case to verify updating elevators by the scheduler.
     */
    @Test
    public void UpdateElevators(){
        Job job;

        // Initializing necessary components
        View newView = new View();
        Scheduler scheduler = new Scheduler(4, 1,34, 33, newView);
        ElevatorSubsystem elevator = new ElevatorSubsystem(1, 5, 1, 70, 34);

        // Putting elevators into the scheduler
        scheduler.putElevators(elevator.elevatorsList);

        // Assertions to validate the correctness of updated elevators
        for (int i = 0; i < scheduler.getElevators().size(); i++){
            assertEquals(scheduler.getElevators().get(i), elevator.elevatorsList.get(i));
        }
    }

    /**
     * Test case to verify assigning jobs to elevators by the scheduler.
     */
    @Test
    public void AssignJobTest(){
        Job job;

        // Initializing necessary components
        View newView = new View();
        Scheduler scheduler = new Scheduler(4, 1,44, 43, newView);
        FloorSubsystem floor = new FloorSubsystem(1, 102, 43);
        Elevator elevator = new Elevator(1, 1, 5);

        // Getting the next job from the floor subsystem
        job = floor.getNextJob();
        scheduler.put(job);

        // Adding elevator to the scheduler
        scheduler.elevatorsList.add(elevator);

        // Assigning job to elevator by the scheduler
        scheduler.assignJob();

        // Assertions to validate the correctness of the assigned job to elevator
        assertEquals(job.getPickupFloor(), elevator.getCurrentJob().getPickupFloor());
        assertEquals(job.getDestinationFloor(), elevator.getCurrentJob().getDestinationFloor());
        assertEquals(job.getButton(), elevator.getCurrentJob().getButton());
        assertEquals(job.getTimeStamp(), elevator.getCurrentJob().getTimeStamp());
    }

    /**
     * Test case to verify the state of elevator.
     */
    @Test
    public void elevatorStateTest(){
        Job job;

        // Initializing necessary components
        View newView = new View();
        Scheduler scheduler = new Scheduler(4, 1,54, 53, newView);
        SchedulerStateMachine schedulerStateMachine = new SchedulerStateMachine(scheduler);
        Elevator elevator = new Elevator(1,2, 5);
        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(1, 5,5, 71, 54);
        FloorSubsystem floor = new FloorSubsystem(1, 103, 53);

        // Getting the next job from the floor subsystem
        job = floor.getNextJob();

        // Assertions to verify the state of elevator
       assertEquals( elevator.currentState().name(), "IDLE");
       assertEquals(elevator.isIdle(), true);

    }


}
