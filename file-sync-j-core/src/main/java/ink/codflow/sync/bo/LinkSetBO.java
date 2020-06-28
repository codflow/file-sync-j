package ink.codflow.sync.bo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class LinkSetBO {
    String id;
    String clientId;
    String name;
    long expire;
    List<LinkBO> links = new ArrayList<LinkBO>();

    public void addLinkBO(LinkBO linkBO) {
        links.add(linkBO);
    }

}
