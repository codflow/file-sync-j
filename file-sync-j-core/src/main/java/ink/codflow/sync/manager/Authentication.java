package ink.codflow.sync.manager;

import java.util.HashMap;
import java.util.Map;

import ink.codflow.sync.consts.AuthDataType;
import ink.codflow.sync.consts.AuthenticationType;



public class Authentication {

    String id;
    
    AuthenticationType authType;
    
    Map<AuthDataType, String> param = new HashMap<AuthDataType, String>();

    public void addParam(AuthDataType type, String value) {
        if (type != null && value != null) {
            param.put(type, value);
        }
    };

    public String getParam(AuthDataType type) {

        return param.get(type);
    }

    public String getId() {
        return id;

    }
    public void setId(String id) {
        this.id = id;
    }
    
    public Map<AuthDataType, String> getParamMap(){
        return this.param;
    }

    public AuthenticationType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthenticationType authType) {
        this.authType = authType;
    }
   
    
    
    

}
