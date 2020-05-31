package ink.codflow.sync.task;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.bo.ObjectBO;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.core.ClientEndpoint;
import ink.codflow.sync.core.SyncProgress;

public class SyncTask implements Runnable {

	
	private static final Logger log = LoggerFactory.getLogger(SyncTask.class);

	
    ClientEndpoint srcEndpoint;
    ClientEndpoint distEndpoint;
    List<LinkWorker> workerList = new ArrayList<LinkWorker>();
    SyncProgress syncProgressView = new SyncProgress();
    List<ObjectBO> selectedObjects;
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
    	try {
            List<ObjectBO> objectUriBOs = this.selectedObjects;
            if (objectUriBOs!=null && !objectUriBOs.isEmpty()) {
                for (ObjectBO objectUriBO : objectUriBOs) {
                    String srcUri = objectUriBO.getUri();
                    AbstractObjectWapper<?> srcObject = srcEndpoint.resolve(srcUri);
                    AbstractObjectWapper<?> destObject = distEndpoint.resolve(srcUri);
                    LinkWorker linkWorker = new LinkWorker(srcObject, destObject);
                    workerList.add(linkWorker);

                }
    		}else {
                AbstractObjectWapper<?> srcObject = srcEndpoint.resolve(srcEndpoint.getRoot());
                AbstractObjectWapper<?> destObject = distEndpoint.resolve(distEndpoint.getRoot());
                LinkWorker linkWorker = new LinkWorker(srcObject, destObject);
                workerList.add(linkWorker);

    		}

            for (LinkWorker linkWorker : this.workerList) {
                SyncProgress progress = linkWorker.analyse();

            }

            for (LinkWorker linkWorker : this.workerList) {
                SyncProgress progress = linkWorker.sync();

            }
		} catch (Exception e) {
			log.error("tasl error",e);
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