package ink.codflow.sync.task;

import java.util.List;
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
import ink.codflow.sync.core.ClientEndpoint;
import ink.codflow.sync.core.ClientEndpointPool;

public class SyncTaskConductor {
	int maxThreadSize = 100;

	ExecutorService pool = new ThreadPoolExecutor(10, maxThreadSize, 2000, TimeUnit.MILLISECONDS,
			new LinkedBlockingDeque<>());

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

	public void launch(SyncTask task) {
		pool.submit(task);
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
		syncTask.setDistEndpoint(dstEndpoint);
		syncTask.setSrcEndpoint(srcEndpoint);
		syncTask.setMode(mode != null ? mode : FileSyncMode.SYNC);
		syncTask.setSelectedObjects(selectedObjects);
		return syncTask;
	}

	public SyncTask createTask(LinkBO linkBO, List<ObjectBO> objectUriList, FileSyncMode mode) {

		ClientEndpointBO destEndpointBO = linkBO.getDistEndpoint();
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
		return createTask(srcEndpointId, dstEndpointId, objectUriList, mode0);

	}

	public SyncTask createSyncTask(TaskBO taskBO) {

		SyncTask task = new SyncTask();

		List<WorkerTaskBO> workerTasklist = taskBO.getWorkerTasklist();
		for (WorkerTaskBO workerTaskBO : workerTasklist) {
			List<ObjectBO> objectUriList = workerTaskBO.getObjectUriList();
			LinkBO linkBO = workerTaskBO.getLinkBO();
			ClientEndpointBO distEndpointBO = linkBO.getDistEndpoint();
			ClientEndpointBO srcEndpointBO = linkBO.getSrcEndpoint();
			FileSyncMode mode = linkBO.getMode();
			registerEndpoint(srcEndpointBO);
			registerEndpoint(distEndpointBO);
			task.addSubTask(createTask(linkBO, objectUriList, mode));
		}
		return task;
	}

}