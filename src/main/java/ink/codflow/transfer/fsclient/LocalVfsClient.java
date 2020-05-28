package ink.codflow.transfer.fsclient;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;

import ink.codflow.sync.exception.FileException;

public class LocalVfsClient implements VFSClient {

    @Override
    public FileObject[] list(String path) throws FileException {
        
        
        try {
			return VFS.getManager().resolveFile(path).getChildren();
		} catch (FileSystemException e) {
	        throw new FileException();
		}
    }
    public FileObject resolve(String path) throws FileException {
        try {
            FileObject dst = VFS.getManager().resolveFile(path);
            
            return dst;
        } catch (FileSystemException e) {
            throw new FileException();
        }
    }

    @Override
    public boolean isRemote() {
        return false;
    }

    

}
