package ink.codflow.sync.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ink.codflow.sync.core.adapter.AdapterTypeEnum;
import ink.codflow.sync.core.adapter.ObjectManipulationAdapter;
import ink.codflow.sync.core.adapter.Vfs2OssObjectManiputationAdapter;
import ink.codflow.sync.core.adapter.Vfs2VfsObjectManipulationAdapter;
import ink.codflow.sync.exception.FileException;

public abstract class AbstractObjectWapper<T> {

	// volatile int checkPoint =-1;

	private static final Map<AdapterTypeEnum, ObjectManipulationAdapter<?, ?>> ADAPTER_MUTI_MAP = new HashMap<>();

	static {

		ADAPTER_MUTI_MAP.put(Vfs2VfsObjectManipulationAdapter.TYPE_TAG_0, new Vfs2VfsObjectManipulationAdapter());
		ADAPTER_MUTI_MAP.put(Vfs2OssObjectManiputationAdapter.TYPE_TAG_0, new Vfs2OssObjectManiputationAdapter());

	}

	protected boolean exist = true;

	protected String uri;

	protected String baseFileName;

	protected ClientEndpoint<T> endpoint;

	protected Boolean directory;

	long size = -1;

	protected T object;

	protected long lastMod;

	protected AbstractObjectWapper(T object, ClientEndpoint<T> endpoint) {
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

	public ObjectManipulationAdapter<?, ?> getObjectManipulationAdapter(Class<?> srcClazz, Class<?> destClazz) {

		AdapterTypeEnum adapterTypeEnum = AdapterTypeEnum.resolve(srcClazz, destClazz);

		return ADAPTER_MUTI_MAP.get(adapterTypeEnum);
	}

	public String getUri() throws FileException {

		if (uri != null) {
			return uri;
		} else {
			String uri0 = doGetUri();
			this.uri = uri0;
			return uri0;
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

	public void setEndpoint(ClientEndpoint<T> endpoint) {
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

	public long getLastMod() throws FileException {
		if (lastMod > 0) {
			return lastMod;
		} else {
			long lastMod0 = doGetLastMod();
			this.lastMod = lastMod0;
			return lastMod0;
		}

	}

	protected abstract long doGetLastMod() throws FileException;

	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}
	
	
	
	@SuppressWarnings("unchecked")
	protected void doCopyFromWithTimeStamp(AbstractObjectWapper<?> objectWapper) throws FileException {

		Object dest = getObject();
		Object src = objectWapper.getObject();
		Class<?> srcClazz = src.getClass();
		Class<?> destClazz = dest.getClass();
		@SuppressWarnings("rawtypes")
		ObjectManipulationAdapter adapter = getObjectManipulationAdapter(srcClazz, destClazz); // NOSONAR dynamic															// loading adapter
		adapter.copy(src, dest);
		
	}
	
	

	public abstract boolean isDiff(AbstractObjectWapper<?> abstractObjectWapper) throws FileException;

	protected abstract boolean doIsDir() throws FileException;

	public abstract List<AbstractObjectWapper<T>> listChildren() throws FileException;

	public abstract Map<String, AbstractObjectWapper<T>> mapChildren() throws FileException;

	protected abstract String doGetUri() throws FileException;

	protected abstract String doGetBaseName() throws FileException;

	protected abstract long doGetSize() throws FileException;

	public abstract AbstractObjectWapper<T> createChildDir(String srcBaseName) throws FileException;

	public abstract T doGetObject() throws FileException;

	public abstract void copyFrom(AbstractObjectWapper<?> objectWapper) throws FileException;

}
