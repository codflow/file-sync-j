package ink.codflow.sync.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.sync.consts.SyncStatusEnum;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.core.SyncProgress;
import ink.codflow.sync.core.handler.WorkerHandler;
import ink.codflow.sync.exception.FileException;

public class LinkWorker {

	private static final Logger logger = LoggerFactory.getLogger(LinkWorker.class);

	WorkerHandler workerHandler;

	SyncProgress progress = new SyncProgress();

	AbstractObjectWapper<?> srcObject;

	AbstractObjectWapper<?> destObject;
	
	TaskSpecs specs;
	

	public LinkWorker() {

	}

	public LinkWorker(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {
		this.srcObject = srcObject;
		this.destObject = destObject;
	}

	public SyncProgress analyse() throws FileException {

		return this.analyse(this.srcObject, this.destObject);
	}

	public SyncProgress sync() {

		return this.sync(this.srcObject, this.destObject);
	}






	public SyncProgress analyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject)
			throws FileException {

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
		AnalyseListener listener = new AnalyseListener();

		listener.checkAndUpdateAnalyseStatus();
		try {
			if(srcObject.isExist()){
				return workerHandler.doAnalyse(srcObject, destObject, listener);
			};
		} catch (FileException e) {
		}
		return 0;

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
		SyncListener listener =  new SyncListener();
		listener.checkAndUpdateSyncStatus();
		try {
			workerHandler.doSync(srcObject, destObject, listener,specs);
			listener.doneWithSyncStatus();
		} catch (Exception e) {
			logger.error("sync failed", e);
			listener.failedWithSyncStatus();
		}
	}

	public class SyncListener {

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


		public void  checkAndUpdateSyncStatus(){
			if (!SyncStatusEnum.SYNC.equals(progress.getStatus())) {
				progress.setStatus(SyncStatusEnum.SYNC);
			}
			;
		}

		public void  doneWithSyncStatus(){
			if (!SyncStatusEnum.DONE.equals(progress.getStatus())) {
				progress.setStatus(SyncStatusEnum.DONE);
			}
		}

		public void  failedWithSyncStatus(){
			if (!SyncStatusEnum.FAILED.equals(progress.getStatus())) {
				progress.setStatus(SyncStatusEnum.FAILED);
			}
		}

	}

	public class AnalyseListener {

		public void doRecordDiff(AbstractObjectWapper<?> srcObject) throws FileException {

			long size = srcObject.getSize();
			recordAnalyseFileSize(size);
			recordAnalyseFileCount();
		}

		public void doRecordFile(AbstractObjectWapper<?> srcObject) throws FileException {

			long size = srcObject.getSize();
			progress.addTotalDestSize(size);
		}


		public void  checkAndUpdateAnalyseStatus(){
			if (!SyncStatusEnum.ANALYSE.equals(progress.getStatus())) {
				progress.setStatus(SyncStatusEnum.ANALYSE);
			}
			;
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

    public TaskSpecs getSpecs() {
        return specs;
    }

    public void setSpecs(TaskSpecs specs) {
        this.specs = specs;
    }
	
	

}
