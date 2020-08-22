package ink.codflow.sync.do;

import java.io.Serializable;

public class TaskDetail implements Serializable {
    private Integer id;

    private String taskId;

    private Integer progress;

    private Long totalSize;

    private Long allocatedSize;

    private Long increasedSize;

    private Long transferedSize;

    private String status;

    private Long createTime;

    private Long updateTime;

    private Long endTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Long getAllocatedSize() {
        return allocatedSize;
    }

    public void setAllocatedSize(Long allocatedSize) {
        this.allocatedSize = allocatedSize;
    }

    public Long getIncreasedSize() {
        return increasedSize;
    }

    public void setIncreasedSize(Long increasedSize) {
        this.increasedSize = increasedSize;
    }

    public Long getTransferedSize() {
        return transferedSize;
    }

    public void setTransferedSize(Long transferedSize) {
        this.transferedSize = transferedSize;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}