import javax.xml.stream.XMLInputFactory;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Scheduler implements Runnable{
    private boolean empty = true;
    private ArrayList<Job> jobList= new ArrayList<>();
    private ArrayList<Elevator> elevatorsList= new ArrayList<>();
    private int MAX_SIZE;
    private boolean endProgram = false;
    private boolean floorProgram = false;
    private boolean elevatorProgram = false;
    private DatagramPacket sendPacket, receivePacket;
    private DatagramSocket floorComSocket, elevatorComSocket;


    //Constructor for class Scheduler
    public Scheduler(int maxJobs, int elevatorComPort, int floorComPort) {
        this.jobList = new ArrayList<Job>(maxJobs);
        this.MAX_SIZE = maxJobs;
        try {
            this.elevatorComSocket = new DatagramSocket(elevatorComPort);
            this.floorComSocket = new DatagramSocket(floorComPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
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
        byte data[] = new byte[100];
        receivePacket = new DatagramPacket(data, data.length);
        try {
            elevatorComSocket.receive(receivePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ObjectInputStream iStream = null;
        try {
            iStream = new ObjectInputStream(new ByteArrayInputStream(data));
            elevatorsList = (ArrayList<Elevator>) iStream.readObject();
            iStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (!empty) {
            assignJob();
        }
        else {
            //byte[] message = {0, 1, 0, 1};
            System.out.println("empty job");
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

        try {
            System.out.println("Sending packet back to elevator");
            elevatorComSocket.send(sendPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void receiveAndSendFloor(){
        
    }

    //This method assigns the jobs for the list of elevators
    public boolean assignJob() {
        boolean assignjob = false;
        for (Elevator i : elevatorsList) {
            if (i.isIdle()) {
                i.setJob(jobList.remove(0));
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
        receiveAndSendElevator();
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
}
