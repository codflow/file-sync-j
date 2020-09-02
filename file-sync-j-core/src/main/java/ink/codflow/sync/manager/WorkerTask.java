package ink.codflow.sync.manager;

import java.util.ArrayList;
import java.util.List;

import ink.codflow.sync.task.TaskSpecs;


public class WorkerTask {

    Link linkBO;
    
    TaskSpecs specs;
    
    List<FileObject> objectList = new ArrayList<FileObject>();
    
    public void addObjectUri(FileObject e) {
        objectList.add(e);
    }

    public Link getLinkBO() {
        return linkBO;
    }

    public void setLinkBO(Link linkBO) {
        this.linkBO = linkBO;
    }

    public List<FileObject> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<FileObject> objectList) {
        this.objectList = objectList;
    }

    public TaskSpecs getSpecs() {
        return specs;
    }

    public void setSpecs(TaskSpecs specs) {
        this.specs = specs;
    }


    
}
