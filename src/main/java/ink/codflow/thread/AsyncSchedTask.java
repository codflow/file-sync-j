package ink.codflow.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.thread.AbstractAsyncScheduledExcutor.AsynchronousScheduledTaskStateListener;

public class AsyncSchedTask implements Runnable {

    String taskId;

    Lock lock;

    private static final Logger logger = LoggerFactory.getLogger(AsyncSchedTask.class);

    volatile boolean done;

    volatile int index;

    volatile long lastInvokeTimestamp;

    List<Callable<TaskStatus>> callables;

    Map<Callable<TaskStatus>, TaskExceptionHandler> rejectMap = new HashMap<>();

    AsynchronousScheduledTaskStateListener stateListener;

    @Override
    public void run() {

        Callable<TaskStatus> runnable = callables.get(index);

        boolean cond = lock.tryLock();

        if (cond) {

            try {
                updateTimeStamp();
                TaskStatus status = runnable.call();
                switch (status) {
                case SUCCESS:

                    index++;
                    break;

                case FAILED:

                    break;

                default:
                    break;
                }

                stateListener.stateChange(this.taskId, status);

            } catch (Exception e) {
                TaskExceptionHandler handler = rejectMap.get(runnable);
                if (handler != null) {
                    handler.process(e);
                } else {
                    String tName = Thread.currentThread().getName();
                    logger.error(tName + ":Exception", e);
                }
            } finally {
                lock.unlock();
            }

        }
    }

    public static AsyncSchedTask supplyAsync(Callable<TaskStatus> future) {
        AsyncSchedTask task = new AsyncSchedTask();
        task.callables.add(future);
        return task;
    }

    public AsyncSchedTask thenAsync(Callable<TaskStatus> future) {
        this.callables.add(future);
        return this;
    }

    public AsyncSchedTask rejectHandler(TaskExceptionHandler handler) {

        int currentIndex = this.callables.size();
        Callable<TaskStatus> currentFuture = callables.get(currentIndex);
        this.rejectMap.put(currentFuture, handler);
        return this;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getLastInvokeTimestamp() {
        return lastInvokeTimestamp;
    }

    public void setLastInvokeTimestamp(long lastInvokeTimestamp) {
        this.lastInvokeTimestamp = lastInvokeTimestamp;
    }

    public long getCurrentPieceInterval() {
        return 3000L;
    }

    void updateTimeStamp() {
        this.setLastInvokeTimestamp(System.currentTimeMillis());
    }
}
