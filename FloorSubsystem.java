/** FloorSubsystem.java
 * This subsystem will be reading the events.txt file and saving the jobs into a
 * Job object.
 * FloorSubsystem communicates with the scheduler by sending UDP packet with the JOB info
 *
 * @authors Muaadh Ali, Jalal Mourad, Sami Mnif
 */

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.*;



public class FloorSubsystem implements Runnable {
    //Scheduler scheduler;
    //SchedulerStateMachine schedulerStateMachine;
    BufferedReader reader;
    Job job;
    ArrayList<Floor> floorsArrayList = new ArrayList<Floor>();
    private ArrayList<Elevator> elevatorsList;
    DatagramSocket sendSocket, receiveSocket;
    DatagramPacket sendPacket, receivePacket;
    private int SCHEDULERF_PORT;

    HashMap<Integer, Job> jobsMap;
    ArrayList<Integer> timestampsInSecsArr;
    int passengersCapacity, hour, minutes, tempID, pickupF;

    /**
     * Constructor for FloorSubsystem, it will import the scheduler port and the scheduler port for UDP comms purposes
     * @param numOfFloors (Total number of floors)
     * @param port (Floor Subsystem Port for UDP comms)
     * @param schedulerFPort (Scheduler Port)
     */
    FloorSubsystem(int numOfFloors, int port, int schedulerFPort) {
        //this.scheduler = scheduler;
        //this.schedulerStateMachine = schedulerStateMachine;
        hour = 0;
        minutes= 0;
        tempID = 0;
        pickupF = 0;
        jobsMap = new HashMap<Integer, Job>();
        timestampsInSecsArr = new ArrayList<>();

        this.SCHEDULERF_PORT = schedulerFPort;

        try {
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
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
        passengersCapacity = 0;
    }

    /**
     * printThreadInfo(): prints the Thread name and the timestamp of the message on console
     */
    public void printThreadInfo(){
        System.out.printf("\033[45m\033[1;30m%s - %s:\033[0m ", new Timestamp(System.currentTimeMillis()), Thread.currentThread().getName());
    }

    /**
     * readFile(): This method parses the whole file containing all the jobs, and returns 1 line everytime its called
     * @return String line
     */
    private String readFile() {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            //do nothing
        }
        return line;
    }

    /**
     * getNextJob(): This method is used to get the next Job in the Job list. It calls the readFile() method that returns
     * a line from the events.txt file.
     * It also sums up the Jobs that are happening in the same hour and minute (Capacity)
     * and also if it is coming from the same floor the jib was requested
     *
     * @return Job object
     */
    public Job getNextJob() {
        String raw = readFile();

        if (raw != null) {
            String[] rawSplit = raw.split(" ");
            String[] timeParts = rawSplit[0].split(":");
            int secs = convertToSecs(rawSplit[0]);

            if (hour == 0 && minutes == 0){
                hour = Integer.parseInt(timeParts[0]);
                minutes = Integer.parseInt(timeParts[1]);
                pickupF = Integer.parseInt(rawSplit[1]);
                tempID = secs;

                job = new Job(rawSplit[0], Integer.parseInt(rawSplit[3]), Integer.parseInt(rawSplit[1]), rawSplit[2], Integer.parseInt(rawSplit[4]));
                jobsMap.put(secs, job);
                timestampsInSecsArr.add(secs);
            }
            else if (hour == Integer.parseInt(timeParts[0]) && minutes == Integer.parseInt(timeParts[1]) && pickupF == Integer.parseInt(rawSplit[1])){
                System.out.println(rawSplit[0]);
                jobsMap.get(tempID).capacity++;
                System.out.println(jobsMap.get(tempID).capacity);
                hour = Integer.parseInt(timeParts[0]);
                minutes = Integer.parseInt(timeParts[1]);
                pickupF = Integer.parseInt(rawSplit[1]);
            }
            else{
                hour = Integer.parseInt(timeParts[0]);
                minutes = Integer.parseInt(timeParts[1]);
                pickupF = Integer.parseInt(rawSplit[1]);
                tempID = secs;

                job = new Job(rawSplit[0], Integer.parseInt(rawSplit[3]), Integer.parseInt(rawSplit[1]), rawSplit[2], Integer.parseInt(rawSplit[4]));
                jobsMap.put(secs, job);
                timestampsInSecsArr.add(secs);
            }

        } else {
            job = null;
            printThreadInfo();
            System.out.println("End of Input File");
        }
        return job;
    }

