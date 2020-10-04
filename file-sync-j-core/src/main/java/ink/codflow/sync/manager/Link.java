package ink.codflow.sync.manager;

import ink.codflow.sync.consts.FileSyncMode;

public class Link {
    
    int id;
    
    Endpoint srcEndpoint;
    Endpoint destEndpoint;
    
    long expire;
    
    FileSyncMode mode;

    int maxThread;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the srcEndpoint
     */
    public Endpoint getSrcEndpoint() {
        return srcEndpoint;
    }

    /**
     * @param srcEndpoint the srcEndpoint to set
     */
    public void setSrcEndpoint(Endpoint srcEndpoint) {
        this.srcEndpoint = srcEndpoint;
    }

    /**
     * @return the destEndpoint
     */
    public Endpoint getDestEndpoint() {
        return destEndpoint;
    }

    /**
     * @param destEndpoint the destEndpoint to set
     */
    public void setDestEndpoint(Endpoint destEndpoint) {
        this.destEndpoint = destEndpoint;
    }

    /**
     * @return the expire
     */
    public long getExpire() {
        return expire;
    }

    /**
     * @param expire the expire to set
     */
    public void setExpire(long expire) {
        this.expire = expire;
    }

    /**
     * @return the mode
     */
    public FileSyncMode getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(FileSyncMode mode) {
        this.mode = mode;
    }

    /**
     * @return the maxThread
     */
    public int getMaxThread() {
        return maxThread;
    }

    /**
     * @param maxThread the maxThread to set
     */
    public void setMaxThread(int maxThread) {
        this.maxThread = maxThread;
    }

    
    
}
