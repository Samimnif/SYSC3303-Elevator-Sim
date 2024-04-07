import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UnitTests {

    @Test
    public void checkReadInputFileTest(){
        System.out.println("Test 1");
        Job job;

        Scheduler scheduler = new Scheduler(4, 1,24, 23);
        SchedulerStateMachine schedulerStateMachine = new SchedulerStateMachine(scheduler);
        FloorSubsystem floor = new FloorSubsystem(1, 100, 23);

        job = floor.getNextJob();

        assertEquals(5, job.getDestinationFloor());
        assertEquals("11:10:11", job.getTimeStamp());
        assertEquals(3, job.getPickupFloor());
        assertEquals("Up", job.getButton());
    }

    @Test
    public void passDataToSchedulerTest(){
        Job job;

        Scheduler scheduler = new Scheduler(4, 1,64, 63);
        FloorSubsystem floor = new FloorSubsystem(1, 150, 63);

        job = floor.getNextJob();

        assertNotEquals(job, null);

        scheduler.put(job);

        ArrayList<Job> jobs = scheduler.getJobList();

        assertEquals(job.getDestinationFloor(), jobs.getFirst().getDestinationFloor());
        assertEquals(job.getPickupFloor(), jobs.getFirst().getPickupFloor());
        assertEquals(job.getButton(), jobs.getFirst().getButton());
        assertEquals(job.getTimeStamp(), jobs.getFirst().getTimeStamp());
    }

    @Test
    public void UpdateElevators(){
        Job job;

        Scheduler scheduler = new Scheduler(4, 1,34, 33);
        ElevatorSubsystem elevator = new ElevatorSubsystem(1, 5, 1, 70, 34);

        scheduler.putElevators(elevator.elevatorsList);

        for (int i = 0; i < scheduler.getElevators().size(); i++){
            assertEquals(scheduler.getElevators().get(i), elevator.elevatorsList.get(i));
        }
    }

    @Test
    public void AssignJobTest(){
        Job job;

        Scheduler scheduler = new Scheduler(4, 1,44, 43);
        FloorSubsystem floor = new FloorSubsystem(1, 102, 43);
        Elevator elevator = new Elevator(1, 1, 5);

        job = floor.getNextJob();
        scheduler.put(job);

        scheduler.elevatorsList.add(elevator);

        scheduler.assignJob();

        assertEquals(job.getPickupFloor(), elevator.getCurrentJob().getPickupFloor());
        assertEquals(job.getDestinationFloor(), elevator.getCurrentJob().getDestinationFloor());
        assertEquals(job.getButton(), elevator.getCurrentJob().getButton());
        assertEquals(job.getTimeStamp(), elevator.getCurrentJob().getTimeStamp());
    }

    @Test
    public void elevatorStateTest(){
        Job job;


        Scheduler scheduler = new Scheduler(4, 1,54, 53);
        SchedulerStateMachine schedulerStateMachine = new SchedulerStateMachine(scheduler);
        Elevator elevator = new Elevator(1,2, 5);
        ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(1, 5,5, 71, 54);
        FloorSubsystem floor = new FloorSubsystem(1, 103, 53);


        job = floor.getNextJob();

       assertEquals( elevator.currentState().name(), "IDLE");
       assertEquals(elevator.isIdle(), true);

    }


}
