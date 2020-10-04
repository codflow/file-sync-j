package ink.codflow.sync.bo;

import ink.codflow.sync.consts.EndpointStateEnum;

/**
 * @author codflow
 *
 * @date Aug 30, 2020
 */
public class EndpointBO {

    
    Integer id;
    
    Integer clientId;
    
    Integer clientAccessId;
    
    String name;
   
    String root;
    
    EndpointStateEnum state;
    
    Long createTime;
    
    Long updateTime;
    
    Long usage;

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

    public EndpointStateEnum getState() {
        return state;
    }

    public void setState(EndpointStateEnum state) {
        this.state = state;
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

    public Long getUsage() {
        return usage;
    }

    public void setUsage(Long usage) {
        this.usage = usage;
    }
    
    
    
}
