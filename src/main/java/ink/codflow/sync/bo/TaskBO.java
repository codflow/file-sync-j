package ink.codflow.sync.bo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TaskBO {

    
    List<WorkerTaskBO> workerTasklist = new ArrayList<WorkerTaskBO>();
    
    
    
    public  void addWorkerTask(WorkerTaskBO workerTaskBO) {
        workerTasklist.add(workerTaskBO);
    }
}
