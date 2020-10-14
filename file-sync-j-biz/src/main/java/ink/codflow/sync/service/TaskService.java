package ink.codflow.sync.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.appender.FileManager;
import org.springframework.beans.factory.annotation.Autowired;

import ink.codflow.sync.bo.ClientAccessBO;
import ink.codflow.sync.bo.EndpointBO;
import ink.codflow.sync.bo.LinkBO;
import ink.codflow.sync.bo.TaskBO;
import ink.codflow.sync.bo.TaskDetailBO;
import ink.codflow.sync.consts.AuthDataType;
import ink.codflow.sync.consts.AuthTypeEnum;
import ink.codflow.sync.consts.AuthenticationType;
import ink.codflow.sync.consts.SyncStatusEnum;
import ink.codflow.sync.core.SyncProgress;
import ink.codflow.sync.dto.TaskDTO;
import ink.codflow.sync.entity.ClientAccessDataDO;
import ink.codflow.sync.entity.TaskDetailDO;
import ink.codflow.sync.exception.AuthTypeException;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.manager.Authentication;
import ink.codflow.sync.manager.ClientAccessManager;
import ink.codflow.sync.manager.ClientManager;
import ink.codflow.sync.manager.Endpoint;
import ink.codflow.sync.manager.EndpointManager;
import ink.codflow.sync.manager.FileSyncManager;
import ink.codflow.sync.manager.Link;
import ink.codflow.sync.manager.LinkManager;
import ink.codflow.sync.manager.TaskManager;
import ink.codflow.sync.manager.WorkerTask;
import ink.codflow.sync.task.SyncTask;
import ink.codflow.sync.task.TaskStatusListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskService {

    @Autowired
    FileSyncManager fileSyncManager;

    @Autowired
    ClientManager clientManager;

    @Autowired
    ClientAccessManager clientAccessManager;

    @Autowired
    LinkManager linkManager;

    @Autowired
    EndpointManager endpointManager;

    @Autowired
    TaskManager taskManager;

    public TaskDTO createTask(String taskId,boolean start) {

        try {
            SyncTask syncTask = prepareTask(taskId);
            String traceId = syncTask.getId();
            addListener(syncTask);
            if(start){
                fileSyncManager.launchTask(syncTask);

            }
        } catch (FileException e) {
            log.error("create task failed", e);
        }

        return null;
    }

    private void addListener(SyncTask syncTask) {
        String taskId = syncTask.getId();
        TaskStatusListener progressUpdateListener = new TaskStatusPersistanceListener(taskId, taskManager);
        List<TaskStatusListener> listeners = new ArrayList<>();
        listeners.add(progressUpdateListener);
        syncTask.setTaskStatusListeners(listeners);
    } 

    public TaskDTO getByTaskId(String taskId) {
        return null;
    }

    public TaskDTO getByTraceId(String traceId) {
        return null;

    }

    public SyncTask prepareTask(String taskId) throws FileException {

        TaskBO taskBO = taskManager.getById(taskId);
        String linkId = taskBO.getLinkId();
        LinkBO linkBO = linkManager.getById(linkId);
        Integer sourceEndpointId = linkBO.getSourceId();
        Integer targetEndpointId = linkBO.getTargetId();

        Endpoint sourceEnpoint0 = fillEndpoint(sourceEndpointId);
        Endpoint targetEnpoint0 = fillEndpoint(targetEndpointId);

        Link link = new Link();
        link.setSrcEndpoint(sourceEnpoint0);
        link.setDestEndpoint(targetEnpoint0);

        WorkerTask subTask = new WorkerTask();
        subTask.setLink(link);
        taskManager.save(subTask);

        SyncTask syncTask = fileSyncManager.createSyncTask(subTask);
        return syncTask;
    }

    public String startTask(SyncTask syncTask){

        String traceId = fileSyncManager.launchTask(syncTask);
        

        return null;
    }

    void updateTaskStatus(SyncTask syncTask){
        
    }

    void updateTaskStatus(String traceId , String taskId , SyncProgress progress){
        TaskDetailBO taskDetailBO = new TaskDetailBO();
        
        taskManager.updateTaskDetail(taskDetailBO);

    }

    private Endpoint fillEndpoint(Integer sourceEndpointId) {
        EndpointBO sourceEndpoint = endpointManager.getById(sourceEndpointId);
        Integer clientAccessId = sourceEndpoint.getClientAccessId();
        ClientAccessBO sourceAccess = clientAccessManager.getClientAccessById(clientAccessId);
        AuthTypeEnum authTypeEnum = sourceAccess.getType();
        AuthenticationType authenticationType = resolveAuthType(authTypeEnum);
        Endpoint endpoint0 = new Endpoint();
        Integer id = sourceEndpoint.getId();
        endpoint0.setId(id);
        Authentication sourceAuthentication = new Authentication();
        List<ClientAccessDataDO> dataList = sourceAccess.getData();

        for (ClientAccessDataDO clientAccessDataDO : dataList) {
            String accessDataType = clientAccessDataDO.getType();
            String accessValue = clientAccessDataDO.getValue();
            AuthDataType dataType = AuthDataType.resolve(accessDataType);
            if (dataType != null) {
                sourceAuthentication.addParam(dataType, accessValue);
            } else {
                log.warn("resolve authentication type failed");
            }
        }

        sourceAuthentication.setAuthType(authenticationType);
        endpoint0.setAuthentication(sourceAuthentication);
        String rootPath = sourceEndpoint.getRoot();
        endpoint0.setRootPath(rootPath);
        String name = sourceEndpoint.getName();
        endpoint0.setName(name);

        return endpoint0;
    }

    public void pauseTask(String taskId) {

    }

    public void resumeTask(String taskId) {

    }

    public void cancleTask(String taskId) {


    }

    public void deleteTask(String taskId) {

    }

    AuthenticationType resolveAuthType(AuthTypeEnum authTypeEnum) {

        switch (authTypeEnum) {
            case PUBLIC_KEY:

                return AuthenticationType.PUBKEY;
            case LOCAL:

                return AuthenticationType.LOCAL;

            default:
                throw new AuthTypeException();
        }
    }

    class TaskStatusPersistanceListener implements TaskStatusListener {

        TaskManager taskManager;

        String taskId;

        public TaskStatusPersistanceListener(String taskId ,TaskManager taskManager) {
            this.taskManager = taskManager;
            this.taskId = taskId;
        }

        @Override
        public boolean statusChange(SyncProgress progress, SyncStatusEnum status) {

            progress.getAnalyseFileCount();
            progress.getAnalyseSize();
            progress.getSyncedFileCount();
            progress.getSyncedSize();
            progress.getTotalDestSize();


            // TODO Auto-generated method stub
            return false;
        }

    }

}