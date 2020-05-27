package ink.codflow.sync.task;

import java.util.List;

import ink.codflow.bo.ObjectUriBO;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.core.ClientEndpoint;
import ink.codflow.sync.core.SyncProgress;

public class SyncTask implements Runnable {

    ClientEndpoint srcEndpoint;
    ClientEndpoint distEndpoint;
    List<LinkWorker> workerList;
    SyncProgress syncProgressView = new SyncProgress();
    List<ObjectUriBO> selectedObjects;
    String traceId;

    public ClientEndpoint getSrcEndpoint() {
        return srcEndpoint;
    }

    public void setSrcEndpoint(ClientEndpoint srcEndpoint) {
        this.srcEndpoint = srcEndpoint;
    }

    public ClientEndpoint getDistEndpoint() {
        return distEndpoint;
    }

    public void setDistEndpoint(ClientEndpoint distEndpoint) {
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

        List<ObjectUriBO> objectUriBOs = this.selectedObjects;
        for (ObjectUriBO objectUriBO : objectUriBOs) {
            String srcUri = objectUriBO.getUri();
            AbstractObjectWapper<?> srcObject = srcEndpoint.resolve(srcUri);
            AbstractObjectWapper<?> destObject = srcEndpoint.resolve(srcUri);
            LinkWorker linkWorker = new LinkWorker(srcObject, destObject);
            workerList.add(linkWorker);

        }

        for (LinkWorker linkWorker : this.workerList) {
            SyncProgress progress = linkWorker.analyse();

        }

        for (LinkWorker linkWorker : this.workerList) {
            SyncProgress progress = linkWorker.sync();

        }
    }

    public SyncProgress getSyncProgressView() {
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

}