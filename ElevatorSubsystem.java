/** ElevatorSubsystem.java
 * This subsystem creates threads for each elevator (depending on the total elevators inputted)
 * The summary of all elevators is saved in an Arraylist.
 * ElevatorSubsystem communicates with scheduler by sending the elevator list through UDP.
 * Then it receives the updated list back from scheduler
 *
 * @authors Sami Mnif, Muaadh Ali
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Properties;

import static java.lang.Math.abs;

public class ElevatorSubsystem implements Runnable {
    //private Scheduler scheduler;
    //private Elevator elevator;
    private SchedulerStateMachine schedulerStateMachine;
    public ArrayList<Elevator> elevatorsList= new ArrayList<>();

    private Elevator currentElevator;
    public Job currentJob;
    DatagramPacket sendPacket, receivePacket;
    DatagramSocket sendReceiveSocket;
    private int SCHEDULER_PORT, TOTAL_ELEVATORS;
    private Thread[] listEleThreads;


    /**
     * Constructs an ElevatorSubsystem with the specified parameters.
     *
     * @param numElevators     the number of elevators in the subsystem
     * @param elevatorCapacity the capacity of each elevator
     * @param numFloors        the total number of floors in the building
     * @param elevatorPort     the port number for elevator communication
     * @param schedulerPort    the port number for scheduler communication
     */
    public ElevatorSubsystem(int numElevators, int elevatorCapacity, int numFloors, int elevatorPort, int schedulerPort){
        //this.scheduler = scheduler;
        this.SCHEDULER_PORT = schedulerPort;
        this.TOTAL_ELEVATORS = numElevators;
        this.schedulerStateMachine = schedulerStateMachine;
        listEleThreads = new Thread[numElevators];


        this.elevatorsList= new ArrayList<Elevator>(numElevators);
        for (int i = 0; i < numElevators; i++) {
            Elevator elevator = new Elevator(i+1, numFloors, elevatorCapacity);
            this.elevatorsList.add(elevator);
            listEleThreads[i] = new Thread(elevator, "Elevator "+ (i+1));
        }
        //scheduler.putElevators(elevatorsList); //This got removed
        System.out.println("List of Elevators: "+elevatorsList);

        try {
            sendReceiveSocket = new DatagramSocket(elevatorPort);
        } catch (SocketException se) {   // Can't create the socket.
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * sendReceiveElevators send ArrayList of elevators to Scheduler
     * and then listens for an updated list from the scheduler.
     *
     * The updated list is used to update the Elevators threads with any assigned JOB.
     */
    public void sendReceiveElevators(){
        //preparing packet
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
                    InetAddress.getLocalHost(), SCHEDULER_PORT);

            //System.out.println(Thread.currentThread().getName()+ ": Sending Packet, message length "+ msg.length);
            sendReceiveSocket.send(sendPacket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //Receive packet
        byte data[] = new byte[2024*TOTAL_ELEVATORS];
        receivePacket = new DatagramPacket(data, data.length);

        try {
            //System.out.println(Thread.currentThread().getName()+ ": Listening to Packet");
            // Block until a datagram is received via sendReceiveSocket.
            sendReceiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // convert data to arrayList

        ObjectInputStream iStream = null;
        ArrayList<Elevator> listReceived  = null;
        try {
            iStream = new ObjectInputStream(new ByteArrayInputStream(data));
            listReceived = (ArrayList<Elevator>) iStream.readObject();
            iStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        updateElevators(listReceived);
    }

    /**
     * Updates the state of elevators in the subsystem based on the provided list of elevators.
     * If an elevator in the subsystem is idle and has the same ID as an elevator in the provided list,
     * it updates the elevator's job and triggers a transient fault when necessary.
     *
     * @param eList the list of elevators to update from
     */
    public void updateElevators(ArrayList<Elevator> eList){
        for (int i = 0; i < elevatorsList.size(); i++) {
            Elevator e1 = elevatorsList.get(i);
            Elevator e2 = eList.get(i);
            if (e1.getId() == e2.getId() && e1.isIdle()){
                Job job = e2.getCurrentJob();
                e1.setJob(job);
                if(job != null && job.getFault() == 1){
                    e1.triggerTransientFault();
                }
            }
        }
    }

    /**
     * Receives a new task from the scheduler state machine and assigns it as the current job for the subsystem.
     */
    public void receiveNewTask() { this.currentJob = schedulerStateMachine.sendTask(); }


//    protected synchronized void delegateTask() { //choose the best elevator to give task to, currently just giving to first elevator
//        scheduler.putElevators(elevatorsList);
//        int i = scheduler.delegateTask(currentJob);
//
//        currentElevator = elevatorsList.get(i);
//
//        currentElevator.setIdle(false);
//        currentElevator.setGoingUp(currentJob.getPickupFloor() > currentElevator.getCurrentFloor());
//        logElevatorGoingUp(currentElevator, currentJob.getPickupFloor());
//        currentElevator.goToFloor(currentJob.getPickupFloor());
//
//        currentElevator.setGoingUp(currentJob.getDestinationFloor() > currentElevator.getCurrentFloor());
//        logElevatorGoingUp(currentElevator, currentJob.getDestinationFloor());
//        currentElevator.goToFloor(currentJob.getDestinationFloor());
//        currentElevator.setIdle(true);
//
//        scheduler.putElevators(elevatorsList);
//    }

    /**
     * Logs the direction of movement of the elevator.
     * If the elevator is already at the specified floor, a message indicating this is logged.
     *
     * @param e the elevator
     * @param f the floor
     */
    private void logElevatorGoingUp(Elevator e, int f) {
        if (e.getCurrentFloor() == f) {
            System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName()+": Elevator "+currentElevator.getId()+ " is ALREADY at floor #"+f);
            return;
        }
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName()+": Elevator "+currentElevator.getId()+ (e.isGoingUp()? " is going UP to floor #" : " is going DOWN to floor #")+f);
    }

    //This method notifies the scheduler that the elevator has arrived at its destination

//    protected void notifyScheduler() {
//        //do something here with the scheduler to notify it that the elevator has arrived at its destination
//        scheduler.notified(currentElevator);
//    }


//    protected boolean getProgramStatus() {
//        return scheduler.isElevatorProgram();
//    }

//    protected void setProgramStatus(boolean status) {
//        scheduler.setElevatorProgram(status);
//    }

    //RUN
    @Override
    public void run() {
        for (int i = 0; i < listEleThreads.length; i++) {
            listEleThreads[i].start();
        }
        while (true){
            sendReceiveElevators();
            try{
                Thread.sleep(500);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }


    }

    public static void main(String[] args) {
        int NUM_ELEVATOR, NUM_FLOORS, ELEVATOR_PORT, SCHEDULER_PORTE, ELEVATOR_CAPACITY;
        try {
            FileInputStream propsInput = new FileInputStream("config.properties");
            Properties prop = new Properties();
            prop.load(propsInput);

            NUM_ELEVATOR = Integer.parseInt(prop.getProperty("NUM_ELEVATOR"));
            NUM_FLOORS = Integer.parseInt(prop.getProperty("NUM_FLOORS"));
            ELEVATOR_PORT = Integer.parseInt(prop.getProperty("ELEVATOR_PORT"));
            SCHEDULER_PORTE = Integer.parseInt(prop.getProperty("SCHEDULER_PORTE"));
            ELEVATOR_CAPACITY = Integer.parseInt(prop.getProperty("ELEVATOR_CAPACITY"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Output to check and display info at the start of program
        System.out.println("\033[1;96mELEVATOR SUBSYSTEM \n\n\033[1;32mCONFIG FILE Input:");
        System.out.println("\033[1;34mTotal Elevators: \033[0m" + NUM_ELEVATOR);
        System.out.println("\033[1;34mTotal Floors: \033[0m" + NUM_FLOORS);
        System.out.println("\033[1;34mElevator Port: \033[0m" + ELEVATOR_PORT);
        System.out.println("\033[1;34mScheduler Elevator Port: \033[0m" + SCHEDULER_PORTE);
        System.out.println("\033[1;34mElevator Capacity: \033[0m" + ELEVATOR_CAPACITY);
        System.out.println();

        Thread Elevator = new Thread(new ElevatorSubsystem(NUM_ELEVATOR, ELEVATOR_CAPACITY, NUM_FLOORS, ELEVATOR_PORT, SCHEDULER_PORTE), "ElevatorSub");
        Elevator.start();
    }
}
