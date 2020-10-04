package ink.codflow.sync.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import ink.codflow.sync.dao.ClientAccountDOMapper;
import ink.codflow.sync.entity.ClientAccountDO;

/**
 * @author SH
 *
 * @date Aug 23, 2020
 */
@Service
public class ClientAccountManager {
    
    @Resource
    ClientAccountDOMapper clientAccountDOMapper;
    
    int save(ClientAccountDO clientAccountDO) {
        return clientAccountDOMapper.insert(clientAccountDO);
    }
    
    int delete(String accountId, Integer clientId) {
        return clientAccountDOMapper.deleteByAccountIdAndClientId(accountId,clientId);
    }
    
    int delete(Integer id) {
        return clientAccountDOMapper.deleteByPrimaryKey(id);
    }
}
