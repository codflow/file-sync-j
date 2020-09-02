package ink.codflow.sync.manager;

import org.springframework.beans.factory.annotation.Autowired;

import ink.codflow.sync.bo.EndpointBO;
import ink.codflow.sync.consts.EndpointStateEnum;
import ink.codflow.sync.dao.EndpointDOMapper;
import ink.codflow.sync.dao.EndpointDetailDOMapper;
import ink.codflow.sync.entity.EndpointDO;

/**
 * @author codflow
 *
 * @date Aug 29, 2020
 */
public class EndpointManager {

    @Autowired
    EndpointDetailDOMapper endpointDetailDOMapper;

    @Autowired
    EndpointDOMapper endpointDOMapper;

    public int create(EndpointBO endpointDTO) {

        EndpointDO record = convert2DO(endpointDTO);
        return endpointDOMapper.insert(record);
    }
    
    public int updateSelective(EndpointBO endpointDTO) {
        EndpointDO record = convert2DO(endpointDTO);
        return updateSelective(record);
    }
    

    public int delete(int id) {
        return endpointDOMapper.deleteByPrimaryKey(id);
    }

    public int updateState(int id, EndpointStateEnum stateEnum) {
        EndpointDO endpointDO = new EndpointDO();
        endpointDO.setState(stateEnum.getCode());
        return updateSelective(endpointDO);
    }

    /**
     * @description: TODO
     * @param endpointDO
     * @return int
     */
    private int updateSelective(EndpointDO endpointDO) {
        return  endpointDOMapper.updateByPrimaryKeySelective(endpointDO);
    }


    
    EndpointDO convert2DO(EndpointBO endpointBO) {
        
        String name = endpointBO.getName();
        String root = endpointBO.getRoot();
        Integer clientId = endpointBO.getClientId();
        Integer accessId = endpointBO.getClientAccessId();
        String state = endpointBO.getState();
        EndpointDO record = new EndpointDO();
        record.setClientId(clientId);
        record.setClientAccessId(accessId);
        record.setName(name);
        record.setRoot(root);
        record.setState(state);
        return record;
    }

    /**
     * @description: TODO
     * @param id
     * @return EndpointBO
     */
    public EndpointBO getById(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }
    
    

}
