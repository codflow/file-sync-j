package ink.codflow.bo;

import ink.codflow.sync.consts.ClientTypeEnum;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClientBO {
    
    
    AuthenticationBO authenticationBO;
    ClientTypeEnum clientTypeEnum;
    String tag;

}