    /**
     * sendPacket(Job newJob): This method is used to send DatagramPackets to the Scheduler
     * with the jobs, and then listen for acknowledgement from the Scheduler.
     * @param newJob (a job to send to Scheduler)
     */
    public void sendPacket(Job newJob) {

        // byte[] dataArray = jobRequest().getBytes();

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = null;
        try {
            oo = new ObjectOutputStream(bStream);
            oo.writeObject(newJob);
            oo.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte msg[] = bStream.toByteArray();

        printThreadInfo();
        System.out.println("Sending packet...");
        printThreadInfo();

        //job.getButton();
        System.out.println("button: " + newJob.getPickupFloor()  + " " + newJob.getButton());

        try {
            sendPacket = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), SCHEDULERF_PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        printThreadInfo();
        System.out.println("\033[0;36m\nSending from address " + sendPacket.getAddress());
        System.out.println("Destination host port: " + sendPacket.getPort());
        System.out.println("Sending with length " + sendPacket.getLength());
        System.out.println("With message: " + new String(sendPacket.getData()));
        System.out.println("Message in bytes: " + Arrays.toString(sendPacket.getData()));
        System.out.printf("Containing: %s\n", new String(sendPacket.getData(), 0, sendPacket.getLength()));
        System.out.print("BYTES: ");
        for (int i = 0; i < msg.length; i++) {
            System.out.print((byte) msg[i] + " ");
        }
        System.out.println("\033[0m");

        try {
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        printThreadInfo();
        System.out.println("Packet sent");

        byte data[] = new byte[1024];
        receivePacket = new DatagramPacket(data, data.length);

        try {
            // Block until a datagram is received via receiveSocket.
            sendSocket.receive(receivePacket);
            printThreadInfo();
            System.out.println("Received Ack from Scheduler");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * NOT USED
     * jobRequest(): returns a line from input file
     * @return
     */
    public String jobRequest() {
        String info;
        info = this.readFile();
        return info;
    }

    /**
     * RUN thread, this sends all the jobs from the input file, then listens for ack
     */
    public synchronized void run() {
        //while (!scheduler.getProgramStatus() && !scheduler.isElevatorProgram()) {
        Job newJob = getNextJob();
        while(newJob != null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            newJob = getNextJob();
        }
        Collections.sort(timestampsInSecsArr);
        System.out.println(timestampsInSecsArr);

        long startTime = System.currentTimeMillis();

        while(!jobsMap.isEmpty()) {
            // System.out.println((System.currentTimeMillis() - startTime)/1);
            if ((System.currentTimeMillis() - startTime)/1 >= timestampsInSecsArr.getFirst()) {
                Job job = jobsMap.remove((timestampsInSecsArr).removeFirst());
                sendPacket(job);
            }
        }
        System.out.println(timestampsInSecsArr);
        System.out.println(jobsMap);
        //System.out.println(System.currentTimeMillis() + " - " + Thread.currentThread().getName() + ": Floor Subsystem Job ended");
    }

    /**
     * MAIN function, this will read teh config file, and inputs them into constructor of the floor Subsystem
     * @param args
     */
    public static void main(String[] args) {
        int NUM_FLOORS, SCHEDULER_PORTF, FLOOR_PORT;
        try {
            FileInputStream propsInput = new FileInputStream("config.properties");
            Properties prop = new Properties();
            prop.load(propsInput);

            NUM_FLOORS = Integer.parseInt(prop.getProperty("NUM_FLOORS"));
            SCHEDULER_PORTF = Integer.parseInt(prop.getProperty("SCHEDULER_PORTF"));
            FLOOR_PORT = Integer.parseInt(prop.getProperty("FLOOR_PORT"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Output to check and display info at the start of program
        System.out.println("\033[1;96mFLOOR SUBSYSTEM \n\n\033[1;32mCONFIG FILE Input:");
        System.out.println("\033[1;34mTotal Floors: \033[0m" + NUM_FLOORS);
        System.out.println("\033[1;34mScheduler Floor Port: \033[0m" + SCHEDULER_PORTF);
        System.out.println("\033[1;34mFloor Port: \033[0m" + FLOOR_PORT);
        System.out.println();

        Thread Floor = new Thread(new FloorSubsystem(NUM_FLOORS, FLOOR_PORT, SCHEDULER_PORTF), "Floor Thread");
        Floor.start();
    }

    private int convertToSecs(String timestamp) {
        String[] nums = timestamp.split(":");

        int secs = 0;

        secs += 360 * Integer.parseInt(nums[0]);
        secs += 60 * Integer.parseInt(nums[1]);
        secs += Integer.parseInt(nums[2]);

        return secs;

    }
}