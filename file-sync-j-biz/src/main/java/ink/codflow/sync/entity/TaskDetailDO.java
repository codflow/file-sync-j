package ink.codflow.sync.entity;

import java.io.Serializable;

public class TaskDetailDO implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_detail.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_detail.task_id
     *
     * @mbg.generated
     */
    private String taskId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_detail.progress
     *
     * @mbg.generated
     */
    private Integer progress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_detail.total_size
     *
     * @mbg.generated
     */
    private Long totalSize;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_detail.allocated_size
     *
     * @mbg.generated
     */
    private Long allocatedSize;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_detail.increased_size
     *
     * @mbg.generated
     */
    private Long increasedSize;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_detail.transfered_size
     *
     * @mbg.generated
     */
    private Long transferedSize;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_detail.status
     *
     * @mbg.generated
     */
    private String status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_detail.create_time
     *
     * @mbg.generated
     */
    private Long createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_detail.update_time
     *
     * @mbg.generated
     */
    private Long updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column task_detail.end_time
     *
     * @mbg.generated
     */
    private Long endTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table task_detail
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_detail.id
     *
     * @return the value of task_detail.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_detail.id
     *
     * @param id the value for task_detail.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_detail.task_id
     *
     * @return the value of task_detail.task_id
     *
     * @mbg.generated
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_detail.task_id
     *
     * @param taskId the value for task_detail.task_id
     *
     * @mbg.generated
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_detail.progress
     *
     * @return the value of task_detail.progress
     *
     * @mbg.generated
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_detail.progress
     *
     * @param progress the value for task_detail.progress
     *
     * @mbg.generated
     */
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_detail.total_size
     *
     * @return the value of task_detail.total_size
     *
     * @mbg.generated
     */
    public Long getTotalSize() {
        return totalSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_detail.total_size
     *
     * @param totalSize the value for task_detail.total_size
     *
     * @mbg.generated
     */
    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_detail.allocated_size
     *
     * @return the value of task_detail.allocated_size
     *
     * @mbg.generated
     */
    public Long getAllocatedSize() {
        return allocatedSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_detail.allocated_size
     *
     * @param allocatedSize the value for task_detail.allocated_size
     *
     * @mbg.generated
     */
    public void setAllocatedSize(Long allocatedSize) {
        this.allocatedSize = allocatedSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_detail.increased_size
     *
     * @return the value of task_detail.increased_size
     *
     * @mbg.generated
     */
    public Long getIncreasedSize() {
        return increasedSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_detail.increased_size
     *
     * @param increasedSize the value for task_detail.increased_size
     *
     * @mbg.generated
     */
    public void setIncreasedSize(Long increasedSize) {
        this.increasedSize = increasedSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_detail.transfered_size
     *
     * @return the value of task_detail.transfered_size
     *
     * @mbg.generated
     */
    public Long getTransferedSize() {
        return transferedSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_detail.transfered_size
     *
     * @param transferedSize the value for task_detail.transfered_size
     *
     * @mbg.generated
     */
    public void setTransferedSize(Long transferedSize) {
        this.transferedSize = transferedSize;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_detail.status
     *
     * @return the value of task_detail.status
     *
     * @mbg.generated
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_detail.status
     *
     * @param status the value for task_detail.status
     *
     * @mbg.generated
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_detail.create_time
     *
     * @return the value of task_detail.create_time
     *
     * @mbg.generated
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_detail.create_time
     *
     * @param createTime the value for task_detail.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_detail.update_time
     *
     * @return the value of task_detail.update_time
     *
     * @mbg.generated
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_detail.update_time
     *
     * @param updateTime the value for task_detail.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column task_detail.end_time
     *
     * @return the value of task_detail.end_time
     *
     * @mbg.generated
     */
    public Long getEndTime() {
        return endTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column task_detail.end_time
     *
     * @param endTime the value for task_detail.end_time
     *
     * @mbg.generated
     */
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}