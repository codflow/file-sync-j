package ink.codflow.sync.manager;


 
public class ObjectBO {
    
    int linkId;
    
    Boolean file;
    
    String uri;
    
    String name;

    Long size;

    /**
     * @return the linkId
     */
    public int getLinkId() {
        return linkId;
    }

    /**
     * @param linkId the linkId to set
     */
    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    /**
     * @return the file
     */
    public Boolean getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(Boolean file) {
        this.file = file;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the size
     */
    public Long getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Long size) {
        this.size = size;
    }

    
    
}
