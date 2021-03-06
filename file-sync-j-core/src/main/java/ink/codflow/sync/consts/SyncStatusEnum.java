package ink.codflow.sync.consts;

import java.util.HashMap;
import java.util.Map;

public enum SyncStatusEnum {

    FAILED("FAILED"),CREATE("CREATE"), ANALYSE("ANALYSE"), SYNC("SYNC"), DONE("DONE");

    String name;

    SyncStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    static final Map<String, SyncStatusEnum> MAP = new HashMap<String, SyncStatusEnum>();

    static {
        SyncStatusEnum[] statusEnums = SyncStatusEnum.values();
        for (SyncStatusEnum syncStatusEnum : statusEnums) {
            MAP.put(syncStatusEnum.getName(), syncStatusEnum);
        }
        MAP.put("FAILD", SyncStatusEnum.FAILED);

    }

    public static SyncStatusEnum resolve(String name) {

        return MAP.get(name);
    }

    public static SyncStatusEnum resolveOrigin(int origin) {

        return SyncStatusEnum.values()[origin];
    }

}
