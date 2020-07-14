package ink.codflow.sync.core;

public abstract class AbstractStreamObjectWapper<T> extends AbstractObjectWapper<T> implements StreamObject {

    public AbstractStreamObjectWapper(String uri, ClientEndpoint<T> endpoint) {
        super(uri, endpoint);
    }

    protected AbstractStreamObjectWapper(T object, ClientEndpoint<T> endpoint) {
		super(object, endpoint);
	}

}