package ink.codflow.sync.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ink.codflow.sync.bo.ClientAccessBO;
import ink.codflow.sync.consts.AuthTypeEnum;
import ink.codflow.sync.dao.ClientAccessDOMapper;
import ink.codflow.sync.dao.ClientAccessDataDOMapper;
import ink.codflow.sync.entity.ClientAccessDO;
import ink.codflow.sync.entity.ClientAccessDataDO;

/**
 * @author codflow
 *
 * @date Aug 29, 2020
 */
public class ClientAccessManager {
    
    @Autowired
    ClientAccessDOMapper clientAccessDOMapper;
    
    @Autowired
    ClientAccessDataDOMapper clientAccessDataDOMapper;
    
    
    public ClientAccessBO  getClientAccessById(Integer clientAccessId){
        
        ClientAccessDO clientAccessDO = clientAccessDOMapper.selectByPrimaryKey(clientAccessId);
        
        List<ClientAccessDataDO> clientAccessDataDOs = clientAccessDataDOMapper.selectByClientAccessId( clientAccessId);
        
        ClientAccessBO clientAccessBO = new ClientAccessBO();
        
        Integer clientId = clientAccessDO.getClientId();

        String clientAccessType =  clientAccessDO.getType();
        
        clientAccessBO.setClientId(clientId);
        clientAccessBO.setId(clientAccessId);
        
        AuthTypeEnum type =  AuthTypeEnum.resolve(clientAccessType);
        clientAccessBO.setType(type);
        clientAccessBO.setData(clientAccessDataDOs);
        
        return clientAccessBO;
    }

 
    
    
    
    
}
