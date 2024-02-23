import java.util.HashMap;
import java.util.Map;


interface JobState {

    boolean receiveCurrentTask(ElevatorSubsystemStateMachine context);

    void delegateTask(ElevatorSubsystemStateMachine context);

    void notifyScheduler(ElevatorSubsystemStateMachine context);

}

class ReceiveNewTask implements JobState {
    public boolean receiveCurrentTask(ElevatorSubsystemStateMachine context) {
        context.elevatorSubsystem.receiveNewTask();
        if (context.elevatorSubsystem.currentJob != null) {
            System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Receiving new task...");
            context.setState("DelegateTask");

            return true;
        }
        return false;
    }

    public void delegateTask(ElevatorSubsystemStateMachine context) {
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Current Job is empty, no task to delegate.");
    }

    public void notifyScheduler(ElevatorSubsystemStateMachine context) {
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": The elevator subsystem is attempting to receive a task.");
    }
}

class DelegateCurrentTask implements JobState {
    public boolean receiveCurrentTask(ElevatorSubsystemStateMachine context) {
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Current task not yet complete, cannot receive a new task.");
        return true;
    }

    public void delegateTask(ElevatorSubsystemStateMachine context) {
        context.elevatorSubsystem.delegateTask();
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Delegating Task...");
        context.setState("NotifyScheduler");
    }

    public void notifyScheduler(ElevatorSubsystemStateMachine context) {
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": The elevator subsystem is delegating a task.");
    }
}

class NotifyScheduler implements JobState {
    public boolean receiveCurrentTask(ElevatorSubsystemStateMachine context) {
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Notifying scheduler not yet complete, cannot receive a new task.");
        return true;
    }

    public void delegateTask(ElevatorSubsystemStateMachine context) {
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Notifying scheduler not yet complete, no new task to delegate.");
    }

    public void notifyScheduler(ElevatorSubsystemStateMachine context) {
        System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName() +": Notifying scheduler...");
        context.elevatorSubsystem.notifyScheduler();
        context.elevatorSubsystem.currentJob = null;
        context.setState("ReceiveNewTask");
    }
}

public class ElevatorSubsystemStateMachine implements Runnable {
    ElevatorSubsystem elevatorSubsystem;

    private Map<String, JobState> states;
    private JobState currentState;

    public ElevatorSubsystemStateMachine(ElevatorSubsystem elevatorSubsystem) {
        states = new HashMap<>();
        this.elevatorSubsystem = elevatorSubsystem;

        addState("ReceiveNewTask", new ReceiveNewTask());
        addState("DelegateTask", new DelegateCurrentTask());
        addState("NotifyScheduler", new NotifyScheduler());

        setState("ReceiveNewTask");
    }

    protected void setState(String stateName) {
        currentState = states.get(stateName);
    }

    private void addState(String stateName, JobState state) {
        states.put(stateName, state);
    }



    @Override
    public void run() {
        while(!elevatorSubsystem.getProgramStatus()) {
            if (elevatorSubsystem.currentJob == null) {
                currentState.receiveCurrentTask(this);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (elevatorSubsystem.currentJob != null) {
                currentState.delegateTask(this);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (elevatorSubsystem.currentJob != null) {
                currentState.notifyScheduler(this);
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
