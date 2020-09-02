package ink.codflow.sync.bo;

import java.util.List;

import ink.codflow.sync.entity.ClientAccessDataDO;

/**
 * @author codflow
 *
 * @date Aug 29, 2020
 */
public class ClientAccessBO {

    Integer id;

    Integer clientId;

    String type;

    List<ClientAccessDataDO> data;
    
    /**
     * @return the data
     */
    public List<ClientAccessDataDO> getData() {
        return data;
    }
    

    String getDataValue(String type) {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                ClientAccessDataDO clientAccessDataDO = data.get(i);
                if (type.equals(clientAccessDataDO.getType())) {
                    return clientAccessDataDO.getValue();
                }
            }
        }
        return null;
    }
    

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


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
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


    public void setData(List<ClientAccessDataDO> data) {
        this.data = data;
    }

    
    
}
