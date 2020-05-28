package ink.codflow.transfer.fsclient;

import org.apache.commons.vfs2.FileObject;

import ink.codflow.sync.exception.FileException;

public interface VFSClient {

	
    
    public FileObject[] list(String path) throws FileException;
    
    
    public FileObject resolve(String path) throws FileException;
    
    public boolean isRemote();
    
    

}
