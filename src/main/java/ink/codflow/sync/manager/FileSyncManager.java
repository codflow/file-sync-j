package ink.codflow.sync.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.sync.bo.ClientEndpointBO;
import ink.codflow.sync.bo.LinkBO;
import ink.codflow.sync.bo.ObjectBO;
import ink.codflow.sync.bo.TaskBO;
import ink.codflow.sync.bo.WorkerTaskBO;
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

	public SyncTask createSyncTask(LinkBO link, List<ObjectBO> selectedObjects, FileSyncMode mode) {

		ClientEndpointBO distEndpointBO = link.getDistEndpoint();
		ClientEndpointBO srcEndpointBO = link.getSrcEndpoint();

		FileSyncMode mode0 = mode != null ? mode : link.getMode();
		conductor.registerEndpoint(srcEndpointBO);
		conductor.registerEndpoint(distEndpointBO);
		SyncTask task = conductor.createSyncTask(link, selectedObjects, mode0);
		return task;
	}

	public SyncTask createSyncTask(TaskBO taskBO) {

		return conductor.createSyncTask(taskBO);
	}

	public void launchTask(SyncTask task) {
		conductor.launch(task);
	}

	public boolean testClient(ClientEndpointBO endpoint) {

		ClientEndpoint<?> clientEndpoint = conductor.getEndpoint(endpoint, true);
		AbstractObjectWapper<?> object = clientEndpoint.resolveRelatively("");
		try {
			object.listChildren();
			return true;
		} catch (FileException e) {
			logger.error("chk error", e);
			return false;
		}
	}
}
