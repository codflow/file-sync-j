package ink.codflow.sync.task;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.sync.bo.ObjectBO;
import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.core.ClientEndpoint;
import ink.codflow.sync.core.SyncProgress;

public class SyncTask implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(SyncTask.class);

	ClientEndpoint<?> srcEndpoint;
	ClientEndpoint<?> distEndpoint;
	List<LinkWorker> workerList = new ArrayList<LinkWorker>();

	List<SyncTask> subTaskList = new ArrayList<SyncTask>();

	SyncProgress syncProgressView = new SyncProgress();

	FileSyncMode mode;

	List<ObjectBO> selectedObjects;
	
	String traceId;

	public ClientEndpoint<?> getSrcEndpoint() {
		return srcEndpoint;
	}

	public void setSrcEndpoint(ClientEndpoint<?> srcEndpoint) {
		this.srcEndpoint = srcEndpoint;
	}

	public ClientEndpoint<?> getDistEndpoint() {
		return distEndpoint;
	}

	public void setDistEndpoint(ClientEndpoint<?> distEndpoint) {
		this.distEndpoint = distEndpoint;
	}

	public List<LinkWorker> getWorkerList() {
		return workerList;
	}

	public void setWorkerList(List<LinkWorker> workerList) {
		this.workerList = workerList;
	}

	@Override
	public void run() {
		try {

			if (!getSubTaskList().isEmpty()) {
				List<SyncTask> tasks = getSubTaskList();
				for (SyncTask syncTask : tasks) {
					doRunATask(syncTask);
				}
			} else {
				doRunATask(this);
			}
		} catch (Exception e) {
			log.error("tasl error", e);
		}
	}

	void doRunATask(SyncTask task) {

		try {

			List<ObjectBO> objectUriBOs = task.selectedObjects;
			if (objectUriBOs != null && !objectUriBOs.isEmpty()) {
				for (ObjectBO objectUriBO : objectUriBOs) {
					String srcUri = objectUriBO.getUri();
					AbstractObjectWapper<?> srcObject = task.srcEndpoint.resolve(srcUri);
					AbstractObjectWapper<?> destObject = task.distEndpoint.resolve(srcUri);

					LinkWorker linkWorker = new LinkWorker(srcObject, destObject);
					task.workerList.add(linkWorker);

				}
			} else {
				AbstractObjectWapper<?> srcObject = task.srcEndpoint.resolve(task.srcEndpoint.getRoot());
				AbstractObjectWapper<?> destObject = task.distEndpoint.resolve(task.distEndpoint.getRoot());
				LinkWorker linkWorker = new LinkWorker(srcObject, destObject);
				task.workerList.add(linkWorker);

			}

			for (LinkWorker linkWorker : task.workerList) {
				linkWorker.analyse();
			}

			for (LinkWorker linkWorker : task.workerList) {
				linkWorker.sync();
			}
		} catch (Exception e) {
			log.error("task error", e);
		}

	}

	public SyncProgress getSyncProgressView() {

		if (!this.subTaskList.isEmpty()) {
			return getSubTaskProgressView();
		} else if (!this.workerList.isEmpty()) {
			return getWorkerProgressView();
		} else {
			return this.syncProgressView;
		}

	}

	SyncProgress getSubTaskProgressView() {
		List<SyncTask> subTasks = getSubTaskList();
		long syncedSize = 0;
		long analyseSize = 0;
		long syncedFileCount = 0;
		long analyseFileCount = 0;
		for (SyncTask syncTask : subTasks) {
			SyncProgress syncProgress = syncTask.getSyncProgressView();
			syncedSize += syncProgress.getSyncedSize();
			analyseSize += syncProgress.getAnalyseSize();
			syncedFileCount += syncProgress.getSyncedFileCount();
			analyseFileCount += syncProgress.getAnalyseFileCount();
		}
		SyncProgress syncProgressView0 = new SyncProgress(syncedSize, analyseSize, syncedFileCount, analyseFileCount);
		this.syncProgressView = syncProgressView0;
		return syncProgressView0;

	}

	SyncProgress getWorkerProgressView() {

		List<LinkWorker> workers = getWorkerList();
		long syncedSize = 0;
		long analyseSize = 0;
		long syncedFileCount = 0;
		long analyseFileCount = 0;
		for (LinkWorker linkWorker : workers) {
			SyncProgress syncProgress = linkWorker.getProgress();
			syncedSize += syncProgress.getSyncedSize();
			analyseSize += syncProgress.getAnalyseSize();
			syncedFileCount += syncProgress.getSyncedFileCount();
			analyseFileCount += syncProgress.getAnalyseFileCount();
		}
		SyncProgress syncProgressView0 = new SyncProgress(syncedSize, analyseSize, syncedFileCount, analyseFileCount);
		this.syncProgressView = syncProgressView0;
		return syncProgressView0;
	}

	public List<SyncTask> getSubTaskList() {
		return subTaskList;
	}

	public void addSubTask(SyncTask subTask) {
		this.subTaskList.add(subTask);
	}

	public FileSyncMode getMode() {
		return mode;
	}

	public void setMode(FileSyncMode mode) {
		this.mode = mode;
	}
	
	

}