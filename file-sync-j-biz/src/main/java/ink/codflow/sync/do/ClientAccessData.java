package ink.codflow.sync.do;

import java.io.Serializable;

public class ClientAccessData implements Serializable {
    private Integer id;

    private Integer clientAccessId;

    private String type;

    private String value;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientAccessId() {
        return clientAccessId;
    }

    public void setClientAccessId(Integer clientAccessId) {
        this.clientAccessId = clientAccessId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }
}