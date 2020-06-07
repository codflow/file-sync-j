package ink.codflow.sync.core.handler;

import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.task.LinkWorker;
import ink.codflow.sync.task.TaskSpecs;
import ink.codflow.sync.task.LinkWorker.AnalyseListener;
import ink.codflow.sync.task.LinkWorker.SyncListener;

public abstract class AbstractWorkerHandler implements WorkerHandler {

	protected void doCopy(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject,
			SyncListener copyListener) throws FileException {
		destObject.copyFrom(srcObject);
		if (copyListener != null) {
			copyListener.doRecordDiff(srcObject);
		}
	}
	
	protected long countSize(AbstractObjectWapper<?> srcElement, AnalyseListener listener) throws FileException {
		long size = countSize(srcElement);
		if (listener != null) {
			listener.doRecordDiff(srcElement);

		}
		return size;
	}

	protected boolean isDiffFile(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject)
			throws FileException {
		// is double check necessary?
		return srcObject.isDiff(destObject);
	}

	protected long countSize(AbstractObjectWapper<?> srcObject) throws FileException {

		return srcObject.getSize();

	}

	protected void checkAfterAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {
		// do nothing in single thread mode
	}

	protected void checkAfterSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {
		// do nothing  
		
	}
	
	public abstract FileSyncMode syncMode();


    @Override
    public void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject, SyncListener listener,
            TaskSpecs specs) {
        
        //ignore spec
        doSync(srcObject, destObject, listener);
        
    }


}
