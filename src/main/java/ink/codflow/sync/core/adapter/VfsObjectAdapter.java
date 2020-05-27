package ink.codflow.sync.core.adapter;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import ink.codflow.sync.exception.FileException;

public class VfsObjectAdapter implements ObjectAdapter<FileObject> {

    public boolean isDir(FileObject object) throws FileException {
       try {
        return  object.isFolder();
    } catch (FileSystemException e) {
        throw new FileException();
    }
    }

    @Override
    public boolean isFile(FileObject object) throws FileException {
        try {
            return  object.isFile();
        } catch (FileSystemException e) {
            throw new FileException();
        }
    }

 
 
}
