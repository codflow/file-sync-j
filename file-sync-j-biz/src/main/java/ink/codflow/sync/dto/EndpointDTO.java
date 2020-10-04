package ink.codflow.sync.dto;

/**
 * @author SH
 *
 * @date Aug 29, 2020
 */
public class EndpointDTO {
    
    Integer id;
    Integer clientId;
    Integer clientAccessId;
    String name;
   
    String root;
    String state;
    Long createTime;
    Long updateTime;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getClientId() {
        return clientId;
    }
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
    public Integer getClientAccessId() {
        return clientAccessId;
    }
    public void setClientAccessId(Integer clientAccessId) {
        this.clientAccessId = clientAccessId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRoot() {
        return root;
    }
    public void setRoot(String root) {
        this.root = root;
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
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    
    

}
