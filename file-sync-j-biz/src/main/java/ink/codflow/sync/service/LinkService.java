package ink.codflow.sync.service;

import java.util.List;

import ink.codflow.sync.bo.LinkBO;
import ink.codflow.sync.dto.LinkDTO;
import ink.codflow.sync.manager.EndpointManager;
import ink.codflow.sync.manager.LinkManager;

public class LinkService {
    
    
    EndpointManager endpointManager;

    LinkManager linkManager;
    
    int create(LinkDTO linkDTO){
        Integer sourceId = linkDTO.getSourceId();
        Integer targetId = linkDTO.getSourceId();
        LinkBO linkBO = new LinkBO();
        linkBO.setSourceId(sourceId);
        linkBO.setTargetId(targetId);
        linkManager.save(linkBO);
        return 0;
    }

    int update(LinkDTO linkDTO){
        return 0;
    }

    int delete(String id,Boolean clearEndpoint){
        return 0;
    }

    List<LinkDTO> selectLinkDTOsByClientId(String clientId,Integer step){
        return null;
    }

}