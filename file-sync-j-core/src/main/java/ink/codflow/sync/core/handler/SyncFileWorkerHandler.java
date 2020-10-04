package ink.codflow.sync.core.handler;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.exception.RemotingException;
import ink.codflow.sync.task.TaskSpecs;
import ink.codflow.sync.task.LinkWorker.AnalyseListener;
import ink.codflow.sync.task.LinkWorker.SyncListener;

public class SyncFileWorkerHandler extends AbstractWorkerHandler  {

    private static final Logger log = LoggerFactory.getLogger(SyncFileWorkerHandler.class);
    public static final FileSyncMode SYNC_MODE = FileSyncMode.SYNC;

    @Override
    public long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {
        return doAnalyse(srcObject, destObject, null,null);

    }

    @Override
    public long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject,
            AnalyseListener listener,TaskSpecs specs){

                return doAnalyse(srcObject,destObject,listener);
            }



    
    public long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject,
            AnalyseListener listener) {

        long totalSize = 0;
        try {
            Map<String, ?> destMap = (destObject != null && destObject.isExist() && destObject.isDir())
                    ? destObject.mapChildren()
                    : null;

            if (srcObject.isDir()) {

                List<?> srcList = srcObject.listChildren();

                for (int i = 0; i < srcList.size(); i++) {
                    AbstractObjectWapper<?> srcElement = (AbstractObjectWapper<?>) srcList.get(i);
                    String srcBaseName = srcElement.getBaseFileName();
                    if (destMap != null && destMap.containsKey(srcBaseName)) {

                        AbstractObjectWapper<?> destElement = (AbstractObjectWapper<?>) destMap.get(srcBaseName);
                        totalSize += doAnalyse(srcElement, destElement, listener);
                    } else {
                        //
                        if (srcElement.isFile()) {
                            // TODO compare ts
                            totalSize += countSize(srcElement, listener);
                        } else {
                            AbstractObjectWapper<?> destElement0 = destObject.createChild(srcBaseName, true);
                            totalSize += doAnalyse(srcElement, destElement0, listener);
                        }
                    }
                }
            } else if (srcObject.isFile()) {
                String srcBaseName = srcObject.getBaseFileName();
                if (!destObject.isExist() || ((destMap == null && isDiffFile(srcObject, destObject)))
                        || (destMap != null && !destMap.containsKey(srcBaseName))) {
                    long objectSize = countSize(srcObject, listener);
                    totalSize += objectSize;
                }
                listener.doRecordFile(srcObject);

            }
            checkAfterAnalyse(srcObject, destObject);
        } catch (FileException e) {
            throw new RemotingException("analyse failure",e);
        }
        return totalSize;

    }

    @Override
    public void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {
        doSync(srcObject, destObject, null);

    }

    @Override
    public void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject, SyncListener listener) {

        try {
            Map<String, ?> destMap = (destObject != null && destObject.isExist() && destObject.isDir())
                    ? destObject.mapChildren()
                    : null;

            if (srcObject.isDir()) {

                List<?> srcList = srcObject.listChildren();

                for (int i = 0; i < srcList.size(); i++) {
                    AbstractObjectWapper<?> srcElement = (AbstractObjectWapper<?>) srcList.get(i);
                    String srcBaseName = srcElement.getBaseFileName();
                    if (destMap != null && destMap.containsKey(srcBaseName)) {

                        AbstractObjectWapper<?> destElement = (AbstractObjectWapper<?>) destMap.get(srcBaseName);
                        doSync(srcElement, destElement, listener);
                        destMap.remove(srcBaseName);
                    } else {
                        //
                        if (srcElement.isFile()) {
                            // TODO compare ts
                            AbstractObjectWapper<?> destElement0 = destObject.createChild(srcBaseName, false);
                            doCopy(srcElement, destElement0, listener);
                            checkAfterSync(srcObject, destObject);
                        } else {
                            AbstractObjectWapper<?> destElement0 = destObject.createChild(srcBaseName, true);
                            doSync(srcElement, destElement0, listener);
                        }
                    }
                }
            } else if (srcObject.isFile()) {
                String srcBaseName = srcObject.getBaseFileName();
                if (!destObject.isExist() || ((destMap == null && isDiffFile(srcObject, destObject)))
                        || (destMap != null && !destMap.containsKey(srcBaseName))) {
                    doCopy(srcObject, destObject, listener);
                }
            }

            doProcessRemainDestFile(destMap);
        } catch (FileException e) {
            throw new RemotingException("analyse failure",e);
        }

    }

    


    void doProcessRemainDestFile(Map<String, ?> destMap) {
        if (destMap != null) {
            Collection<?> objects = destMap.values();
            for (Object object : objects) {
                if (object instanceof AbstractObjectWapper<?>) {
                    AbstractObjectWapper<?> wapper = (AbstractObjectWapper<?>) object;
                    try {
                        wapper.remove();
                    } catch (FileException e) {
                        log.warn("delete remaining file failed");
                    }
                }
            }
        }
    }
    @Override
    protected boolean isDiffFile(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject)
            throws FileException {
        long srcSize = srcObject.getSize();
        long destSize = destObject.getSize();
        long srcTS = srcObject.getLastMod();
        long destTs = destObject.getLastMod();
        return srcSize != destSize || srcTS != destTs;
    }

    @Override
    public FileSyncMode syncMode() {
        return SYNC_MODE;
    }

}
