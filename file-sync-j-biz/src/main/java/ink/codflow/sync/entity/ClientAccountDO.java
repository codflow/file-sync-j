package ink.codflow.sync.entity;

import java.io.Serializable;
import java.util.Date;

public class ClientAccountDO implements Serializable {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column fk_client_account.id
     * @mbg.generated
     */
    private Integer id;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column fk_client_account.client_id
     * @mbg.generated
     */
    private Integer clientId;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column fk_client_account.account_id
     * @mbg.generated
     */
    private String accountId;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column fk_client_account.type
     * @mbg.generated
     */
    private String type;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column fk_client_account.create_time
     * @mbg.generated
     */
    private Date createTime;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column fk_client_account.update_time
     * @mbg.generated
     */
    private Date updateTime;
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database table fk_client_account
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column fk_client_account.id
     * @return  the value of fk_client_account.id
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column fk_client_account.id
     * @param id  the value for fk_client_account.id
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column fk_client_account.client_id
     * @return  the value of fk_client_account.client_id
     * @mbg.generated
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column fk_client_account.client_id
     * @param clientId  the value for fk_client_account.client_id
     * @mbg.generated
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column fk_client_account.account_id
     * @return  the value of fk_client_account.account_id
     * @mbg.generated
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column fk_client_account.account_id
     * @param accountId  the value for fk_client_account.account_id
     * @mbg.generated
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column fk_client_account.type
     * @return  the value of fk_client_account.type
     * @mbg.generated
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column fk_client_account.type
     * @param type  the value for fk_client_account.type
     * @mbg.generated
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column fk_client_account.create_time
     * @return  the value of fk_client_account.create_time
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column fk_client_account.create_time
     * @param createTime  the value for fk_client_account.create_time
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column fk_client_account.update_time
     * @return  the value of fk_client_account.update_time
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column fk_client_account.update_time
     * @param updateTime  the value for fk_client_account.update_time
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}