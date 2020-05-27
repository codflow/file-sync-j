package ink.codflow.sync.core.adapter;

import org.apache.commons.vfs2.FileObject;

import ink.codflow.sync.exception.FileException;

public interface ObjectAdapter<T> {
    
    boolean isFile(FileObject object)  throws FileException;
    
    boolean isDir(FileObject object)  throws FileException;


}
