package ink.codflow.sync.transfer;


import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.exception.FileException;

public interface Client<T> {
	
    public T[] list(String path) throws FileException;
    
    
    public T resolve(String path) throws FileException;
    
    public boolean isRemote();


	public AbstractObjectWapper<T> resolveWapper(String path);

	
}
