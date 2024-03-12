public class SchedulerStateMachine{
    Scheduler scheduler;
    private enum SchedulerState {
        //WAIT_FOR_FLOORSUBSYTEM,
        RECEIVE_EVENT,
        SEND_EVENT,
    }

    private SchedulerState currentState;

    public SchedulerStateMachine(Scheduler scheduler){
        this.scheduler = scheduler;
        currentState = SchedulerState.RECEIVE_EVENT;
    }

    public Job pressFloorButton(Job job){
        Job getJob = null;
        switch(currentState){
//            case WAIT_FOR_FLOORSUBSYTEM:
//                currentState = SchedulerState.RECEIVE_EVENT;
//                System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName()+": Scheduler is ready to receive job from floor subsystem");
//                break;
            case RECEIVE_EVENT:
                currentState = SchedulerState.SEND_EVENT;
                scheduler.put(job);
                //System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName()+": Scheduler is ready to send job to elevator subsystem");
                break;
        }

        if(getJob != null){
            return getJob;
        }

        return null;

    }

    public Job sendTask() {
        Job getJob = null;

        if (currentState == SchedulerState.SEND_EVENT) {
            currentState = SchedulerState.RECEIVE_EVENT;
            getJob = scheduler.get();
            //System.out.println(System.currentTimeMillis()+ " - " +Thread.currentThread().getName()+": Scheduler is waiting for job request from floor subsystem");
        }

        return getJob;
    }

    public SchedulerState getSchedulerState(){
        return SchedulerState.valueOf(String.valueOf(currentState));
    }

    public SchedulerState getEnumState(String index){
        return SchedulerState.valueOf(index);
    }
}
