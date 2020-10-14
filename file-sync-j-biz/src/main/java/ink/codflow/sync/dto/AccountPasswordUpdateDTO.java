package ink.codflow.sync.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SH
 *
 * @date Aug 23, 2020
 */

@Getter
@Setter
public class AccountPasswordUpdateDTO {

    String id;
    
    String username;
    
    String originPassword;
    
    String password;
    
    String verifyFlag;
    
}  
