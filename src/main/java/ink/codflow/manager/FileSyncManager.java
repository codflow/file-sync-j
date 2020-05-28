package ink.codflow.manager;

import java.util.List;

import ink.codflow.bo.ClientEndpointBO;
import ink.codflow.bo.LinkBO;
import ink.codflow.bo.ObjectUriBO;
import ink.codflow.bo.TaskBO;
import ink.codflow.bo.WorkerTaskBO;
import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.task.SyncTask;
import ink.codflow.sync.task.SyncTaskConductor;

public class FileSyncManager {

	SyncTaskConductor conductor;

	public void createSyncTask(LinkBO link, List<ObjectUriBO> selectedObjects) {

		ClientEndpointBO distEndpointBO = link.getDistEndpoint();
		ClientEndpointBO srcEndpointBO = link.getSrcEndpoint();
		FileSyncMode mode = link.getMode();
		conductor.registerEndpoint(srcEndpointBO);
		conductor.registerEndpoint(distEndpointBO);
		SyncTask task = conductor.launch(link, selectedObjects, true);

	}

	public void createSyncTask(TaskBO taskBO) {

		List<WorkerTaskBO> workerTasklist = taskBO.getWorkerTasklist();
		for (WorkerTaskBO workerTaskBO : workerTasklist) {
			List<ObjectUriBO> objectUriList = workerTaskBO.getObjectUriList();
			LinkBO linkBO = workerTaskBO.getLinkBO();
			ClientEndpointBO distEndpointBO = linkBO.getDistEndpoint();
			ClientEndpointBO srcEndpointBO = linkBO.getSrcEndpoint();
			FileSyncMode mode = linkBO.getMode();

			conductor.registerEndpoint(srcEndpointBO);
			conductor.registerEndpoint(distEndpointBO);
			conductor.createTask(linkBO, objectUriList, mode);

			if (objectUriList != null) {

			}
		}
	}

}
