package ink.codflow.sync.service;

import org.springframework.beans.factory.annotation.Autowired;

import ink.codflow.sync.bo.EndpointBO;
import ink.codflow.sync.bo.LinkBO;
import ink.codflow.sync.bo.TaskBO;
import ink.codflow.sync.dto.TaskDTO;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.manager.ClientManager;
import ink.codflow.sync.manager.EndpointManager;
import ink.codflow.sync.manager.FileSyncManager;
import ink.codflow.sync.manager.LinkManager;
import ink.codflow.sync.manager.Task;
import ink.codflow.sync.manager.TaskManager;

public class TaskService {

    @Autowired
    FileSyncManager fileSyncManager;
    @Autowired
    ClientManager clientManager;
    @Autowired
    LinkManager linkManager;
    @Autowired
    EndpointManager endpointManager;
    @Autowired
    TaskManager taskManager;

    public TaskDTO createTask() {

        return null;
    }

    public TaskDTO getByTaskId(String taskId) {
        return null;
    }

    public TaskDTO getByTraceId(String traceId) {
        return null;

    }

    public void startTask(String taskId) {

        TaskBO taskBO = taskManager.getById(taskId);
        String linkId = taskBO.getLinkId();

        LinkBO linkBO = linkManager.getById(linkId);

        Integer sourceEndpointId = linkBO.getSourceId();
        Integer targetEndpointId = linkBO.getTargetId();

        EndpointBO sourceEndpoint = endpointManager.getById(sourceEndpointId);
        EndpointBO targetEndpoint = endpointManager.getById(targetEndpointId);
        Task task = new Task();
        
        try {
            fileSyncManager.createSyncTask(task);
        } catch (FileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    public void pauseTask(String taskId){

    }

    public void resumeTask( String taskId){

    }
    
    public void cancleTask( String taskId){

    }

    public void deleteTask( String taskId){

    }

    
}