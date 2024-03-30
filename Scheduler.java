import javax.xml.stream.XMLInputFactory;
import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;

public class Scheduler implements Runnable{
    private boolean empty = true;
    private ArrayList<Job> jobList= new ArrayList<>();
    public   ArrayList<Elevator> elevatorsList= new ArrayList<>();
    private int MAX_SIZE, TOTAL_ELEVATORS;
    private boolean endProgram = false;
    private boolean floorProgram = false;
    private boolean elevatorProgram = false;
    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket floorComSocket, elevatorComSocket;


    //Constructor for class Scheduler
    public Scheduler(int maxJobs, int numElevators, int elevatorComPort, int floorComPort) {
        this.jobList = new ArrayList<Job>(maxJobs);
        this.MAX_SIZE = maxJobs;
        this.TOTAL_ELEVATORS = numElevators;
        try {
            this.elevatorComSocket = new DatagramSocket(elevatorComPort);
            this.floorComSocket = new DatagramSocket(floorComPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void printThreadInfo(){
        System.out.printf("\033[45m\033[1;30m%s - %s:\033[0m ", new Timestamp(System.currentTimeMillis()), Thread.currentThread().getName());
    }

    public synchronized void put(Job newJob) {
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        if (this.jobList.size() < MAX_SIZE){
            this.jobList.add(newJob);
        }

        if (newJob == null) {
            setFloorProgram(true);
            setElevatorProgram(true);
        }

        empty = jobList.isEmpty();
        notifyAll();
    }
    public synchronized Job get() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        //printJobList();
        // Mark the box as empty.
        //Job returnedJob = jobList.get(0); //save the first // we just use remove below
        Job returnedJob = jobList.remove(0);
        empty = jobList.isEmpty();
        notifyAll();
        return returnedJob;
    }

    /**
     * receiveAndSendElevator for elevator list exchange with the elevator subsystem program
     */
    public void receiveAndSendElevator(){
        byte data[] = new byte[1024*TOTAL_ELEVATORS];
        receivePacket = new DatagramPacket(data, data.length);
        try {
            //System.out.println("Listening");
            elevatorComSocket.receive(receivePacket);
            //System.out.println("Received");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ObjectInputStream iStream = null;
        try {
            iStream = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData()));
            elevatorsList = (ArrayList<Elevator>) iStream.readObject();
            iStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Print received data
        printThreadInfo();
        System.out.println("\033[0;36m\nElevator Packet received:");
        System.out.println("From host: " + receivePacket.getAddress());
        System.out.println("Host port: " + receivePacket.getPort());
        int len = receivePacket.getLength();
        System.out.println("Length: " + len);
        System.out.print("Containing: ");
        // Form a String from the byte array.
        String received = new String(data, 0, len);
        System.out.println(received);

        System.out.println("BYTES:");
        for (int i = 0; i < len; i++) {
            System.out.print((byte) data[i] + " ");
        }
        System.out.println("\033[0m");

        if (!empty) {
            assignJob();
        }
        else {
            //byte[] message = {0, 1, 0, 1};
            //System.out.println("empty job");
        }

        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = null;
        try {
            oo = new ObjectOutputStream(bStream);
            oo.writeObject(elevatorsList);
            oo.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte msg[] = bStream.toByteArray();

        //Sending packet
        try {
            sendPacket = new DatagramPacket(msg, msg.length,
                    InetAddress.getLocalHost(), receivePacket.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Print sending data
        printThreadInfo();
        System.out.println("\033[0;36m\nSending Elevator Packet:");
        System.out.println("From host: " + sendPacket.getAddress());
        System.out.println("Host port: " + sendPacket.getPort());
        len = sendPacket.getLength();
        System.out.println("Length: " + len);
        System.out.print("Containing: ");
        // Form a String from the byte array.
        String sending = new String(msg, 0, len);
        System.out.println(sending);

        System.out.println("BYTES:");
        for (int i = 0; i < len; i++) {
            System.out.print((byte) msg[i] + " ");
        }
        System.out.println("\033[0m");

        try {
            //System.out.println("Sending packet back to elevator");
            elevatorComSocket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void receiveAndSendFloor(){
        byte data[] = new byte[1024*MAX_SIZE];
        DatagramPacket receivePacketF = new DatagramPacket(data, data.length);
        try {
            //System.out.println("Scheduler Floor Listening");
            floorComSocket.receive(receivePacketF);
            //System.out.println("Scheduler Floor Received");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ObjectInputStream iStream = null;
        Job newJob = null;
        try {
            iStream = new ObjectInputStream(new ByteArrayInputStream(receivePacketF.getData()));
            newJob = (Job) iStream.readObject();
            iStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Print received data
        printThreadInfo();
        System.out.println("\033[0;33m\nFloor Packet received:");
        System.out.println("From host: " + receivePacketF.getAddress());
        System.out.println("Host port: " + receivePacketF.getPort());
        int len = receivePacketF.getLength();
        System.out.println("Length: " + len);
        System.out.print("Containing: ");
        // Form a String from the byte array.
        String received = new String(data, 0, len);
        System.out.println(received);

        System.out.println("BYTES:");
        for (int i = 0; i < len; i++) {
            System.out.print((byte) data[i] + " ");
        }
        System.out.println("\033[0m");

        put(newJob);

        /*
        else {
            //byte[] message = {0, 1, 0, 1};
            System.out.println("empty job");
        }

         */

        byte msg[] =  {0, 1};

        //Sending packet
        DatagramPacket sendPacketF = null;
        try {
            sendPacketF = new DatagramPacket(msg, msg.length,
                    InetAddress.getLocalHost(), receivePacketF.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Print sending data
        printThreadInfo();
        System.out.println("\033[0;33m\nSending Ack to Floor Packet:");
        System.out.println("From host: " + sendPacketF.getAddress());
        System.out.println("Host port: " + sendPacketF.getPort());
        len = sendPacketF.getLength();
        System.out.println("Length: " + len);
        System.out.print("Containing: ");
        // Form a String from the byte array.
        String sending = new String(msg, 0, len);
        System.out.println(sending);

        System.out.println("BYTES:");
        for (int i = 0; i < len; i++) {
            System.out.print((byte) msg[i] + " ");
        }
        System.out.println("\033[0m");

        try {
            System.out.println("Sending packet back to floor");
            floorComSocket.send(sendPacketF);
            System.out.println("sent ack to floor");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //This method assigns the jobs for the list of elevators
    public boolean assignJob() {
        boolean assignjob = false;
        for (Elevator i : elevatorsList) {
            if (i.isIdle()) {
                Job job = get();
                System.out.println("FLOOR: assigned a job: "+jobList.size());
                i.setJob(job); //jobList.remove(0)
                empty = jobList.isEmpty();
                assignjob = true;
                if (empty){return assignjob;}
            }
        }
        return assignjob;
    }


    public void putElevators(ArrayList<Elevator> elevatorList) {
        this.elevatorsList = elevatorList;
    }
    public synchronized ArrayList<Elevator> getElevators() {
        return this.elevatorsList;
    }


    public boolean getProgramStatus(){
        return this.endProgram;
    }

    public void info(){
        if (!endProgram){
            System.out.println("program is running");
        }
        else{
            System.out.println("Program is offline");
        }

        System.out.println("Max Job list Size: "+ MAX_SIZE + "\n");
        //System.out.println(jobList);
    }

    //This method prints a list of Jobs
    private void printJobList(){
        for (int i = 0; i < MAX_SIZE; i++) {
            try {
                System.out.print(jobList.get(i)+" -> ");
            }catch (IndexOutOfBoundsException e)
            {
                System.out.print("NULL -> ");
            }
        }
        System.out.print("END\n");
    }

    public ArrayList<Job> getJobList() {
        return jobList;
    }

    @Override
    public void run() {
        Thread floorSideThread = new Thread("floorSideThread"){
            @Override
            public void run(){
                while(true){
                    receiveAndSendFloor();
                }

            }
        };
        Thread elevatorSideThread = new Thread("elevatorSideThread"){
            @Override
            public void run() {
                while (true){
                    receiveAndSendElevator();
                }
            }
        };
        floorSideThread.start();
        elevatorSideThread.start();


        /*while(!endProgram){
            if (elevatorProgram){
                this.endProgram = true;
            }
        }
        System.out.println("Scheduler ended");*/
    }

    public boolean isEmpty() { return empty; }

    public void setFloorProgram(boolean floorProgram) {
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Set End Floor");
        this.floorProgram = floorProgram;
    }

    public void setElevatorProgram(boolean elevatorProgram) {
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Set End Elevator");
        this.elevatorProgram = elevatorProgram;
    }

    public boolean isElevatorProgram() {
        return elevatorProgram;
    }

    public void notified(Elevator elevator){
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Scheduler is notified that elevator " + elevator.getId() + " is at floor " + elevator.getCurrentFloor());
    }

    //This method delegates the current Job
    public synchronized int delegateTask(Job currentJob) {
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName()+": Delegating Job: Received @"+currentJob.getTimeStamp()+" for floor #"+currentJob.getPickupFloor()+" Pressed the Button "+currentJob.getButton() + " going to " + currentJob.getDestinationFloor());

        boolean goingUp = currentJob.getPickupFloor() < currentJob.getDestinationFloor();

        int min = 1000;
        int index = 0;




        for (int i = 0; i < elevatorsList.size(); i++) {
            if (!elevatorsList.get(i).isIdle()) {
                if (elevatorsList.get(i).getCurrentFloor() <= currentJob.getPickupFloor() && goingUp && elevatorsList.get(i).isGoingUp()) {
                    if (Math.abs(elevatorsList.get(i).getCurrentFloor() - currentJob.getPickupFloor()) < min) {
                        min = Math.abs(elevatorsList.get(i).getCurrentFloor() - currentJob.getPickupFloor());
                        index = i;
                    }
                } else if (elevatorsList.get(i).getCurrentFloor() >= currentJob.getPickupFloor() && !goingUp && !elevatorsList.get(i).isGoingUp()) {
                    if (Math.abs(elevatorsList.get(i).getCurrentFloor() - currentJob.getPickupFloor()) < min) {
                        min = Math.abs(elevatorsList.get(i).getCurrentFloor() - currentJob.getPickupFloor());
                        index = i;
                    }
                }
            }
            else {
                if (Math.abs(elevatorsList.get(i).getCurrentFloor() - currentJob.getPickupFloor()) < min) {
                    min = Math.abs(elevatorsList.get(i).getCurrentFloor() - currentJob.getPickupFloor());
                    index = i;
                }
            }
        }

        return index;
    }

    public static void main(String[] args) {
        int MAX_JOB, NUM_ELEVATOR, SCHEDULER_PORTF, SCHEDULER_PORTE;
        try {
            FileInputStream propsInput = new FileInputStream("config.properties");
            Properties prop = new Properties();
            prop.load(propsInput);

            MAX_JOB = Integer.parseInt(prop.getProperty("MAX_JOB"));
            NUM_ELEVATOR = Integer.parseInt(prop.getProperty("NUM_ELEVATOR"));
            SCHEDULER_PORTF = Integer.parseInt(prop.getProperty("SCHEDULER_PORTF"));
            SCHEDULER_PORTE = Integer.parseInt(prop.getProperty("SCHEDULER_PORTE"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Output to check and display info at the start of program
        System.out.println("\033[1;96mSCHEDULER \n\n\033[1;32mCONFIG FILE Input:");
        System.out.println("\033[1;34mMAX Job: \033[0m" + MAX_JOB);
        System.out.println("\033[1;34mTotal Elevators: \033[0m" + NUM_ELEVATOR);
        System.out.println("\033[1;34mScheduler Elevator Port: \033[0m" + SCHEDULER_PORTE);
        System.out.println("\033[1;34mScheduler Floor Port: \033[0m" + SCHEDULER_PORTF);
        System.out.println();

        Scheduler scheduler = new Scheduler(MAX_JOB, NUM_ELEVATOR, SCHEDULER_PORTE, SCHEDULER_PORTF);
        Thread Scheduler = new Thread(scheduler, "Scheduler Thread");
        Scheduler.start();
    }
}
