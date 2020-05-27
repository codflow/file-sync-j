package ink.codflow.bo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class WorkerTaskBO {

    LinkBO linkBO;
    
    List<ObjectUriBO> objectUriList = new ArrayList<ObjectUriBO>();
    
    public void addObjectUri(ObjectUriBO e) {
        objectUriList.add(e);
    }
}
