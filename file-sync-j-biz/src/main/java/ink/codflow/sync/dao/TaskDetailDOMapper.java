package ink.codflow.sync.dao;

import ink.codflow.sync.entity.TaskDetailDO;

public interface TaskDetailDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_detail
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_detail
     *
     * @mbg.generated
     */
    int insert(TaskDetailDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_detail
     *
     * @mbg.generated
     */
    int insertSelective(TaskDetailDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_detail
     *
     * @mbg.generated
     */
    TaskDetailDO selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_detail
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(TaskDetailDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table task_detail
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(TaskDetailDO record);
}