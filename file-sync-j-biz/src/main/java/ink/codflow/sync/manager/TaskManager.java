package ink.codflow.sync.manager;

import org.springframework.beans.factory.annotation.Autowired;
import ink.codflow.sync.bo.TaskBO;
import ink.codflow.sync.bo.TaskDetailBO;
import ink.codflow.sync.dao.TaskDOMapper;
import ink.codflow.sync.dao.TaskDetailDOMapper;
import ink.codflow.sync.entity.TaskDO;

public class TaskManager {

    @Autowired
    TaskDOMapper taskDOMapper;
    @Autowired
    TaskDetailDOMapper taskDetailDOMapper;

    public TaskBO getById(String taskId){

        return null;
    }

	public void save(WorkerTask workerTask) {
        
        TaskDO taskDO = new TaskDO();
        String taskUUID= generateUUID();
        taskDO.setId(taskUUID);
        Link link = workerTask.getLink();
        Integer linkId = link.getId();
        taskDO.setLinkId(linkId);
        taskDOMapper.insert(taskDO);

    }
    
    public void updateTaskDetail(TaskDetailBO taskDetailBO){
        
    }
    
    String generateUUID(){
        return null;
    }
}
