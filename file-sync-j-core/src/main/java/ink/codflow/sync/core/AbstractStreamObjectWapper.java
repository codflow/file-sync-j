package ink.codflow.sync.core;

import ink.codflow.sync.exception.FileException;

public abstract class AbstractStreamObjectWapper<T> extends AbstractObjectWapper<T> implements StreamObject {

    public AbstractStreamObjectWapper(String uri, ClientEndpoint<T> endpoint) {
        super(uri, endpoint);
    }

    protected AbstractStreamObjectWapper(T object, ClientEndpoint<T> endpoint) {
		super(object, endpoint);
	}


    public abstract AbstractStreamObjectWapper<T> createChild(String srcBaseName,boolean isDir) throws FileException;
    

}