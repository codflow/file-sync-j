package ink.codflow.sync.bo;

import ink.codflow.sync.consts.FileSyncMode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkBO {
    
    int id;
    
    ClientEndpointBO srcEndpoint;
    ClientEndpointBO destEndpoint;
    
    long expire;
    
    FileSyncMode mode;

    int maxThread;

    
    
}
