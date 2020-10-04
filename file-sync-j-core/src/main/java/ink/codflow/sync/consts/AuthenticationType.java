package ink.codflow.sync.consts;

public enum AuthenticationType {

    PASSWORD, PUBKEY, AK_SK,LOCAL;

    public static AuthenticationType resolveProtoName(String name) {
        return valueOf(name);
    }

    public static AuthenticationType resolve(String name) {
        return valueOf(name);
    }

    public String getName() {
        return this.name();
    }

}
