package ink.codflow.bo;

import ink.codflow.sync.consts.FileSyncMode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkBO {
    
    int id;
    
    ClientEndpointBO srcEndpoint;
    ClientEndpointBO distEndpoint;

    FileSyncMode mode;

    int maxThread;

    
    
}
