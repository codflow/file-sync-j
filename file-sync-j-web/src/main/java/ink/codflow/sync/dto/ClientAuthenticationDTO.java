package ink.codflow.sync.dto;

import java.util.Map;

import ink.codflow.sync.consts.AuthTypeEnum;

public class ClientAuthenticationDTO {

    public static final String DATA_USER = "user";
    public static final String DATA_PWD = "pwd";
    public static final String DATA_HOST = "host";
    public static final String DATA_AK = "ak";
    public static final String DATA_SK = "ak";
    public static final String DATA_US = "ak";

    int id;

    AuthTypeEnum authTypeEnum;

    Map<String,String> data;

    

}