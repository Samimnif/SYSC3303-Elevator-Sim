import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class FloorSubsystem implements Runnable {
    Scheduler scheduler;
    BufferedReader reader;
    Job job = new Job(null,0,0,null);
    ArrayList<Floor> floorsArrayList = new ArrayList<Floor>();
    FloorSubsystem(int numOfFloors, Scheduler scheduler) {
        this.scheduler = scheduler;
        try {
            reader = new BufferedReader(new FileReader("events.txt"));
        } catch (IOException e) {
        }
        if (reader == null) {
            System.out.println("reader is null :(");
        }

        for (int i = 0; i < numOfFloors; i++) {
            Floor floor = new Floor(i + 1);
            floorsArrayList.add(floor);
        }
    }

    private String readFile () {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            //do nothing
        }
        return line;
    }

    private Job getNextJob() {
        String raw = readFile();

        if (raw != null) {
            String[] rawSplit = raw.split(" ");

            job.setTimeStamp(rawSplit[0]);
            job.setElevatorID(Integer.valueOf(rawSplit[1]));
            job.setFloor(Integer.valueOf(rawSplit[2]));
            job.setButton(rawSplit[3]);
        }
        else{
            job = null;
        }
        return job;
    }

    public synchronized void run () {
        while(!scheduler.getProgramStatus()){
            this.job = getNextJob();
            if(this.job!= null) {
                System.out.println(Thread.currentThread().getName()+": Sending Job @"+job.getTimeStamp()+" for floor #"+job.getFloor()+" Pressed the Button "+job.getButton());
                scheduler.put(job);
            }
            else{
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //this.job = null;
        }
        System.out.println(Thread.currentThread().getName()+": Floor Subsystem Job ended");
    }

/*
    public static void main(String[] args) {
        String info;
        Scheduler scheduler = new Scheduler(4);

        FloorSubsystem floor = new FloorSubsystem(1,scheduler);

        info = floor.readFile();

        System.out.println(info);

    }

 */
}
