package ink.codflow.sync.bo;

import lombok.Data;

@Data
public class ObjectBO {
    
    int linkId;
    
    Boolean file;
    
    String uri;
    
    String name;

    Long size;
    
}
