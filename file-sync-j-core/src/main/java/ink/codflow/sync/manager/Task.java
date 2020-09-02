package ink.codflow.sync.manager;

import java.util.ArrayList;
import java.util.List;



public class Task {

    
    List<WorkerTask> workerTasklist = new ArrayList<WorkerTask>();
    
    
    
    public  void addWorkerTask(final WorkerTask workerTaskBO) {
        workerTasklist.add(workerTaskBO);
    }

    /**
     * @return the workerTasklist
     */
    public List<WorkerTask> getWorkerTasklist() {
        return workerTasklist;
    }

    /**
     * @param workerTasklist the workerTasklist to set
     */
    public void setWorkerTasklist(final List<WorkerTask> workerTasklist) {
        this.workerTasklist = workerTasklist;
    }
    

}
