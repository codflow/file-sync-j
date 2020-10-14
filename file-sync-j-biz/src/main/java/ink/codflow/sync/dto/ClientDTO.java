package ink.codflow.sync.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDTO {

    Integer id;
    
    String accountId;
    
    String name;
    
    Long createTime;
    
    Long updateTime;

}