package ink.codflow.sync.core;

import java.util.concurrent.atomic.AtomicLong;

import ink.codflow.sync.consts.SyncStatusEnum;

public class SyncProgress {

    private volatile SyncStatusEnum status = SyncStatusEnum.CREATE;

    AtomicLong totalDestSize;

    AtomicLong syncedSize;

    AtomicLong analyseSize;

    AtomicLong syncedFileCount;

    AtomicLong analyseFileCount;

    String message;

    public SyncProgress() {

        totalDestSize = new AtomicLong();
        syncedSize = new AtomicLong();
        analyseSize = new AtomicLong();
        syncedFileCount = new AtomicLong();
        analyseFileCount = new AtomicLong();
    }

    public SyncProgress(long syncedSize, long analyseSize, long syncedFileCount, long analyseFileCount,long totalDestSize) {

        this.syncedSize = new AtomicLong(syncedSize);
        this.analyseSize = new AtomicLong(analyseSize);
        this.syncedFileCount = new AtomicLong(syncedFileCount);
        this.analyseFileCount = new AtomicLong(analyseFileCount);
        
        this.totalDestSize = new AtomicLong(totalDestSize);

        

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

    public long getTotalDestSize() {
        return totalDestSize.get();
    }

    public long addAnalyseSize(long size) {
        return analyseSize.addAndGet(size);
    }

    public long addSyncedSize(long size) {
        return syncedSize.addAndGet(size);
    }

    public long addTotalDestSize(long size) {
        return totalDestSize.addAndGet(size);
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

    public SyncStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SyncStatusEnum status) {
        this.status = status;
    }

}
