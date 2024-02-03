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

        assertEquals(4, job.getDestinationFloor());
        assertEquals("14:05:15", job.getTimeStamp());
        assertEquals(2, job.getPickupFloor());
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

        assertEquals(job.getDestinationFloor(), jobs.get(0).getDestinationFloor());
        assertEquals(job.getPickupFloor(), jobs.get(0).getPickupFloor());
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

        assertEquals(job.getPickupFloor(), newJob.getPickupFloor());
        assertEquals(job.getDestinationFloor(), newJob.getDestinationFloor());
        assertEquals(job.getButton(), newJob.getButton());
        assertEquals(job.getTimeStamp(), newJob.getTimeStamp());
    }
}
