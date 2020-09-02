package ink.codflow.sync.task;

import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ink.codflow.sync.manager.*;
import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.consts.SyncStatusEnum;
import ink.codflow.sync.core.ClientEndpoint;
import ink.codflow.sync.core.ClientEndpointPool;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.util.IdGen;

public class SyncTaskConductor {
    
    
    int maxThreadSize = 20;

    ThreadPoolExecutor pool = new ThreadPoolExecutor(10, maxThreadSize, 2000, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>());
    WeakHashMap<String, SyncTask> cacheMap = new WeakHashMap<String, SyncTask>();

    ClientEndpointPool clientPool = new ClientEndpointPool();

    public Integer registerEndpoint(Endpoint endpointBO) throws FileException {

        ClientEndpoint<?> endPoint = clientPool.create(endpointBO);
        return endPoint.getId();
    }

    public ClientEndpoint<?> getEndpoint(Endpoint endpointBO) throws FileException {
        return getEndpoint(endpointBO, false);
    }

    public ClientEndpoint<?> getEndpoint(Endpoint endpointBO, boolean renew) throws FileException {
        int clientId = endpointBO.getId();

        ClientEndpoint<?> endPoint = clientPool.getEndpoint(clientId);
        if (endPoint == null || renew) {
            endPoint = clientPool.create(endpointBO);
        }
        return endPoint;
    }

    public SyncTask createSyncTask(Link link, List<FileObject> selectedObjects, FileSyncMode mode) throws FileException {

        FileSyncMode mode0 = mode != null ? mode : link.getMode();
        return createTask(link, selectedObjects, mode0);
    }

    public String launch(SyncTask task) {
        
        String id = task.getId();
        String traceId ="T-"+id;
        cacheMap.put(traceId, task);
        pool.submit(task);
        return traceId;
    }

    public SyncTask createTask(Integer srcEndpointId, Integer dstEndpointId, List<FileObject> selectedObjects,
            FileSyncMode mode) {
        ClientEndpoint<?> srcEndpoint = clientPool.getEndpoint(srcEndpointId);
        ClientEndpoint<?> dstEndpoint = clientPool.getEndpoint(dstEndpointId);
        return createTask(srcEndpoint, dstEndpoint, selectedObjects, mode);
        
    }

    protected SyncTask createTask(ClientEndpoint<?> srcEndpoint, ClientEndpoint<?> dstEndpoint,
            List<FileObject> selectedObjects, FileSyncMode mode) {

        SyncTask syncTask = new SyncTask();
        syncTask.setId(IdGen.genUUID());
        syncTask.setDistEndpoint(dstEndpoint);
        syncTask.setSrcEndpoint(srcEndpoint);
        syncTask.setMode(mode != null ? mode : FileSyncMode.SYNC);
        syncTask.setSelectedObjects(selectedObjects);
        return syncTask;
    }

    public SyncTask createTask(Link linkBO, List<FileObject> objectList, FileSyncMode mode) throws FileException {

        Endpoint destEndpointBO = linkBO.getDestEndpoint();
        Endpoint srcEndpointBO = linkBO.getSrcEndpoint();
        int srcEndpointId = srcEndpointBO.getId();
        int dstEndpointId = destEndpointBO.getId();
        ClientEndpoint<?> srcEndpoint0 = clientPool.getEndpoint(srcEndpointId);
        ClientEndpoint<?> destEndpoint0 = clientPool.getEndpoint(dstEndpointId);

        if (srcEndpoint0 == null) {
            getEndpoint(srcEndpointBO);
        }
        if (destEndpoint0 == null) {
            getEndpoint(destEndpointBO);
        }

        FileSyncMode mode0 = mode != null ? mode : linkBO.getMode();
        linkBO.getMaxThread();
        return createTask(srcEndpointId, dstEndpointId, objectList, mode0);

    }

    public SyncTask createSyncTask(Task taskBO) throws FileException {

        SyncTask task = new SyncTask();
        task.setId(IdGen.genUUID());
        List<WorkerTask> workerTasklist = taskBO.getWorkerTasklist();
        for (WorkerTask workerTaskBO : workerTasklist) {
            List<FileObject> objectUriList = workerTaskBO.getObjectList();
            Link linkBO = workerTaskBO.getLinkBO();
            Endpoint distEndpointBO = linkBO.getDestEndpoint();
            Endpoint srcEndpointBO = linkBO.getSrcEndpoint();
            if (distEndpointBO.getId() == srcEndpointBO.getId()) {
				
			}
            FileSyncMode mode = linkBO.getMode();
            registerEndpoint(srcEndpointBO);
            registerEndpoint(distEndpointBO);
            TaskSpecs specs = workerTaskBO.getSpecs();
            SyncTask subSyncTask = createTask(linkBO, objectUriList, mode);
            subSyncTask.setSpecs(specs);
            task.addSubTask(subSyncTask);
        }
        return task;
    }

    public boolean checkAndTryCancle(String traceId) {
        SyncTask task = cacheMap.get(traceId);
        SyncStatusEnum status = task.getSyncProgressView().getStatus();
        if (SyncStatusEnum.DONE.equals(status)&& SyncStatusEnum.FAILED.equals(status)) {
            return true;
        }
    
        return false;
    }
    
    public int countTaskInProgress() {
    	return pool.getActiveCount();
    }
    
    
    
    

}