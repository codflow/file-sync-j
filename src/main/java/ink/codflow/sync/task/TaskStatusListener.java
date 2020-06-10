package ink.codflow.sync.task;

import ink.codflow.sync.consts.SyncStatusEnum;
import ink.codflow.sync.core.SyncProgress;

public interface TaskStatusListener {
    boolean statusChange(SyncProgress progress, SyncStatusEnum status);
}
