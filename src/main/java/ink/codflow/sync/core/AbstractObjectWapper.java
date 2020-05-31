package ink.codflow.sync.core;

import java.util.List;
import java.util.Map;

import ink.codflow.sync.exception.FileException;

public abstract class AbstractObjectWapper<T> {

    //volatile int checkPoint =-1;
    
    boolean exist =true;
    
    String uri;

    String baseFileName;

    ClientEndpoint<T> endpoint;

    Boolean directory;

    long size = -1;

    T object;

    
    
    AbstractObjectWapper(T object,ClientEndpoint<T> endpoint) {
    	this.endpoint = endpoint;
        this.object = object;
    }

    public AbstractObjectWapper(String uri, ClientEndpoint<T> endpoint) {
        this.uri = uri;
        this.endpoint = endpoint;
        
    }

    T objectHandle() {
        return null;

    }


    

    public String getUri() {

        if (uri != null) {
            return uri;
        } else {
            String uri = doGetUri();
            this.uri = uri;
            return uri;
        }

    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setBaseFileName(String baseFileName) {
        this.baseFileName = baseFileName;
    }

    public ClientEndpoint<T> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(ClientEndpoint<T>  endpoint) {
        this.endpoint = endpoint;
    }

    public T getObject() throws FileException {

        if (this.object != null) {
            return this.object;
        }
        T object0 = doGetObject();
        this.object = object0;
        return object0;
    	
    }

    public boolean isFile() throws FileException {
        if (this.directory != null) {
            return !this.directory;
        }
        boolean isDir = doIsDir();
        this.directory = isDir;
        return !isDir;
    }

    public boolean isDir() throws FileException {
        if (this.directory != null) {
            return this.directory;
        }
        boolean isDirectory = doIsDir();
        this.directory = isDirectory;
        return isDirectory;
    }
    
    

    public void setDirectory(Boolean directory) {
        this.directory = directory;
    }

    public String getBaseFileName() throws FileException {
        if (this.baseFileName != null) {
            return this.baseFileName;
        }
        String baseFileName0 = doGetBaseName();
        this.baseFileName = baseFileName0;
        return baseFileName0;
    }

    public long getSize() throws FileException {
        if (size > 0) {
            return size;
        } else {
            long size = doGetSize();
            this.size = size;
            return size;
        }

    }
    
    

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    
    
    
    public abstract boolean isDiff(AbstractObjectWapper<?> abstractObjectWapper) throws FileException;

    protected abstract boolean doIsDir() throws FileException;

    public abstract List<AbstractObjectWapper<T>> listChildren() throws FileException;

    public abstract Map<String, AbstractObjectWapper<T>> mapChildren() throws FileException;

    protected abstract String doGetUri();

    protected abstract String doGetBaseName();

    protected abstract long doGetSize() throws FileException;

    public abstract AbstractObjectWapper<T> createChildDir(String srcBaseName) throws FileException;
    
    public abstract T doGetObject() throws FileException; 
    
    
    public abstract void copyFrom(AbstractObjectWapper<?> objectWapper) throws FileException;


}
