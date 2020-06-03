package ink.codflow.sync.task;

import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.task.LinkWorker.AnalyseListener;
import ink.codflow.sync.task.LinkWorker.CopyListener;

public abstract class AbstractWorkerHandler implements WorkerHandler {

	protected void doCopy(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject,
			CopyListener copyListener) throws FileException {
		destObject.copyFrom(srcObject);
		if (copyListener != null) {
			copyListener.doRecord(srcObject);
		}
	}
	
	protected long countSize(AbstractObjectWapper<?> srcElement, AnalyseListener listener) throws FileException {
		long size = countSize(srcElement);
		if (listener != null) {
			listener.doRecord(srcElement);

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

	public abstract FileSyncMode syncMode();

}
