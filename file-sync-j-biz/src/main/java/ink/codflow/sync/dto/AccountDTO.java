package ink.codflow.sync.dto;

import lombok.Data;

@Data
public class AccountDTO {

    String id;

    String username;

    String password;
    
    String type;

    Long createTime;
    
    Long updateTime;
    
    

}