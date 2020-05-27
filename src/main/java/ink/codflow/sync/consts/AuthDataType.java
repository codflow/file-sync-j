package ink.codflow.sync.consts;

public enum AuthDataType {

    PASSWD,

    USERNAME,

    AK,

    SK,

    HOST,

    NAME,

    ID;

    public static AuthDataType resolveProtoName(String name) {

        return valueOf(name);
    }

    public static AuthDataType resolve(String name) {

        return valueOf(name);
    }

    public String getName() {

        return this.name();
    }

}
