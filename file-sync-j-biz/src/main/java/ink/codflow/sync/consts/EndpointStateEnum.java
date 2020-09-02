package ink.codflow.sync.consts;

/**
 * @author SH
 *
 * @date Aug 29, 2020
 */
public enum EndpointStateEnum {
    
    
    CREATED("CREATED"),CHECKED("CHECKED"),UNACCESSIBLE("UNACCESSIBLE"),DELETED("DELETED");
    

    
    String code;
    
    
    EndpointStateEnum(String code){
        this.code = code;
    }
    
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
}
