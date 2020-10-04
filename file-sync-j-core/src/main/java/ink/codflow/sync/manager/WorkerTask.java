package ink.codflow.sync.manager;

import java.util.ArrayList;
import java.util.List;

import ink.codflow.sync.task.TaskSpecs;


public class WorkerTask {

    Link link;
    
    TaskSpecs specs;
    
    List<FileObject> objectList = new ArrayList<FileObject>();
    
    public void addObjectUri(FileObject e) {
        objectList.add(e);
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
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
