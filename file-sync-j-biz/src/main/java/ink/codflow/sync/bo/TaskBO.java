package ink.codflow.sync.bo;

import java.time.LocalDateTime;
import java.util.List;


public class TaskBO {

    String id;

    String linkId;

    String traceId;

    LocalDateTime createTime;
    
    LocalDateTime updateTime;
    
    TaskDetailBO taskDetail;

    List<String> traceIdList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public TaskDetailBO getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(TaskDetailBO taskDetail) {
        this.taskDetail = taskDetail;
    }

    public List<String> getTraceIdList() {
        return traceIdList;
    }

    public void setTraceIdList(List<String> traceIdList) {
        this.traceIdList = traceIdList;
    }


    

}
