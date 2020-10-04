package ink.codflow.sync.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SH
 *
 * @date Aug 23, 2020
 */

@Setter
@Getter
public class ClientAccessDataDTO {

    Integer id;
    
    Integer clientAccessId;
    
    String type;
    
    String value;
    
}
