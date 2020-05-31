package ink.codflow.sync.task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import ink.codflow.bo.ClientEndpointBO;
import ink.codflow.bo.LinkBO;
import ink.codflow.bo.ObjectBO;
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

	public SyncTask launch(LinkBO link, List<ObjectBO> selectedObjects, boolean startImmediatly) {

		SyncTask task = createTask(link, selectedObjects, FileSyncMode.FILE_INC);
		pool.submit(task);
		return task;
	}

	public void launch(SyncTask task) {

		pool.submit(task);
	}

	public SyncTask createTask(Integer srcEndpointId, Integer dstEndpointId, List<ObjectBO> selectedObjects,
			FileSyncMode mode) {

		ClientEndpoint<?> srcEndpoint = clientPool.getEndpoint(srcEndpointId);
		ClientEndpoint<?> dstEndpoint = clientPool.getEndpoint(dstEndpointId);

		SyncTask syncTask = new SyncTask();
		syncTask.setDistEndpoint(dstEndpoint);
		syncTask.setSrcEndpoint(srcEndpoint);
		return syncTask;
	}

	public SyncTask createTask(LinkBO linkBO, List<ObjectBO> objectUriList, FileSyncMode mode) {

		ClientEndpointBO destEndpointBO = linkBO.getDistEndpoint();
		ClientEndpointBO srcEndpointBO = linkBO.getSrcEndpoint();
		int srcEndpointId = srcEndpointBO.getId();
		int dstEndpointId = destEndpointBO.getId();
		ClientEndpoint<?> srcEndpoint0 = clientPool.getEndpoint(srcEndpointId);
		ClientEndpoint<?> destEndpoint0 = clientPool.getEndpoint(dstEndpointId);

		ClientEndpoint<?> srcEndpoint = srcEndpoint0 != null ? srcEndpoint0 : getEndpoint(srcEndpointBO);
		ClientEndpoint<?> destEndpoint = destEndpoint0 != null ? destEndpoint0 : getEndpoint(destEndpointBO);

		FileSyncMode mode0 = mode != null ? mode : linkBO.getMode();
		linkBO.getMaxThread();
		return createTask(srcEndpointId, dstEndpointId, objectUriList, mode0);

	}

}