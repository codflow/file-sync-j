package ink.codflow.sync.manager;

import org.springframework.beans.factory.annotation.Autowired;

import ink.codflow.sync.bo.LinkBO;
import ink.codflow.sync.dao.LinkDOMapper;
import ink.codflow.sync.entity.LinkDO;

public class LinkManager {

    @Autowired
    LinkDOMapper linkDOMapper;
    
    public void save(LinkBO linkBO) {

        LinkDO linkDO = new LinkDO();

        Integer sourceId = linkBO.getSourceId();
        Integer targetId = linkBO.getTargetId();
        linkDO.setSourceEndpointId(sourceId);
        linkDO.setTargetEndpointId(targetId);
        linkDOMapper.insert(linkDO);
    }

    public LinkBO getById(String linkId) {
        return null;
    }

}
