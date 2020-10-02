package ink.codflow.sync.service;

import java.util.List;

import org.apache.logging.log4j.core.appender.FileManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import ink.codflow.sync.bo.ClientAccessBO;
import ink.codflow.sync.bo.EndpointBO;
import ink.codflow.sync.consts.AuthDataType;
import ink.codflow.sync.consts.ClientTypeEnum;
import ink.codflow.sync.consts.EndpointStateEnum;
import ink.codflow.sync.dto.EndpointDTO;
import ink.codflow.sync.entity.ClientAccessDataDO;
import ink.codflow.sync.entity.ClientDO;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.manager.Authentication;
import ink.codflow.sync.manager.ClientAccessManager;
import ink.codflow.sync.manager.ClientManager;
import ink.codflow.sync.manager.Endpoint;
import ink.codflow.sync.manager.EndpointManager;
import ink.codflow.sync.manager.FileSyncManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EndpointService {

    @Autowired
    EndpointManager endpointManager;

    @Autowired
    FileSyncManager fileSyncManager;

    @Autowired
    ClientAccessManager clientAccessManager;

    @Autowired
    ClientManager clientManager;

    public int create(EndpointDTO endpointDTO) {
        return create(endpointDTO, false);

    }

    public int create(EndpointDTO endpointDTO, boolean checkAccess) {

        EndpointBO endpointBO = convert2EndpointBO(endpointDTO);
        if (checkAccess) {
            if (checkAccess(endpointDTO)) {
                return 0;
            }
        }
        return endpointManager.create(endpointBO);
    }

    public int update(EndpointDTO endpointDTO) {
        EndpointBO endpointBO = new EndpointBO();
        return endpointManager.updateSelective(endpointBO);

    }

    public int delete(Integer id) {

        return endpointManager.delete(id);
    }

    public EndpointDTO get(Integer id) {

        EndpointBO endpointBO = endpointManager.getById(id);
        return convertDTO(endpointBO);
    }

    public boolean checkAccess(EndpointDTO endpointDTO) {
        Endpoint endpoint = convert2Endpoint(endpointDTO);
        boolean result = fileSyncManager.testClient(endpoint);
        return result;
    }

    public EndpointDTO convertDTO(EndpointBO endpointBO) {

        return null;
    }

    public EndpointBO convertBO(EndpointDTO endpointDTO) {
        return null;
    }

    EndpointBO convert2EndpointBO(EndpointDTO endpointDTO) {
        Integer accessId = endpointDTO.getClientAccessId();

        Integer clientId = endpointDTO.getClientId();
        String name = endpointDTO.getName();
        String root = endpointDTO.getRoot();
        String state = endpointDTO.getState();
        endpointDTO.getUpdateTime();
        EndpointBO endpointBO = new EndpointBO();
        endpointBO.setClientAccessId(accessId);
        endpointBO.setRoot(root);
        endpointBO.setName(name);
        endpointBO.setClientId(clientId);
        EndpointStateEnum endpointStateEnum = EndpointStateEnum.resolve(state);
        endpointBO.setState(endpointStateEnum);
        return null;
    }

    Endpoint convert2Endpoint(EndpointDTO endpointDTO) {
        Integer clientAccessId = endpointDTO.getClientAccessId();
        Integer clientId = endpointDTO.getClientId();

        ClientAccessBO clientAccessBO = clientAccessManager.getClientAccessById(clientAccessId);
        Authentication authentication = new Authentication();
        String authenticationId = authentication.getId();
        List<ClientAccessDataDO> clientAccessDataDOs = clientAccessBO.getData();
        for (ClientAccessDataDO clientAccessDataDO : clientAccessDataDOs) {
            String dataType = clientAccessDataDO.getType();
            String dataValue = clientAccessDataDO.getValue();
            authentication.addParam(AuthDataType.resolve(dataType), dataValue);
        }
        Endpoint endpoint = new Endpoint();
        endpoint.setAuthId(authenticationId);
        endpoint.setAuthentication(authentication);
        String root = endpointDTO.getRoot();
        endpoint.setRootPath(root);
        ClientDO clientDO = clientManager.getClientById(clientId);
        String clientType = clientDO.getType();
        ClientTypeEnum clientTypeEnum = ClientTypeEnum.resolve(clientType);
        endpoint.setType(clientTypeEnum);
        return endpoint;
    }
}