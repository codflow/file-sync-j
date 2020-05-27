package ink.codflow.sync.consts;

public enum ClientTypeEnum {

    
    OSS,SFTP,LOCAL;
    
    public static ClientTypeEnum resolve(String name) {
        return valueOf(name);
    }
    
    public   String getName( ) {
        return this.name();
    }
}
