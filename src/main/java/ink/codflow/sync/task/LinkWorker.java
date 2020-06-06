package ink.codflow.sync.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.core.SyncProgress;
import ink.codflow.sync.exception.FileException;

public class LinkWorker {

	private static final Logger logger = LoggerFactory.getLogger(LinkWorker.class);

	WorkerHandler workerHandler;

	SyncProgress progress = new SyncProgress();

	AbstractObjectWapper<?> srcObject;

	AbstractObjectWapper<?> destObject;

	public LinkWorker() {

	}

	public LinkWorker(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {
		this.srcObject = srcObject;
		this.destObject = destObject;
	}

	public SyncProgress analyse() {

		return this.analyse(this.srcObject, this.destObject);
	}

	public SyncProgress sync() {

		return this.sync(this.srcObject, this.destObject);
	}

	public SyncProgress analyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {

		if (srcObject == null) {
			this.srcObject = srcObject;
		}
		if (destObject == null) {
			this.destObject = destObject;
		}
		// TODO check not null
		doAnalyse(this.srcObject, this.destObject);
		return progress;
	}

	protected long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {
		return workerHandler.doAnalyse(srcObject, destObject, new AnalyseListener());

	}

	public SyncProgress sync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {
		if (srcObject == null) {
			this.srcObject = srcObject;
		}
		if (destObject == null) {
			this.destObject = destObject;
		}
		doSync(srcObject, destObject);
		return this.progress;
	}

	// src object type: dir,file ;dest object type: dir,file
	protected void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {
		workerHandler.doSync(srcObject, destObject, new SyncListener());
	}

	class SyncListener {

		long expire;

		public SyncListener() {
			
		}

		public SyncListener(long expire) {
			this.expire = expire;
		}

		public boolean doCheckExpired(long targetUnixtime) {
			long currentUnixtime = System.currentTimeMillis();
			return currentUnixtime - targetUnixtime > expire*1000;
		}

		public void doRecordDiff(AbstractObjectWapper<?> srcObject) throws FileException {
			if (srcObject.isFile()) {
				long size = srcObject.getSize();
				recordSyncedFileSize(size);
				recordSyncedFileCount();
			} else {
				logger.warn("ignore record : {}", srcObject.getBaseFileName());
			}
		}

		void recordSyncedFileSize(long size) {
			progress.addSyncedSize(size);
		}

		void recordSyncedFileCount() {
			progress.increaseSyncedFileCount();
		}

	}

	class AnalyseListener {

		public void doRecordDiff(AbstractObjectWapper<?> srcObject) throws FileException {

			long size = srcObject.getSize();
			recordAnalyseFileSize(size);
			recordAnalyseFileCount();
		}

		void recordAnalyseFileSize(long size) {
			progress.addAnalyseSize(size);
		}

		void recordAnalyseFileCount() {
			progress.increaseAnalyseFileCount();
		}

	}

	public SyncProgress getProgress() {
		return progress;
	}

	public void setWorkerHandler(WorkerHandler workerHandler) {
		this.workerHandler = workerHandler;
	}

}
