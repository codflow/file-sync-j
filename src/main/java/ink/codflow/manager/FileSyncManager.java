package ink.codflow.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.bo.ClientEndpointBO;
import ink.codflow.bo.LinkBO;
import ink.codflow.bo.ObjectBO;
import ink.codflow.bo.TaskBO;
import ink.codflow.bo.WorkerTaskBO;
import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.core.ClientEndpoint;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.task.SyncTask;
import ink.codflow.sync.task.SyncTaskConductor;

public class FileSyncManager {

	Logger logger = LoggerFactory.getLogger(FileSyncManager.class);

	SyncTaskConductor conductor;

	public FileSyncManager(SyncTaskConductor conductor) {

		this.conductor = conductor;

	}

	public void createSyncTask(LinkBO link, List<ObjectBO> selectedObjects) {

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
			List<ObjectBO> objectUriList = workerTaskBO.getObjectUriList();
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

	public boolean testClient(ClientEndpointBO endpoint) {

		ClientEndpoint<?> clientEndpoint = conductor.getEndpoint(endpoint,true);
		AbstractObjectWapper<?> object = clientEndpoint.resolveRelatively("/");
		try {
			object.listChildren();
			return true;
		} catch (FileException e) {
			logger.error("chk error",e);
			return false;
		}

	}

}
