import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTests {
    @Test
    public void checkReadInputFileTest(){
        Job job;

        Scheduler scheduler = new Scheduler(1);
        FloorSubsystem floor = new FloorSubsystem(1, scheduler);

        job = floor.getNextJob();

        assertEquals(0, job.getElevatorID());
        assertEquals("14:05:15", job.getTimeStamp());
        assertEquals(2, job.getFloor());
        assertEquals("Up", job.getButton());
    }

    @Test
    public void passDataToSchedulerTest(){
        Job job;

        Scheduler scheduler = new Scheduler(1);
        FloorSubsystem floor = new FloorSubsystem(1, scheduler);

        job = floor.getNextJob();

        scheduler.put(job);

        ArrayList<Job> jobs = scheduler.getJobList();

        assertEquals(job.getElevatorID(), jobs.get(0).getElevatorID());
        assertEquals(job.getFloor(), jobs.get(0).getFloor());
        assertEquals(job.getButton(), jobs.get(0).getButton());
        assertEquals(job.getTimeStamp(), jobs.get(0).getTimeStamp());
    }

    @Test
    public void getDataFromSchedulerTest(){
        Job job;

        Scheduler scheduler = new Scheduler(1);
        FloorSubsystem floor = new FloorSubsystem(1, scheduler);

        job = floor.getNextJob();

        scheduler.put(job);

        Job newJob = scheduler.get();

        assertEquals(job.getElevatorID(), newJob.getElevatorID());
        assertEquals(job.getFloor(), newJob.getFloor());
        assertEquals(job.getButton(), newJob.getButton());
        assertEquals(job.getTimeStamp(), newJob.getTimeStamp());
    }
}
