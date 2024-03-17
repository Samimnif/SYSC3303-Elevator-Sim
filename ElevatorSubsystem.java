import java.io.*;
import java.net.*;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class ElevatorSubsystem implements Runnable {
    private Scheduler scheduler;
    //private Elevator elevator;
    private SchedulerStateMachine schedulerStateMachine;
    private ArrayList<Elevator> elevatorsList= new ArrayList<>();

    private Elevator currentElevator;
    public Job currentJob;
    DatagramPacket sendPacket, receivePacket;
    DatagramSocket sendReceiveSocket;
    private int SCHEDULER_PORT;


    //Constructor for class ElevatorSubsystem
    public ElevatorSubsystem(int numElevators, int numFloors, Scheduler scheduler, int elevatorPort, int schedulerPort){
        this.scheduler = scheduler;
        this.SCHEDULER_PORT = schedulerPort;
        this.schedulerStateMachine = schedulerStateMachine;

        this.elevatorsList= new ArrayList<Elevator>(numElevators);
        for (int i = 0; i < numElevators; i++) {
            Elevator elevator = new Elevator(i+1, numFloors);
            this.elevatorsList.add(elevator);
        }
        scheduler.putElevators(elevatorsList);
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

            System.out.println(Thread.currentThread().getName()+ ": Sending Packet");
            sendReceiveSocket.send(sendPacket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //Receive packet
        byte data[] = new byte[1024];
        receivePacket = new DatagramPacket(data, data.length);

        try {
            System.out.println(Thread.currentThread().getName()+ ": Listening to Packet");
            // Block until a datagram is received via sendReceiveSocket.
            sendReceiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // convert data to arrayList

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

        updateElevators(elevatorsList);

    }

    //This method updates the elevators with the current Job that we have
    public void updateElevators(ArrayList<Elevator> eList){
        for (int i = 0; i < elevatorsList.size(); i++) {
            Elevator e1 = elevatorsList.get(i);
            Elevator e2 = eList.get(i);
            if (e1.getId() == e2.getId()){
               e1.setJob(e2.getCurrentJob());
            }
            
        }
    }

    //This method recieves a ned task from the FloorSubsystem
    public void receiveNewTask() { this.currentJob = schedulerStateMachine.sendTask(); }


    protected synchronized void delegateTask() { //choose the best elevator to give task to, currently just giving to first elevator
        scheduler.putElevators(elevatorsList);
        int i = scheduler.delegateTask(currentJob);

        currentElevator = elevatorsList.get(i);

        currentElevator.setIdle(false);
        currentElevator.setGoingUp(currentJob.getPickupFloor() > currentElevator.getCurrentFloor());
        logElevatorGoingUp(currentElevator, currentJob.getPickupFloor());
        currentElevator.goToFloor(currentJob.getPickupFloor());

        currentElevator.setGoingUp(currentJob.getDestinationFloor() > currentElevator.getCurrentFloor());
        logElevatorGoingUp(currentElevator, currentJob.getDestinationFloor());
        currentElevator.goToFloor(currentJob.getDestinationFloor());
        currentElevator.setIdle(true);

        scheduler.putElevators(elevatorsList);
    }

    //This method specifies at which floor the elevator is
    private void logElevatorGoingUp(Elevator e, int f) {
        if (e.getCurrentFloor() == f) {
            System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName()+": Elevator "+currentElevator.getId()+ " is ALREADY at floor #"+f);
            return;
        }
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName()+": Elevator "+currentElevator.getId()+ (e.isGoingUp()? " is going UP to floor #" : " is going DOWN to floor #")+f);
    }

    //This method notifies the scheduler that the elevator has arrived at its destination

    protected void notifyScheduler() {
        //do something here with the scheduler to notify it that the elevator has arrived at its destination
        scheduler.notified(currentElevator);
    }


    protected boolean getProgramStatus() {
        return scheduler.isElevatorProgram();
    }

    protected void setProgramStatus(boolean status) {
        scheduler.setElevatorProgram(status);
    }

    @Override
    public void run() {
        sendReceiveElevators();

    }
//    @Override
//    public void run() {
//        scheduler.putElevators(elevatorsList);
//        Elevator serviceElevator;
//        while(!scheduler.getProgramStatus()){
//            this.currentJob =  scheduler.get();
//            if (this.currentJob != null){
//                System.out.println(Thread.currentThread().getName()+": Received Job @"+currentJob.getTimeStamp()+" for floor #"+currentJob.getPickupFloor()+" Pressed the Button "+currentJob.getButton() + " going to " + currentJob.getDestinationFloor());
//                //for now we assume one elevator is available
//                serviceElevator = elevatorsList.get(0); //Here we have to rewrite to account for multiple elevators and check which one is available to use
//                if ((currentJob.getPickupFloor() > serviceElevator.getCurrentFloor())){
//                    System.out.println(Thread.currentThread().getName()+": Elevator "+serviceElevator.getId()+" is going UP to floor #"+currentJob.getPickupFloor());
//                }else {
//                    System.out.println(Thread.currentThread().getName()+": Elevator "+serviceElevator.getId()+" is going DOWN to floor #"+currentJob.getPickupFloor());
//                }
//                serviceElevator.goToFloor(currentJob.getPickupFloor());
//                scheduler.putElevators(elevatorsList);
//                System.out.println(Thread.currentThread().getName()+": Elevator "+serviceElevator.getId()+" arrived to floor #"+currentJob.getPickupFloor() + " and now is going "+ currentJob.getButton() +" to "+currentJob.getDestinationFloor());
//
//                serviceElevator.goToFloor(currentJob.getDestinationFloor());
//                scheduler.putElevators(elevatorsList);
//                System.out.println(Thread.currentThread().getName()+": Elevator "+serviceElevator.getId()+" arrived to destination floor #"+currentJob.getDestinationFloor());
//            } else {
//                break;
//            }
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            this.currentJob = null;
//        }
//        setProgramStatus(true);
//        System.out.println(Thread.currentThread().getName() + "Elevator Subsystem Job ended");
//    }
}
