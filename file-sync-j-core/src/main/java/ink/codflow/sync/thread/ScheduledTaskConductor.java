package ink.codflow.sync.thread;

import ink.codflow.sync.thread.AbstractAsyncScheduledExcutor.TaskViewIterator;

public class ScheduledTaskConductor extends Thread {

    long defaultInterval = 3000;

    TaskViewIterator iterator;

    static final String CONDUCTOR_THREAD_NAME = "thread-conductor-";

    public ScheduledTaskConductor(String name) {
        super(CONDUCTOR_THREAD_NAME + name);
    }

    @Override
    public void run() {

        

    }
    
    
    void doFireTask(AsyncSchedTask task) {
        if (checkTime(task)) {
            String taskId = task.getTaskId();
            iterator.invoke(taskId);
            
        }
        
    }

    
    
    
    
    boolean checkTime(AsyncSchedTask task) {

        long latestTimestamp = task.getLastInvokeTimestamp();
        long interval = task.getCurrentPieceInterval() > 0 ? task.getCurrentPieceInterval() : defaultInterval;
        long currentTs = System.currentTimeMillis();
        
        return (currentTs - interval < latestTimestamp);

    }

    public TaskViewIterator getIterator() {
        return iterator;
    }

    public void setIterator(TaskViewIterator iterator) {
        this.iterator = iterator;
    }

}
