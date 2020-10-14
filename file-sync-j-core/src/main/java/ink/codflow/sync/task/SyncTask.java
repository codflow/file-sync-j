package ink.codflow.sync.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ink.codflow.sync.manager.*;
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
import ink.codflow.sync.exception.FileException;

public class SyncTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(SyncTask.class);

    private static final Map<FileSyncMode, WorkerHandler> handlerMap = new HashMap<FileSyncMode, WorkerHandler>();
    static {
        // register sync mode handler
        handlerMap.put(FileSyncMode.FILE_INC, new IncreaseFileWorkerHandler());
        handlerMap.put(FileSyncMode.SYNC, new SyncFileWorkerHandler());
        handlerMap.put(FileSyncMode.META_OPTIMIZED, new MetaOptFileWorkerHandler());
    }

    String id;

    FileSyncMode mode;

    TaskSpecs specs;

    List<TaskStatusListener> taskStatusListeners;

    ClientEndpoint<?> srcEndpoint;
    ClientEndpoint<?> destEndpoint;

    List<LinkWorker> workerList = new ArrayList<LinkWorker>();

    List<SyncTask> subTaskList = new ArrayList<SyncTask>();

    SyncProgress syncProgressView = new SyncProgress();

    List<FileObject> selectedObjects;

    public SyncTask(TaskSpecs specs) {
        this();
        this.specs = specs;

    }

    public SyncTask() {

        this.workerList = new ArrayList<LinkWorker>();

        this.subTaskList = new ArrayList<SyncTask>();

        this.syncProgressView = new SyncProgress();
    }

    public ClientEndpoint<?> getSrcEndpoint() {
        return srcEndpoint;
    }

    public void setSrcEndpoint(ClientEndpoint<?> srcEndpoint) {
        this.srcEndpoint = srcEndpoint;
    }

    public ClientEndpoint<?> getDestEndpoint() {
        return destEndpoint;
    }

    public void setDistEndpoint(ClientEndpoint<?> destEndpoint) {
        this.destEndpoint = destEndpoint;
    }

    public List<LinkWorker> getWorkerList() {
        return workerList;
    }

    public List<TaskStatusListener> getTaskStatusListeners() {
        return taskStatusListeners;
    }

    public void setTaskStatusListeners(List<TaskStatusListener> taskStatusListeners) {
        this.taskStatusListeners = taskStatusListeners;
    }

    public void setWorkerList(List<LinkWorker> workerList) {
        this.workerList = workerList;
    }

    @Override
    public void run() {
        try {

            if (!getSubTaskList().isEmpty()) {
                List<SyncTask> tasks = getSubTaskList();
                doRunMultiTask(tasks);
            } else {
                doRunATask(this);
            }
        } catch (Exception e) {
            handleStateChange(getSyncProgressView(), SyncStatusEnum.FAILED);
            log.error("task error", e);
        }
    }

    void doRunMultiTask(List<SyncTask> tasks) throws FileException {

        for (SyncTask syncTask : tasks) {
            doPrepareATask(syncTask);
        }
        for (SyncTask syncTask : tasks) {
            doAnalyzeTask(syncTask);
        }
        for (SyncTask syncTask : tasks) {
            doSyncATask(syncTask);
        }

    }

    void doPrepareATask(SyncTask task) throws FileException {
        List<FileObject> objectUriBOs = task.selectedObjects;
        if (objectUriBOs != null && !objectUriBOs.isEmpty()) {
            ArrayList<SimpleObject> simpleObjects = new ArrayList<SimpleObject>();

            AbstractObjectWapper<?> srcObject = task.srcEndpoint.resolve(task.srcEndpoint.getRoot());

            AbstractObjectWapper<?> destObject = task.destEndpoint.resolve(task.destEndpoint.getRoot());
            SelectedLinkWorker linkWorker = new SelectedLinkWorker(srcObject, destObject, simpleObjects);
            linkWorker.setSpecs(specs);
            for (FileObject objectBO : objectUriBOs) {

                String uri = objectBO.getUri();
                boolean file = objectBO.getFile();

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
            AbstractObjectWapper<?> destObject = task.destEndpoint.resolve(task.destEndpoint.getRoot());

            LinkWorker linkWorker = new LinkWorker(srcObject, destObject);
            linkWorker.setSpecs(specs);
            FileSyncMode mode0 = task.getMode();
            WorkerHandler handler = getHandler(mode0);
            linkWorker.setWorkerHandler(handler);
            task.workerList.add(linkWorker);
        }
    }

    void doAnalyzeTask(SyncTask task) throws FileException {

        for (LinkWorker linkWorker : task.workerList) {
            linkWorker.analyse();
        }

        if (!handleStateChange(getSyncProgressView(), getSyncProgressView().getStatus())) {

            if (task.workerList != null) {
                for (LinkWorker linkWorker : task.workerList) {
                    linkWorker.forceUpdateStatus(SyncStatusEnum.FAILED);
                }
            }
            if (task.subTaskList != null) {
                for (SyncTask syncTask0 : task.subTaskList) {
                    syncTask0.forceUpdateStatus(SyncStatusEnum.FAILED);
                }
            }
            throw new BackupInterruptException();
        }
        ;
    }

    boolean handleStateChange(SyncProgress progress, SyncStatusEnum status) {

        boolean result = true;

        if (this.taskStatusListeners != null && !this.taskStatusListeners.isEmpty()) {

            for (TaskStatusListener taskStatusListener : this.taskStatusListeners) {
                if (taskStatusListener.statusChange(progress, status)) {
                    continue;
                }
                result = false;
            }
        }
        return result;
    }

    void doSyncATask(SyncTask task) {
        if (!SyncStatusEnum.FAILED.equals(getSyncProgressView().getStatus())) {
            for (LinkWorker linkWorker : task.workerList) {
                linkWorker.sync();
            }

        }

        SyncStatusEnum status = getSyncProgressView().getStatus();
        handleStateChange(getSyncProgressView(), status);

    }

    void doRunATask(SyncTask task) {

        try {

            List<FileObject> objectUriBOs = task.selectedObjects;
            if (objectUriBOs != null && !objectUriBOs.isEmpty()) {
                ArrayList<SimpleObject> simpleObjects = new ArrayList<SimpleObject>();

                AbstractObjectWapper<?> srcObject = task.srcEndpoint.resolve(task.srcEndpoint.getRoot());

                AbstractObjectWapper<?> destObject = task.destEndpoint.resolve(task.destEndpoint.getRoot());
                SelectedLinkWorker linkWorker = new SelectedLinkWorker(srcObject, destObject, simpleObjects);
                linkWorker.setSpecs(specs);
                for (FileObject objectBO : objectUriBOs) {

                    String uri = objectBO.getUri();
                    boolean file = objectBO.getFile();

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
                AbstractObjectWapper<?> destObject = task.destEndpoint.resolve(task.destEndpoint.getRoot());

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

            if (!handleStateChange(getSyncProgressView(), getSyncProgressView().getStatus())) {
                if (task.workerList != null) {
                    for (LinkWorker linkWorker : task.workerList) {
                        linkWorker.forceUpdateStatus(SyncStatusEnum.FAILED);
                    }
                }
                if (task.subTaskList != null) {
                    for (SyncTask syncTask0 : task.subTaskList) {
                        syncTask0.forceUpdateStatus(SyncStatusEnum.FAILED);
                    }
                }
                throw new BackupInterruptException();
            }

            if (!SyncStatusEnum.FAILED.equals(getSyncProgressView().getStatus())) {
                for (LinkWorker linkWorker : task.workerList) {
                    linkWorker.sync();
                }

            }

            SyncStatusEnum status = getSyncProgressView().getStatus();
            handleStateChange(getSyncProgressView(), status);

        } catch (Exception e) {

            handleStateChange(getSyncProgressView(), SyncStatusEnum.FAILED);
            log.error("task error0", e);
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
        int taskStatusOrgin = 99;
        for (SyncTask syncTask : subTasks) {
            SyncProgress syncProgress = syncTask.getSyncProgressView();
            syncedSize += syncProgress.getSyncedSize();
            analyseSize += syncProgress.getAnalyseSize();
            syncedFileCount += syncProgress.getSyncedFileCount();
            analyseFileCount += syncProgress.getAnalyseFileCount();
            totalDestSize += syncProgress.getTotalDestSize();
            SyncStatusEnum status = syncProgress.getStatus();
            if (status.ordinal() < taskStatusOrgin) {
                taskStatusOrgin = status.ordinal();
            }
        }

        SyncStatusEnum finalStatus = SyncStatusEnum.resolveOrigin(taskStatusOrgin);

        SyncProgress syncProgressView0 = new SyncProgress(syncedSize, analyseSize, syncedFileCount, analyseFileCount,
                totalDestSize);

        syncProgressView0.setStatus(finalStatus);
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
        int taskStatusOrgin = 99;
        for (LinkWorker linkWorker : workers) {
            SyncProgress syncProgress = linkWorker.getProgress();
            syncedSize += syncProgress.getSyncedSize();
            analyseSize += syncProgress.getAnalyseSize();
            syncedFileCount += syncProgress.getSyncedFileCount();
            analyseFileCount += syncProgress.getAnalyseFileCount();
            totalDestSize += syncProgress.getTotalDestSize();
            SyncStatusEnum status = syncProgress.getStatus();
            if (status.ordinal() < taskStatusOrgin) {
                taskStatusOrgin = status.ordinal();
            }
        }
        SyncProgress syncProgressView0 = new SyncProgress(syncedSize, analyseSize, syncedFileCount, analyseFileCount,
                totalDestSize);
        SyncStatusEnum finalStatus = SyncStatusEnum.resolveOrigin(taskStatusOrgin);
        syncProgressView0.setStatus(finalStatus);
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

    public List<FileObject> getSelectedObjects() {
        return selectedObjects;
    }

    public void setSelectedObjects(List<FileObject> selectedObjects) {
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

    public void forceUpdateStatus(SyncStatusEnum status) {
        this.syncProgressView.setStatus(status);
    }

}