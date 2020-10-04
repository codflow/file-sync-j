package ink.codflow.sync.consts;

import java.util.HashMap;
import java.util.Map;

public enum AuthTypeEnum {

    PUBLIC_KEY("public_key"), LOCAL("local");

    AuthTypeEnum(String code) {
        this.code = code;
    }

    private static final Map<String, AuthTypeEnum> MAP = new HashMap<>();
    static {
        for (AuthTypeEnum enum0 : AuthTypeEnum.values()) {
            MAP.put(enum0.code, enum0);
        }
    }
    private String code;

    public String getCode() {
        return code;
    }

    public static AuthTypeEnum resolve(String code){
        return MAP.get(code);
    }

    
}