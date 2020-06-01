package ink.codflow.sync.core;

import java.util.concurrent.atomic.AtomicLong;

import ink.codflow.sync.consts.SyncStatus;

public class SyncProgress {

	private volatile SyncStatus status;

	AtomicLong syncedSize;

	AtomicLong analyseSize;

	AtomicLong syncedFileCount;

	AtomicLong analyseFileCount;

	String message;

	public SyncProgress() {

		syncedSize = new AtomicLong();
		analyseSize = new AtomicLong();
		syncedFileCount = new AtomicLong();
		analyseFileCount = new AtomicLong();
	}

	public SyncProgress(long syncedSize, long analyseSize, long syncedFileCount, long analyseFileCount) {

		this.analyseFileCount = new AtomicLong(syncedSize);
		this.analyseSize = new AtomicLong(analyseSize);
		this.syncedFileCount = new AtomicLong(syncedFileCount);
		this.analyseFileCount = new AtomicLong(analyseFileCount);

	}

	public long getSyncedSize() {
		return syncedSize.get();
	}

	public long getAnalyseSize() {
		return analyseSize.get();
	}

	public long getSyncedFileCount() {
		return syncedFileCount.get();
	}

	public long getAnalyseFileCount() {
		return analyseFileCount.get();
	}

	public long addAnalyseSize(long size) {
		return analyseSize.addAndGet(size);
	}

	public long addSyncedSize(long size) {
		return syncedSize.addAndGet(size);
	}

	public long addAnalyseFileCount(long size) {
		return analyseFileCount.addAndGet(size);
	}

	public long addSyncedFileCountSize(long size) {
		return syncedFileCount.addAndGet(size);
	}

	public long increaseAnalyseFileCount() {
		return analyseFileCount.incrementAndGet();
	}

	public long increaseSyncedFileCount() {
		return syncedFileCount.incrementAndGet();
	}

	public void clear() {
		syncedSize.set(0);
		analyseSize.set(0);
		syncedFileCount.set(0);
		analyseFileCount.set(0);
	}

	public SyncStatus getStatus() {
		return status;
	}

	public void setStatus(SyncStatus status) {
		this.status = status;
	}

 

}
