import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FloorSubsystem implements Runnable {
    Scheduler scheduler;
    BufferedReader reader;
    Job job;
    int ID;
    FloorSubsystem(int ID,Scheduler scheduler) {
        this.scheduler = scheduler;
        this.ID = ID;
        try {
            reader = new BufferedReader(new FileReader("events.txt"));
        } catch (IOException e) {
            //do nothing
        }
        if (reader == null) {
            System.out.println("reader is null :(");
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

    public synchronized void run () {

        while(scheduler.getProgramStatus()){
            if(this.job!= null) {
                scheduler.put(job);
            }
        }

    }



    public static void main(String[] args) {
        String info;
        Scheduler scheduler = new Scheduler();

        FloorSubsystem floor = new FloorSubsystem(scheduler);

        info = floor.readFile();

        System.out.println(info);

    }

}
