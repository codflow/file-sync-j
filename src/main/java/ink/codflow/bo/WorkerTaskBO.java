package ink.codflow.bo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WorkerTaskBO {

    LinkBO linkBO;
    
    List<ObjectBO> objectUriList = new ArrayList<ObjectBO>();
    
    public void addObjectUri(ObjectBO e) {
        objectUriList.add(e);
    }
}
