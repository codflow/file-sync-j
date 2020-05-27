package ink.codflow.transfer.fsclient;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;

public class LocalVfsClient implements VFSClient {

    @Override
    public FileObject[] list(String path) {
        
        
        return null;
    }
    public FileObject resolve(String path) {
        try {
            FileObject dst = VFS.getManager().resolveFile(path);
            
            return dst;
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isRemote() {
        return false;
    }

    

}
