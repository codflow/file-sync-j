package ink.codflow.sync.bo;

import java.util.ArrayList;
import java.util.List;

import ink.codflow.sync.task.TaskSpecs;
import lombok.Data;


public class WorkerTaskBO {

    LinkBO linkBO;
    
    TaskSpecs specs;
    
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

    public void setObjectList(List<ObjectBO> objectList) {
        this.objectList = objectList;
    }

    public TaskSpecs getSpecs() {
        return specs;
    }

    public void setSpecs(TaskSpecs specs) {
        this.specs = specs;
    }


    
}
