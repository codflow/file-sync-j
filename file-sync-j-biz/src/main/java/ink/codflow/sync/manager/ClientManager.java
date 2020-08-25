package ink.codflow.sync.manager;

import java.util.List;

import javax.annotation.Resource;

import ink.codflow.sync.dao.ClientDOMapper;
import ink.codflow.sync.entity.ClientDO;

/**
 * @author SH
 *
 * @date Aug 23, 2020
 */

public class ClientManager {

    @Resource
    ClientDOMapper clientDOMapper;

    public List<ClientDO> getClientByAccountId(String accountId, Integer step, Integer limit) {

        return null;
    }

    public int create(ClientDO clientDO) {

        int r = clientDOMapper.insert(clientDO);
        return r;
    }

    public int update(ClientDO clientDO) {

        int r = clientDOMapper.updateByPrimaryKeySelective(clientDO);
        return r;
    }

    public int deleteById(int clientId) {
        return clientDOMapper.deleteByPrimaryKey(clientId);
    }
}
