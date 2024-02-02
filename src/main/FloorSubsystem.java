package src.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FloorSubsystem implements Runnable {
    Scheduler scheduler;
    BufferedReader reader;
    Job job;
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
        String[] rawSplit = raw.split(" ");

        job.setElevatorID(Integer.valueOf(rawSplit[0].replace(":", "")));
        job.setElevatorID(Integer.valueOf(rawSplit[1]));
        job.setFloor(Integer.valueOf(rawSplit[2]));
        job.setButton(rawSplit[3]);

        return job;
    }

    public synchronized void run () {

        while(scheduler.getProgramStatus()){
            this.job = getNextJob();
            if(this.job!= null) {
                scheduler.put(job);
            }
        }
    }



//    public static void main(String[] args) {
//        String info;
//        Scheduler scheduler = new Scheduler();
//
//        FloorSubsystem floor = new FloorSubsystem(scheduler);
//
//        info = floor.readFile();
//
//        System.out.println(info);
//
//    }

}
