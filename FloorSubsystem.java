import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class FloorSubsystem implements Runnable {
    Scheduler scheduler;
    SchedulerStateMachine schedulerStateMachine;
    BufferedReader reader;
    Job job = new Job(null, 0, 0, null);
    ArrayList<Floor> floorsArrayList = new ArrayList<Floor>();
    private ArrayList<Elevator> elevatorsList;
    DatagramSocket sendSocket, receiveSocket;
    DatagramPacket sendPacket, receivePacket;
    private int SCHEDULERF_PORT, SCHEDULER_PORT;

    FloorSubsystem(int numOfFloors, Scheduler scheduler, SchedulerStateMachine schedulerStateMachine, int port, int schedulerFPort) {
        this.scheduler = scheduler;
        this.schedulerStateMachine = schedulerStateMachine;
        this.SCHEDULERF_PORT = schedulerFPort;
        this.SCHEDULER_PORT = schedulerFPort;
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

    private String readFile() {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            //do nothing
        }
        return line;
    }

    public Job getNextJob() {
        String raw = readFile();

        if (raw != null) {
            String[] rawSplit = raw.split(" ");

            job.setTimeStamp(rawSplit[0]);
            job.setPickupFloor(Integer.valueOf(rawSplit[1]));
            job.setButton(rawSplit[2]);
            job.setDestinationFloor(Integer.valueOf(rawSplit[3]));
        } else {
            job = null;
        }
        return job;
    }

    public void sendPacket() {

       // byte[] dataArray = jobRequest().getBytes();

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = null;
        try {
            oo = new ObjectOutputStream(bStream);
            oo.writeObject(getNextJob());
            oo.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte msg[] = bStream.toByteArray();


        System.out.println("\nFloor: Sending packet...\n");

        try {
            sendPacket = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), SCHEDULERF_PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Sending from address " + sendPacket.getAddress());
        System.out.println("Destination host port: " + sendPacket.getPort());
        System.out.println("Sending with length " + sendPacket.getLength());
        System.out.println("With message: " + new String(sendPacket.getData()));
        System.out.println("Message in bytes: " + msg);

        try {
            sendSocket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nFloor: Packet sent.\n");

        byte data[] = new byte[4];
        receivePacket = new DatagramPacket(data, data.length);

        try {
            // Block until a datagram is received via receiveSocket.
            receiveSocket.receive(receivePacket);
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
        while (!scheduler.getProgramStatus() && !scheduler.isElevatorProgram()) {
            this.elevatorsList = scheduler.getElevators();
            System.out.println("\n" + System.currentTimeMillis() + " - " + Thread.currentThread().getName() + ": ------ Floor Elevator Information -----");
            for (Elevator e : elevatorsList) {
                System.out.println(System.currentTimeMillis() + " - " + Thread.currentThread().getName() + ": Elevator " + e.getId() + " is currently @ floor# " + e.getCurrentFloor());
            }
            System.out.println(System.currentTimeMillis() + " - " + Thread.currentThread().getName() + ": ------ End Information -----\n");
            if (scheduler.isEmpty()) {
                this.job = getNextJob();
                if (this.job != null) {
                    System.out.println(System.currentTimeMillis() + " - " + Thread.currentThread().getName() + ": Sending Job @" + job.getTimeStamp() + " for floor #" + job.getPickupFloor() + " Pressed the Button " + job.getButton() + " going to " + job.getDestinationFloor());
//                    scheduler.put(job);
                }
                schedulerStateMachine.pressFloorButton(job);
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(System.currentTimeMillis() + " - " + Thread.currentThread().getName() + ": Floor Subsystem Job ended");
    }


    /*
    public static void main(String[] args) {
      //  String info;
       Scheduler scheduler = new Scheduler(4, );
//
//        FloorSubsystem floor = new FloorSubsystem(1,scheduler);
//



SchedulerStateMachine stateMachine= new SchedulerStateMachine(scheduler);
        FloorSubsystem floor = new FloorSubsystem(3,scheduler,stateMachine);
        floor.sendPacket();

        //info = floor.readFile();
        //System.out.println(info);
    }


}

     */
}
