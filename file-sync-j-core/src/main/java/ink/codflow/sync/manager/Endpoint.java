package ink.codflow.sync.manager;

import ink.codflow.sync.consts.ClientTypeEnum;

public class Endpoint {

    int id;
    String name;
    String authId;
    
    Authentication authenticationBO;
    
    ClientTypeEnum type; 
    String rootPath;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the authId
     */
    public String getAuthId() {
        return authId;
    }

    /**
     * @param authId the authId to set
     */
    public void setAuthId(String authId) {
        this.authId = authId;
    }

    /**
     * @return the authenticationBO
     */
    public Authentication getAuthentication() {
        return authenticationBO;
    }

    /**
     * @param authenticationBO the authenticationBO to set
     */
    public void setAuthentication(Authentication authenticationBO) {
        this.authenticationBO = authenticationBO;
    }

    /**
     * @return the type
     */
    public ClientTypeEnum getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ClientTypeEnum type) {
        this.type = type;
    }

    /**
     * @return the rootPath
     */
    public String getRootPath() {
        return rootPath;
    }

    /**
     * @param rootPath the rootPath to set
     */
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
    
    
    
}
