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




    //Constructor for FloorSubsystem
    FloorSubsystem(int numOfFloors, int port, int schedulerFPort) {
        //this.scheduler = scheduler;
        //this.schedulerStateMachine = schedulerStateMachine;
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
    }
    public void printThreadInfo(){
        System.out.printf("\033[45m\033[1;30m%s - %s:\033[0m ", new Timestamp(System.currentTimeMillis()), Thread.currentThread().getName());
    }

    //This method parses the whole file containing all the jobs
    private String readFile() {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            //do nothing
        }
        return line;
    }

    //This method is used to get the next Job in the Job list
    public Job getNextJob() {
        String raw = readFile();

        if (raw != null) {
            String[] rawSplit = raw.split(" ");
            job = new Job(rawSplit[0], Integer.parseInt(rawSplit[3]), Integer.parseInt(rawSplit[1]), rawSplit[2], Integer.parseInt(rawSplit[4]));
            int secs = convertToSecs(rawSplit[0]);
            jobsMap.put(secs, job);
            timestampsInSecsArr.add(secs);
        } else {
            job = null;
            printThreadInfo();
            System.out.println("End of Input File");
        }
        return job;
    }

    //This method is used to send DatagramPackets to the Scheduler
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

    public String jobRequest() {
        String info;
        info = this.readFile();
        return info;
    }

    public synchronized void run() {
        //while (!scheduler.getProgramStatus() && !scheduler.isElevatorProgram()) {
        Job newJob = getNextJob();
        while(newJob != null){
//            sendPacket(newJob);
            // this.elevatorsList = scheduler.getElevators();
            // System.out.println("\n" + System.currentTimeMillis() + " - " + Thread.currentThread().getName() + ": ------ Floor Elevator Information -----");
            //for (Elevator e : elevatorsList) {
            //    System.out.println(System.currentTimeMillis() + " - " + Thread.currentThread().getName() + ": Elevator " + e.getId() + " is currently @ floor# " + e.getCurrentFloor());
            //}
            //System.out.println(System.currentTimeMillis() + " - " + Thread.currentThread().getName() + ": ------ End Information -----\n");
//            if (scheduler.isEmpty()) {
//                this.job = getNextJob();
//                if (this.job != null) {
//                    System.out.println(System.currentTimeMillis() + " - " + Thread.currentThread().getName() + ": Sending Job @" + job.getTimeStamp() + " for floor #" + job.getPickupFloor() + " Pressed the Button " + job.getButton() + " going to " + job.getDestinationFloor());
////                    scheduler.put(job);
//                }
//                schedulerStateMachine.pressFloorButton(job);
//            }
            try {
                Thread.sleep(1500);
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

    /*
SchedulerStateMachine stateMachine= new SchedulerStateMachine(scheduler);
        FloorSubsystem floor = new FloorSubsystem(3,scheduler,stateMachine);
        floor.sendPacket();

        //info = floor.readFile();
        //System.out.println(info);
    }


}

     */
}