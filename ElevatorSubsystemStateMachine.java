public class ElevatorSubsystemStateMachine implements Runnable {
    ElevatorSubsystem elevatorSubsystem;

    private enum ElevatorStates {
        RECEIVE_NEW_TASK,
        DELEGATE_TASK,
        NOTIFY_SCHEDULER
    }
    private ElevatorStates currentState;

    public ElevatorSubsystemStateMachine(ElevatorSubsystem elevatorSubsystem) {
        this.elevatorSubsystem = elevatorSubsystem;

        currentState = ElevatorStates.RECEIVE_NEW_TASK;
    }

    public ElevatorStates getState(){
        return currentState;
    }

    public ElevatorStates getHashState(String s) {
        return currentState;
    }

    @Override
    public void run() {
        while(!elevatorSubsystem.getProgramStatus()) {
            switch (currentState) {
                case RECEIVE_NEW_TASK:
                    elevatorSubsystem.receiveNewTask();
                    if (elevatorSubsystem.currentJob != null) {
                        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Receiving new task...");
                        currentState = ElevatorStates.DELEGATE_TASK;
                    }
                    break;
                case DELEGATE_TASK:
                    elevatorSubsystem.delegateTask();
                    System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Delegating Task...");
                    currentState = ElevatorStates.NOTIFY_SCHEDULER;
                    break;
                case NOTIFY_SCHEDULER:
                    System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Notifying scheduler...");
                    elevatorSubsystem.notifyScheduler();
                    elevatorSubsystem.currentJob = null;
                    currentState = ElevatorStates.RECEIVE_NEW_TASK;
                    break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() + " Elevator Subsystem Job ended");
    }
}
