package ink.codflow.sync.thread;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractAsyncScheduledExcutor extends AbstractThreadPool {

    ScheduledTaskConductor conductor = new ScheduledTaskConductor(taskDefineName);

    Map<String, AsyncSchedTask> taskMap = new ConcurrentHashMap<String, AsyncSchedTask>();

    public AbstractAsyncScheduledExcutor(String name) {
        super(name);
    }

    boolean submit(AsyncSchedTask task) {

        String taskId = task.getTaskId();
        taskMap.put(taskId, task);
        return false;

    }

    class AsynchronousScheduledTaskStateListener {

        public void stateChange(String id, TaskStatus state) {
            switch (state) {
            case DONE:
                doRemove(id);
                break;
            default:
                break;
            }

        }

        public void doRemove(String id) {
            taskMap.remove(id);
        }
    }

    void doInvoke(String uuid) {

        AsyncSchedTask asyncSchedTask = this.taskMap.get(uuid);
        submit(asyncSchedTask);
    }
    
    void  forceUpdateTimeStamp(AsyncSchedTask asyncSchedTask,long timestamp){
        asyncSchedTask.setLastInvokeTimestamp(timestamp);
    }

    class TaskViewIterator {
        
        AbstractAsyncScheduledExcutor excutor = AbstractAsyncScheduledExcutor.this;
        
        Iterator<Entry<String, AsyncSchedTask>> it = taskMap.entrySet().iterator();

        public boolean hasNext() {
            return it.hasNext();
        }

        public AsyncSchedTask next() {
            Entry<String, AsyncSchedTask> entry = it.next();
            return entry != null ? entry.getValue() : null;
        }
        
        public void invoke(String uuid){
            excutor.doInvoke(uuid);
        }
        
    }
    
    
    void start() {
        
        
        
        
    }
    

}
