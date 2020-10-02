package ink.codflow.sync.consts;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SH
 *
 * @date Aug 29, 2020
 */
public enum EndpointStateEnum {

    CREATED("CREATED"), CHECKED("CHECKED"), UNACCESSIBLE("UNACCESSIBLE"), DELETED("DELETED");

    String code;

    private static final Map<String, EndpointStateEnum> MAP = new HashMap<>();

    static {
        for (EndpointStateEnum endpointStateEnum : EndpointStateEnum.values()) {
            MAP.put(endpointStateEnum.getCode(), endpointStateEnum);
        }
    }

    EndpointStateEnum(String code) {
        this.code = code;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    public static EndpointStateEnum resolve(String name) {
        EndpointStateEnum enum0 = MAP.get(name);
        return enum0;
    }
}
