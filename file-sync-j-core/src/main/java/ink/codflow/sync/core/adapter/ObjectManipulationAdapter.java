package ink.codflow.sync.core.adapter;


import ink.codflow.sync.exception.FileException;

public interface ObjectManipulationAdapter<C, V> {
    
    
    Class<C> getSrcClassType();
    
    Class<V> getDestClassType();

    void copy(C src, V dest) throws FileException;

    void copyFileToFile(C srcFile, V destFile) throws FileException;

    void copyFileToDir(C srcFile, V destDir) throws FileException;
    
    void copyDirToDir(C srcDir, V destDir) throws FileException;
    
    boolean checkDiff(C fileObject0, V fileObject1) throws FileException;
    
    
}
