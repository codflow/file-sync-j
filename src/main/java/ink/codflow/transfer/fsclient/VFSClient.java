package ink.codflow.transfer.fsclient;

import org.apache.commons.vfs2.FileObject;

public interface VFSClient {

    
    public FileObject[] list(String path);
    
    
    public FileObject resolve(String path);
    
    public boolean isRemote();
    
}
