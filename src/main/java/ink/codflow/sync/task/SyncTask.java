package ink.codflow.sync.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.sync.bo.ObjectBO;
import ink.codflow.sync.consts.FileSyncMode;
import ink.codflow.sync.consts.SyncStatusEnum;
import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.core.ClientEndpoint;
import ink.codflow.sync.core.SyncProgress;
import ink.codflow.sync.core.handler.IncreaseFileWorkerHandler;
import ink.codflow.sync.core.handler.MetaOptFileWorkerHandler;
import ink.codflow.sync.core.handler.SyncFileWorkerHandler;
import ink.codflow.sync.core.handler.WorkerHandler;
import ink.codflow.sync.exception.BackupInterruptException;

public class SyncTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(SyncTask.class);

    private static final Map<FileSyncMode, WorkerHandler> handlerMap = new HashMap<FileSyncMode, WorkerHandler>();
    static {
        // register sync mode handler
        handlerMap.put(FileSyncMode.FILE_INC, new IncreaseFileWorkerHandler());
        handlerMap.put(FileSyncMode.SYNC, new SyncFileWorkerHandler());
        handlerMap.put(FileSyncMode.META_OPTIMIZED, new MetaOptFileWorkerHandler());
    }

    TaskStatusListener taskStatusListener;

    ClientEndpoint<?> srcEndpoint;
    ClientEndpoint<?> distEndpoint;

    List<LinkWorker> workerList = new ArrayList<LinkWorker>();

    List<SyncTask> subTaskList = new ArrayList<SyncTask>();

    SyncProgress syncProgressView = new SyncProgress();

    FileSyncMode mode;

    List<ObjectBO> selectedObjects;

    String id;

    TaskSpecs specs;

    public SyncTask(TaskSpecs specs) {
        this.specs = specs;
    }

    public SyncTask() {

    }

    public ClientEndpoint<?> getSrcEndpoint() {
        return srcEndpoint;
    }

    public void setSrcEndpoint(ClientEndpoint<?> srcEndpoint) {
        this.srcEndpoint = srcEndpoint;
    }

    public ClientEndpoint<?> getDistEndpoint() {
        return distEndpoint;
    }

    public void setDistEndpoint(ClientEndpoint<?> distEndpoint) {
        this.distEndpoint = distEndpoint;
    }

    public List<LinkWorker> getWorkerList() {
        return workerList;
    }

    
    public TaskStatusListener getTaskStatusListener() {
        return taskStatusListener;
    }

    public void setTaskStatusListener(TaskStatusListener taskStatusListener) {
        this.taskStatusListener = taskStatusListener;
    }

    public void setWorkerList(List<LinkWorker> workerList) {
        this.workerList = workerList;
    }

    @Override
    public void run() {
        try {

            if (!getSubTaskList().isEmpty()) {
                List<SyncTask> tasks = getSubTaskList();
                for (SyncTask syncTask : tasks) {
                    doRunATask(syncTask);
                }
            } else {
                doRunATask(this);
            }
        } catch (Exception e) {
            log.error("task error", e);
        }
    }

    void doRunATask(SyncTask task) {

        try {

            List<ObjectBO> objectUriBOs = task.selectedObjects;
            if (objectUriBOs != null && !objectUriBOs.isEmpty()) {
                ArrayList<SimpleObject> simpleObjects = new ArrayList<SimpleObject>();

                AbstractObjectWapper<?> srcObject = task.srcEndpoint.resolve(task.srcEndpoint.getRoot());
                AbstractObjectWapper<?> destObject = task.distEndpoint.resolve(task.distEndpoint.getRoot());
                SelectedLinkWorker linkWorker = new SelectedLinkWorker(srcObject, destObject, simpleObjects);
                linkWorker.setSpecs(specs);
                for (ObjectBO objectBO : objectUriBOs) {

                    String uri = objectBO.getUri();
                    boolean file = objectBO.isFile();

                    SimpleObject simpleObject = new SimpleObject();
                    simpleObject.setDir(!file);
                    simpleObject.setPath(uri);
                    simpleObjects.add(simpleObject);
                }
                FileSyncMode mode0 = task.getMode();
                WorkerHandler handler = getHandler(mode0);
                linkWorker.setWorkerHandler(handler);
                task.workerList.add(linkWorker);

            } else {

                AbstractObjectWapper<?> srcObject = task.srcEndpoint.resolve(task.srcEndpoint.getRoot());
                AbstractObjectWapper<?> destObject = task.distEndpoint.resolve(task.distEndpoint.getRoot());

                LinkWorker linkWorker = new LinkWorker(srcObject, destObject);
                linkWorker.setSpecs(specs);
                FileSyncMode mode0 = task.getMode();
                WorkerHandler handler = getHandler(mode0);
                linkWorker.setWorkerHandler(handler);
                task.workerList.add(linkWorker);

            }

            for (LinkWorker linkWorker : task.workerList) {
                linkWorker.analyse();
            }

            if (taskStatusListener != null) {
                if (!taskStatusListener.statusChange(getSyncProgressView(), getSyncProgressView().getStatus())) {
                    throw new BackupInterruptException();
                }
            }

            for (LinkWorker linkWorker : task.workerList) {
                linkWorker.sync();
            }

            if (taskStatusListener != null) {
                SyncStatusEnum status = getSyncProgressView().getStatus();
                taskStatusListener.statusChange(getSyncProgressView(), status);
            }

        } catch (Exception e) {
            log.error("task error", e);
        }

    }

    WorkerHandler getHandler(FileSyncMode mode) {
        return handlerMap.get(mode);
    }

    public SyncProgress getSyncProgressView() {

        if (!this.subTaskList.isEmpty()) {
            return getSubTaskProgressView();
        } else if (!this.workerList.isEmpty()) {
            return getWorkerProgressView();
        } else {
            return this.syncProgressView;
        }

    }

    SyncProgress getSubTaskProgressView() {
        List<SyncTask> subTasks = getSubTaskList();
        long syncedSize = 0;
        long analyseSize = 0;
        long syncedFileCount = 0;
        long analyseFileCount = 0;
        long totalDestSize = 0;
        for (SyncTask syncTask : subTasks) {
            SyncProgress syncProgress = syncTask.getSyncProgressView();
            syncedSize += syncProgress.getSyncedSize();
            analyseSize += syncProgress.getAnalyseSize();
            syncedFileCount += syncProgress.getSyncedFileCount();
            analyseFileCount += syncProgress.getAnalyseFileCount();
            totalDestSize += syncProgress.getTotalDestSize();
        }
        SyncProgress syncProgressView0 = new SyncProgress(syncedSize, analyseSize, syncedFileCount, analyseFileCount,
                totalDestSize);
        this.syncProgressView = syncProgressView0;
        return syncProgressView0;

    }

    SyncProgress getWorkerProgressView() {

        List<LinkWorker> workers = getWorkerList();
        long syncedSize = 0;
        long analyseSize = 0;
        long syncedFileCount = 0;
        long analyseFileCount = 0;
        long totalDestSize = 0;

        for (LinkWorker linkWorker : workers) {
            SyncProgress syncProgress = linkWorker.getProgress();
            syncedSize += syncProgress.getSyncedSize();
            analyseSize += syncProgress.getAnalyseSize();
            syncedFileCount += syncProgress.getSyncedFileCount();
            analyseFileCount += syncProgress.getAnalyseFileCount();
            totalDestSize += syncProgress.getTotalDestSize();

        }
        SyncProgress syncProgressView0 = new SyncProgress(syncedSize, analyseSize, syncedFileCount, analyseFileCount,
                totalDestSize);
        this.syncProgressView = syncProgressView0;
        return syncProgressView0;
    }

    public List<SyncTask> getSubTaskList() {
        return subTaskList;
    }

    public void addSubTask(SyncTask subTask) {
        this.subTaskList.add(subTask);
    }

    public FileSyncMode getMode() {
        return mode;
    }

    public void setMode(FileSyncMode mode) {
        this.mode = mode;
    }

    public List<ObjectBO> getSelectedObjects() {
        return selectedObjects;
    }

    public void setSelectedObjects(List<ObjectBO> selectedObjects) {
        this.selectedObjects = selectedObjects;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TaskSpecs getSpecs() {
        return specs;
    }

    public void setSpecs(TaskSpecs specs) {
        this.specs = specs;
    }

    
}