package ink.codflow.sync.service;

import org.apache.logging.log4j.core.appender.FileManager;
import org.springframework.beans.factory.annotation.Autowired;

import ink.codflow.sync.bo.EndpointBO;
import ink.codflow.sync.dto.EndpointDTO;
import ink.codflow.sync.exception.FileException;
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

    public int create(EndpointDTO endpointDTO) {
        return create(endpointDTO, false);

    }

    public int create(EndpointDTO endpointDTO, boolean checkAccess) {
        String root = endpointDTO.getRoot();
        
        if (checkAccess) {
            Endpoint endpoint = new Endpoint();
            
            try {
                fileSyncManager.listEndpointContent(endpoint, root);
            } catch (FileException e) {
                log.info("check endpoint failed", e);
                return 0;
            }
        }
        EndpointBO endpointBO = new EndpointBO();
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

    public boolean checkAccesse() {
        return false;
    }

    public EndpointDTO convertDTO(EndpointBO endpointBO) {
        
        return null;
    }
    
    public EndpointBO convertBO(EndpointDTO endpointDTO) {
        return null;
    }

}