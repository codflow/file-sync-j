package ink.codflow.sync.task;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.task.LinkWorker.CopyListener;

public class IncreaseFileWorkerHandler extends AbstractWorkerHandler implements WorkerHandler {

	public static final FileSyncMode SYNC_MODE = FileSyncMode.FILE_INC;

	private static final Logger log = LoggerFactory.getLogger(IncreaseFileWorkerHandler.class);

	@Override
	public void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {

		doSync(srcObject, destObject, null);

	}

	public void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject,
			CopyListener copyListener) {
		try {
			Map<String, ? extends AbstractObjectWapper<?>> destMap = destObject.isDir() ? destObject.mapChildren()
					: null;

			if (srcObject.isDir()) {
				List<? extends AbstractObjectWapper<?>> srcList = srcObject.listChildren();
				for (int i = 0; i < srcList.size(); i++) {
					AbstractObjectWapper<?> srcElement = srcList.get(i);
					String srcBaseName = srcElement.getBaseFileName();
					if (destMap != null && destMap.containsKey(srcBaseName)) {
						AbstractObjectWapper<?> destElement = destMap.get(srcBaseName);
						doSync(srcElement, destElement);
					} else {
						doCopy(srcElement, destObject,copyListener);
					}
				}
			} else if (srcObject.isFile()) {

				String srcBaseName = srcObject.getBaseFileName();

				if ((destMap == null && isDiffFile(srcObject, destObject))
						|| (destMap != null && !destMap.containsKey(srcBaseName))) {
					doCopy(srcObject, destObject,copyListener);
				}
			}
		} catch (FileException e) {
			log.error("analyse error", e);
		}
	}

	@Override
	// src object type: dir,file ;dest object type: dir
	public long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {

		long totalSize = 0;
		try {
			Map<String, ?> destMap = (destObject != null && destObject.isDir()) ? destObject.mapChildren() : null;

			if (srcObject.isDir()) {

				List<?> srcList = srcObject.listChildren();

				for (int i = 0; i < srcList.size(); i++) {
					AbstractObjectWapper<?> srcElement = (AbstractObjectWapper<?>) srcList.get(i);
					String srcBaseName = srcElement.getBaseFileName();
					if (destMap != null && destMap.containsKey(srcBaseName)) {

						AbstractObjectWapper<?> destElement = (AbstractObjectWapper<?>) destMap.get(srcBaseName);
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
				if ((destMap == null && isDiffFile(srcObject, destObject))
						|| (destMap != null && !destMap.containsKey(srcBaseName))) {
					long objectSize = countSize(srcObject);
					totalSize += objectSize;
				}
			}
		} catch (FileException e) {
			log.error("analyse error", e);
		}
		checkAfterAnalyse(srcObject, destObject);
		return totalSize;
	}

	@Override
	public FileSyncMode syncMode() {
		return SYNC_MODE;
	}

}
