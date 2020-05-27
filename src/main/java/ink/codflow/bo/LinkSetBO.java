package ink.codflow.bo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class LinkSetBO {
    String clientId;
    String name;
    List<LinkBO> links = new ArrayList<LinkBO>();

    public void addLinkBO(LinkBO linkBO) {
        links.add(linkBO);
    }

}
