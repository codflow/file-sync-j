package ink.codflow.sync.dto;

public class LinkDTO {

    
    Integer sourceId;
    
    Integer targetId;

    String mode;
    
    Long createTime;
    Long updateTime;
    public Integer getSourceId() {
        return sourceId;
    }
    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }
    public Integer getTargetId() {
        return targetId;
    }
    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }
    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
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
    
    
}