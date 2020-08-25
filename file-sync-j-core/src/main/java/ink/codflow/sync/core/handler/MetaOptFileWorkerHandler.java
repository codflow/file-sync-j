package ink.codflow.sync.core.handler;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.consts.TaskSpecType;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.exception.RemotingException;
import ink.codflow.sync.task.LinkWorker.AnalyseListener;
import ink.codflow.sync.task.LinkWorker.SyncListener;
import ink.codflow.sync.task.TaskSpecs;

public class MetaOptFileWorkerHandler extends AbstractWorkerHandler  {

    private static final Logger log = LoggerFactory.getLogger(MetaOptFileWorkerHandler.class);
    public static final FileSyncMode SYNC_MODE0 = FileSyncMode.META_OPTIMIZED;

    private static final long DEFAULT_EXPIRE = 604800L;

    @Override
    public long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject) {
        return doAnalyse(srcObject, destObject, null,null);

    }

 
    public long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject,
            AnalyseListener listener,long expire) {

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
                        totalSize += doAnalyse(srcElement, destElement, listener,expire);
                        destMap.remove(srcBaseName);
                    } else {
                        //
                        if (srcElement.isFile()) {
                            // TODO compare ts
                            totalSize += countSize(srcElement, listener);
                        } else {
                            AbstractObjectWapper<?> destElement0 = destObject.createChild(srcBaseName, true);
                            totalSize += doAnalyse(srcElement, destElement0, listener,expire);
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
                // record total size
                listener.doRecordFile(srcObject);
            }
            doProcessRemainDestFileInAnalyse(destMap, expire,listener);
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
    public void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject, SyncListener listener,
            TaskSpecs specs) {
        long expire = DEFAULT_EXPIRE;
        if (specs != null) {
            String expireS = specs.getSpec(TaskSpecType.EXPIRE);
            if (expireS != null) {
                expire = Long.valueOf(expireS);
            }
        }
        doSync(srcObject, destObject, listener, expire);
    }

    @Override
    public void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject, SyncListener listener) {

        doSync(srcObject, destObject, listener, DEFAULT_EXPIRE);

    }

    public void doSync(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject, SyncListener listener,
            long expire) {

        try {

            boolean isDir = (destObject != null && destObject.isExist() && destObject.isDir());
            if (isDir) {
                long timestamp = System.currentTimeMillis();
                destObject.setTimeStamp(timestamp);
            }
            Map<String, ?> destMap = isDir
                    ? destObject.mapChildren()
                    : null;
            if (srcObject.isExist()) {

                if (srcObject.isDir()) {

                    List<?> srcList = srcObject.listChildren();

                    for (int i = 0; i < srcList.size(); i++) {
                        AbstractObjectWapper<?> srcElement = (AbstractObjectWapper<?>) srcList.get(i);
                        String srcBaseName = srcElement.getBaseFileName();
                        if (destMap != null && destMap.containsKey(srcBaseName)) {

                            AbstractObjectWapper<?> destElement = (AbstractObjectWapper<?>) destMap.get(srcBaseName);
                            doSync(srcElement, destElement, listener,expire);
                            destMap.remove(srcBaseName);
                        } else {
                            //
                            if (srcElement.isFile()) {
                                // TODO compare ts
                                AbstractObjectWapper<?> destElement0 = destObject.createChild(srcBaseName, false);
                                log.debug("copy:" + srcBaseName);
                                doCopy(srcElement, destElement0, listener);
                                checkAfterSync(srcObject, destObject);
                            } else {
                                AbstractObjectWapper<?> destElement0 = destObject.createChild(srcBaseName, true);
                                doSync(srcElement, destElement0, listener,expire);
                            }
                        }
                    }
                } else if (srcObject.isFile()) {
                    String srcBaseName = srcObject.getBaseFileName();
                    if (!destObject.isExist() || ((destMap == null && isDiffFile(srcObject, destObject)))
                            || (destMap != null && !destMap.containsKey(srcBaseName))) {
                        log.debug("copy:" + srcBaseName);
                        doCopy(srcObject, destObject, listener);
                    }
                    if (destObject.isExist()) {
                        destObject.setTimeStamp(currentTimestamp());
                    }
                }
            }
            doProcessRemainDestFile(destMap, expire);
        } catch (FileException e) {
            throw new RemotingException("sync failure",e);
            // if (msg.contains("not close the input stream")) {
            //     throw new RemotingException("connection lost");
            // }
        }

    }

    void doProcessRemainDestFileInAnalyse(Map<String, ?> destMap, long expire,AnalyseListener listener) {
        if (destMap != null) {

            Collection<?> objects = destMap.values();
            for (Object object : objects) {
                if (object instanceof AbstractObjectWapper<?>) {
                    AbstractObjectWapper<?> wapper = (AbstractObjectWapper<?>) object;
                    try {
                        if (!wapper.isDir()) {
                            if (doCheckExpired(wapper.getLastMod(), expire)) {
                            }else{
                                listener.doRecordFile(wapper);
                            }
                        } else {
                            Map<String, ?> map = wapper.mapChildren();
                            doProcessRemainDestFileInAnalyse(map, expire,listener);
                        }
                    } catch (FileException e) {
                        log.warn("analyse remaining file failed");
                    }
                }
            }
        }
    }



    void doProcessRemainDestFile(Map<String, ?> destMap, long expire) {
        if (destMap != null) {

            Collection<?> objects = destMap.values();
            for (Object object : objects) {
                if (object instanceof AbstractObjectWapper<?>) {
                    AbstractObjectWapper<?> wapper = (AbstractObjectWapper<?>) object;
                    try {
                        if (!wapper.isDir()) {
                            if (doCheckExpired(wapper.getLastMod(), expire)) {
                                wapper.remove();
                            }
                        } else {
                            Map<String, ?> map = wapper.mapChildren();
                            doProcessRemainDestFile(map, expire);
                        }

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
        return srcSize != destSize || srcTS < destTs;
    }


    public boolean doCheckExpired(long targetUnixtime, long expire) {
        long currentUnixtime = System.currentTimeMillis();
        return currentUnixtime - targetUnixtime > expire * 1000;
    }

    @Override
    public FileSyncMode syncMode() {
        return SYNC_MODE0;
    }


    long currentTimestamp(){
        return System.currentTimeMillis();
    }

    @Override
    public long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject,
            AnalyseListener listener, TaskSpecs specs) {
                String expireS = specs.getSpec(TaskSpecType.EXPIRE);
                Long expire = 0L;
				if (expireS != null) {
					try {
						expire = 	Long.valueOf(expire);
					} catch (Exception e) {
                        log.error("expire num parse failed", e);
					}
				}
        return doAnalyse(srcObject,destObject,listener,expire);
    }

    @Override
    public long doAnalyse(AbstractObjectWapper<?> srcObject, AbstractObjectWapper<?> destObject,
            AnalyseListener listener) {
                
        return  doAnalyse(srcObject,destObject,listener,0);
    }

}
