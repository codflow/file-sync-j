package ink.codflow.sync.task;

import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.core.SyncProgress;
import ink.codflow.sync.exception.FileException;

public class LinkWorker {

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
		return workerHandler.doAnalyse(srcObject, destObject,new AnalyseListener());

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
		workerHandler.doSync(srcObject, destObject, new CopyListener());
	}

 

	class CopyListener {

		public void doRecord(AbstractObjectWapper<?> srcObject) throws FileException {
			if (srcObject.isFile()) {
				long size = srcObject.getSize();
				recordSyncedFileSize(size);
				recordSyncedFileCount();
			}else {
				System.err.println("ignore record :"+srcObject.getBaseFileName() );
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

		public void doRecord(AbstractObjectWapper<?> srcObject) throws FileException {

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

	private void doRecord() {

		// TODO Auto-generated method stub

	}
	
	

	void recordAnalyseFileSize(long size) {
		this.progress.addAnalyseSize(size);
	}

	void recordAnalyseFileCount() {
		this.progress.increaseAnalyseFileCount();
	}

	void recordSyncedFileSize(long size) {
		this.progress.addSyncedSize(size);
	}

	void recordSyncedFileCount() {
		this.progress.increaseSyncedFileCount();
	}

	public SyncProgress getProgress() {
		return progress;
	}

	public void setWorkerHandler(WorkerHandler workerHandler) {
		this.workerHandler = workerHandler;
	}

}
