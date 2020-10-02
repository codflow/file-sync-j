package ink.codflow.sync.manager;

import org.springframework.beans.factory.annotation.Autowired;

import ink.codflow.sync.bo.EndpointBO;
import ink.codflow.sync.consts.EndpointStateEnum;
import ink.codflow.sync.dao.EndpointDOMapper;
import ink.codflow.sync.dao.EndpointDetailDOMapper;
import ink.codflow.sync.entity.EndpointDO;
import ink.codflow.sync.entity.EndpointDetailDO;

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
        return endpointDOMapper.updateByPrimaryKeySelective(endpointDO);
    }

    EndpointDO convert2DO(EndpointBO endpointBO) {

        String name = endpointBO.getName();
        String root = endpointBO.getRoot();
        Integer clientId = endpointBO.getClientId();
        Integer accessId = endpointBO.getClientAccessId();
        EndpointStateEnum state = endpointBO.getState();
        EndpointDO record = new EndpointDO();
        record.setClientId(clientId);
        record.setClientAccessId(accessId);
        record.setName(name);
        record.setRoot(root);
        if (state != null) {
            record.setState(state.getCode());
        }
        return record;
    }

    public EndpointBO getById(Integer id) {
        EndpointDO endpointDO = endpointDOMapper.selectByPrimaryKey(id);

        Integer clientId = endpointDO.getClientId();
        String name = endpointDO.getName();
        String root = endpointDO.getRoot();
        String state = endpointDO.getState();
        Integer clientAccessId = endpointDO.getClientAccessId();

        EndpointDetailDO endpointDetailDO = endpointDetailDOMapper.selectByPrimaryKey(id);
        String endpointStatus = endpointDetailDO.getStatus();
        Long usage = endpointDetailDO.getUsage();

        EndpointBO endpointBO = new EndpointBO();

        return null;
    }

}
