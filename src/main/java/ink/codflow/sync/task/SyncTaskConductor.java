package ink.codflow.sync.task;

import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ink.codflow.sync.bo.ClientEndpointBO;
import ink.codflow.sync.bo.LinkBO;
import ink.codflow.sync.bo.ObjectBO;
import ink.codflow.sync.bo.TaskBO;
import ink.codflow.sync.bo.WorkerTaskBO;
import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.consts.SyncStatusEnum;
import ink.codflow.sync.core.ClientEndpoint;
import ink.codflow.sync.core.ClientEndpointPool;
import ink.codflow.sync.util.IdGen;

public class SyncTaskConductor {
    int maxThreadSize = 20;

    ThreadPoolExecutor pool = new ThreadPoolExecutor(10, maxThreadSize, 2000, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>());
    WeakHashMap<String, SyncTask> cacheMap = new WeakHashMap<String, SyncTask>();

    ClientEndpointPool clientPool = new ClientEndpointPool();

    public Integer registerEndpoint(ClientEndpointBO endpointBO) {

        ClientEndpoint<?> endPoint = clientPool.create(endpointBO);
        return endPoint.getId();
    }

    public ClientEndpoint<?> getEndpoint(ClientEndpointBO endpointBO) {
        return getEndpoint(endpointBO, false);
    }

    public ClientEndpoint<?> getEndpoint(ClientEndpointBO endpointBO, boolean renew) {
        int clientId = endpointBO.getId();

        ClientEndpoint<?> endPoint = clientPool.getEndpoint(clientId);
        if (endPoint == null || renew) {
            endPoint = clientPool.create(endpointBO);
        }
        return endPoint;
    }

    public SyncTask createSyncTask(LinkBO link, List<ObjectBO> selectedObjects, FileSyncMode mode) {

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

    public SyncTask createTask(Integer srcEndpointId, Integer dstEndpointId, List<ObjectBO> selectedObjects,
            FileSyncMode mode) {
        ClientEndpoint<?> srcEndpoint = clientPool.getEndpoint(srcEndpointId);
        ClientEndpoint<?> dstEndpoint = clientPool.getEndpoint(dstEndpointId);
        return createTask(srcEndpoint, dstEndpoint, selectedObjects, mode);
        
    }

    protected SyncTask createTask(ClientEndpoint<?> srcEndpoint, ClientEndpoint<?> dstEndpoint,
            List<ObjectBO> selectedObjects, FileSyncMode mode) {

        SyncTask syncTask = new SyncTask();
        syncTask.setId(IdGen.genUUID());
        syncTask.setDistEndpoint(dstEndpoint);
        syncTask.setSrcEndpoint(srcEndpoint);
        syncTask.setMode(mode != null ? mode : FileSyncMode.SYNC);
        syncTask.setSelectedObjects(selectedObjects);
        return syncTask;
    }

    public SyncTask createTask(LinkBO linkBO, List<ObjectBO> objectList, FileSyncMode mode) {

        ClientEndpointBO destEndpointBO = linkBO.getDestEndpoint();
        ClientEndpointBO srcEndpointBO = linkBO.getSrcEndpoint();
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

    public SyncTask createSyncTask(TaskBO taskBO) {

        SyncTask task = new SyncTask();

        List<WorkerTaskBO> workerTasklist = taskBO.getWorkerTasklist();
        for (WorkerTaskBO workerTaskBO : workerTasklist) {
            List<ObjectBO> objectUriList = workerTaskBO.getObjectList();
            LinkBO linkBO = workerTaskBO.getLinkBO();
            ClientEndpointBO distEndpointBO = linkBO.getDestEndpoint();
            ClientEndpointBO srcEndpointBO = linkBO.getSrcEndpoint();
            if (distEndpointBO.getId() == srcEndpointBO.getId()) {
				
			}
            FileSyncMode mode = linkBO.getMode();
            registerEndpoint(srcEndpointBO);
            registerEndpoint(distEndpointBO);
            task.addSubTask(createTask(linkBO, objectUriList, mode));
        }

        return task;
    }

    public boolean checkAndTryCancle(String traceId) {
        SyncTask task = cacheMap.get(traceId);
        SyncStatusEnum status = task.getSyncProgressView().getStatus();
        if (SyncStatusEnum.DONE.equals(status)&& SyncStatusEnum.FAILD.equals(status)) {
            return true;
        }
        return false;
    }
    
    public int countTaskInProgress() {
    	return pool.getActiveCount();
    }
    

}