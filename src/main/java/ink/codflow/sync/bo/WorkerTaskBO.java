package ink.codflow.sync.bo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;


public class WorkerTaskBO {

    LinkBO linkBO;
    
    List<ObjectBO> objectList = new ArrayList<ObjectBO>();
    
    public void addObjectUri(ObjectBO e) {
        objectList.add(e);
    }

    public LinkBO getLinkBO() {
        return linkBO;
    }

    public void setLinkBO(LinkBO linkBO) {
        this.linkBO = linkBO;
    }

    public List<ObjectBO> getObjectList() {
        return objectList;
    }
    
    
}
