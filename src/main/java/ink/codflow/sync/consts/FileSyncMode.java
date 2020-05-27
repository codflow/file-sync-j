package ink.codflow.sync.consts;

public enum FileSyncMode {

    FULL, FILE_INC, META_OPTIMIZED;

    public static FileSyncMode resolve(String name) {
        return valueOf(name);
    }

    public String getName() {
        return this.name();
    }

}
