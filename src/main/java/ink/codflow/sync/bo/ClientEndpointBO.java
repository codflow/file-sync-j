package ink.codflow.sync.bo;

import ink.codflow.sync.consts.ClientTypeEnum;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClientEndpointBO {

    int id;
    String name;
    String authId;
    
    AuthenticationBO authenticationBO;
    
    ClientTypeEnum type; 
    String rootPath;
    
    
}
