package ink.codflow.sync.manager;

import java.util.ArrayList;
import java.util.List;



public class TaskBO {

    
    List<WorkerTaskBO> workerTasklist = new ArrayList<WorkerTaskBO>();
    
    
    
    public  void addWorkerTask(final WorkerTaskBO workerTaskBO) {
        workerTasklist.add(workerTaskBO);
    }

    /**
     * @return the workerTasklist
     */
    public List<WorkerTaskBO> getWorkerTasklist() {
        return workerTasklist;
    }

    /**
     * @param workerTasklist the workerTasklist to set
     */
    public void setWorkerTasklist(final List<WorkerTaskBO> workerTasklist) {
        this.workerTasklist = workerTasklist;
    }
    

}
