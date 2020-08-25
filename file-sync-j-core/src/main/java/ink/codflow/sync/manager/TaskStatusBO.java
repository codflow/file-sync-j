package ink.codflow.sync.manager;

import ink.codflow.sync.consts.SyncStatusEnum;
import ink.codflow.sync.consts.TaskType;

public class TaskStatusBO {


    String id;
    
    String linkSetId;
    
    String traceId;
    
    long totalSize;
    
    long analyzedSize;
    
    
    TaskType type;
    
    SyncStatusEnum status;
    
    int progress; 
    
    int createTime;
    
    int endTime;

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getLinkSetId() {
        return linkSetId;
    }


    public void setLinkSetId(String linkSetId) {
        this.linkSetId = linkSetId;
    }


    public String getTraceId() {
        return traceId;
    }


    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }


    public SyncStatusEnum getStatus() {
        return status;
    }


    public void setStatus(SyncStatusEnum status) {
        this.status = status;
    }


    public Integer getProgress() {
        return progress;
    }


    public void setProgress(Integer progress) {
        this.progress = progress;
    }


    public Integer getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }


    public Integer getEndTime() {
        return endTime;
    }


    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }


    public Long getTotalSize() {
        return totalSize;
    }


    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }


    public Long getAnalyzedSize() {
        return analyzedSize;
    }


    public void setAnalyzedSize(Long analyzedSize) {
        this.analyzedSize = analyzedSize;
    }


    public TaskType getType() {
        return type;
    }


    public void setType(TaskType type) {
        this.type = type;
    }



    
}
