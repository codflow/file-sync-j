package ink.codflow.sync.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.core.SyncProgress;
import ink.codflow.sync.exception.FileException;

public class LinkWorker {

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

	// src object type: dir,file ;dest object type: dir
	public long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {

		long totalSize = 0;
		try {
			Map<String, AbstractObjectWapper<?>> destMap = (destObject != null && destObject.isDir())
					? destObject.mapChildren()
					: null;

			if (srcObject.isDir()) {

				List<AbstractObjectWapper<?>> srcList = srcObject.listChildren();

				for (int i = 0; i < srcList.size(); i++) {
					AbstractObjectWapper<?> srcElement = srcList.get(i);
					String srcBaseName = srcElement.getBaseFileName();
					if (destMap != null && destMap.containsKey(srcBaseName)) {

						AbstractObjectWapper<?> destElement = destMap.get(srcBaseName);
						totalSize += doAnalyse(srcElement, destElement);
					} else {
						//
						if (srcElement.isFile()) {
							// TODO compare ts
							totalSize += countSize(srcElement);
						} else {
							AbstractObjectWapper<?> destElement0 = destObject.createChildDir(srcBaseName);
							totalSize += doAnalyse(srcElement, destElement0);

						}
					}
				}
			} else if (srcObject.isFile()) {
				String srcBaseName = srcObject.getBaseFileName();
				if ((destMap == null &&  isDiffFile(srcObject, destObject)) ||(destMap != null &&!destMap.containsKey(srcBaseName))) {
					long objectSize = countSize(srcObject);
					totalSize += objectSize;
				}
			}
		} catch (FileException e) {

			e.printStackTrace();
		}

		// checkAndLaunch(srcObject,destObject,totalSize);

		return totalSize;
	}

	private void checkAndLaunch(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject, long totalSize) {
		// TODO Auto-generated method stub

	}

	private long countSize(AbstractObjectWapper<?> srcObject) throws FileException {

		long size = srcObject.getSize();
		recordAnalyseFileCount();
		recordAnalyseFileSize(size);
		return size;

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

		try {
			Map<String, AbstractObjectWapper<?>> destMap = destObject.isDir() ? destObject.mapChildren() : null;

			if (srcObject.isDir()) {

				// Map<String, AbstractObjectWapper<?>> destMap = destObject.mapChildren();
				List<AbstractObjectWapper<?>> srcList = srcObject.listChildren();
				for (int i = 0; i < srcList.size(); i++) {
					AbstractObjectWapper<?> srcElement = srcList.get(i);
					String srcBaseName = srcElement.getBaseFileName();
					if (destMap != null && destMap.containsKey(srcBaseName)) {
						AbstractObjectWapper<?> destElement = destMap.get(srcBaseName);
						doSync(srcElement, destElement);
					} else {
						doCopy(srcObject, destObject);
					}
				}
			} else if (srcObject.isFile()) {

				String srcBaseName = srcObject.getBaseFileName();

				if ((destMap == null &&  isDiffFile(srcObject, destObject)) ||(destMap != null &&!destMap.containsKey(srcBaseName)) ) {
					doCopy(srcObject, destObject);
				}
			}
		} catch (FileException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean isDiffFile(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject)
			throws FileException {
		// is double check necessary?
		return srcObject.isDiff(destObject);
	}

	void submitSubLink() {

	}

	void doCopy(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) throws FileException {
		// long size = destObject.getSize();
		// recordSyncedFileSize(size);
		// recordSyncedFileCount();
		destObject.copyFrom(srcObject);
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

}
