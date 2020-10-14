package ink.codflow.sync.core.adapter;


import ink.codflow.sync.exception.FileException;

public interface ObjectAdapter<T> {
    
    boolean isFile(T object)  throws FileException;
    
    boolean isDir(T object)  throws FileException;


}
