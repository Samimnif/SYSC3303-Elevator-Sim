import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTests {
    @Test
    public void checkReadInputFileTest(){
        Job job;

        Scheduler scheduler = new Scheduler(1, 200);
        SchedulerStateMachine schedulerStateMachine = new SchedulerStateMachine(scheduler);
        FloorSubsystem floor = new FloorSubsystem(1, scheduler, schedulerStateMachine,100);

        job = floor.getNextJob();

        assertEquals(4, job.getDestinationFloor());
        assertEquals("14:05:15", job.getTimeStamp());
        assertEquals(2, job.getPickupFloor());
        assertEquals("Up", job.getButton());
    }

    @Test
    public void passDataToSchedulerTest(){
        Job job;

        Scheduler scheduler = new Scheduler(1, 200);
        SchedulerStateMachine schedulerStateMachine = new SchedulerStateMachine(scheduler);
        FloorSubsystem floor = new FloorSubsystem(1, scheduler, schedulerStateMachine,100);

        job = floor.getNextJob();

        scheduler.put(job);

        ArrayList<Job> jobs = scheduler.getJobList();

        assertEquals(job.getDestinationFloor(), jobs.get(0).getDestinationFloor());
        assertEquals(job.getPickupFloor(), jobs.get(0).getPickupFloor());
        assertEquals(job.getButton(), jobs.get(0).getButton());
        assertEquals(job.getTimeStamp(), jobs.get(0).getTimeStamp());
    }

    @Test
    public void getDataFromSchedulerTest(){
        Job job;

        Scheduler scheduler = new Scheduler(1, 200);
        SchedulerStateMachine schedulerStateMachine = new SchedulerStateMachine(scheduler);
        FloorSubsystem floor = new FloorSubsystem(1, scheduler, schedulerStateMachine,100 );

        job = floor.getNextJob();

        scheduler.put(job);

        Job newJob = scheduler.get();

        assertEquals(job.getPickupFloor(), newJob.getPickupFloor());
        assertEquals(job.getDestinationFloor(), newJob.getDestinationFloor());
        assertEquals(job.getButton(), newJob.getButton());
        assertEquals(job.getTimeStamp(), newJob.getTimeStamp());
    }
    @Test
    public void schedulerStateTest(){
        Job job;

        Scheduler scheduler = new Scheduler(1, 200);
        SchedulerStateMachine schedulerStateMachine = new SchedulerStateMachine(scheduler);
        FloorSubsystem floor = new FloorSubsystem(1, scheduler, schedulerStateMachine,100);

        assertEquals(schedulerStateMachine.getSchedulerState(), schedulerStateMachine.getEnumState("WAIT_FOR_FLOORSUBSYTEM"));

        job = floor.getNextJob();

        schedulerStateMachine.pressFloorButton(null);

        assertEquals(schedulerStateMachine.getSchedulerState(), schedulerStateMachine.getEnumState("RECEIVE_EVENT"));

        schedulerStateMachine.pressFloorButton(job); //floor

        assertEquals(schedulerStateMachine.getSchedulerState(), schedulerStateMachine.getEnumState("SEND_EVENT"));

        job = schedulerStateMachine.pressFloorButton(null); //elevator

        assertEquals(schedulerStateMachine.getSchedulerState(), schedulerStateMachine.getEnumState("WAIT_FOR_FLOORSUBSYTEM"));
    }

    @Test
    public void elevatorStateTest(){
        Job job;

        Scheduler scheduler = new Scheduler(1, 200);
        SchedulerStateMachine schedulerStateMachine = new SchedulerStateMachine(scheduler);

        //ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(1, 1, scheduler, schedulerStateMachine);
        //ElevatorSubsystemStateMachine elevatorStateMachine = new ElevatorSubsystemStateMachine(elevatorSubsystem);

        //assertEquals(elevatorStateMachine.getState(), elevatorStateMachine.getHashState("ReceiveNewTask"));


    }
}
