package ink.codflow.sync.transfer.oss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.core.ClientEndpoint;
import ink.codflow.sync.core.adapter.ObjectAdapter;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.transfer.Client;

public class OssObjectWapper extends AbstractObjectWapper<OssObject> {

	private static final Logger log = LoggerFactory.getLogger(OssObjectWapper.class);

	static final OssObjectAdapter OBJECT_ADAPTER = new OssObjectAdapter();

	public OssObjectWapper(String uri) {
		super(uri, null);
	}

	public OssObjectWapper(String root, ClientEndpoint<OssObject> endpoint) {
		super(root, endpoint);
	}

	protected OssObjectWapper(OssObject object, ClientEndpoint<OssObject> endpoint) {
		super(object, endpoint);
	}

	@Override
	public boolean isDiff(AbstractObjectWapper<?> abstractObjectWapper) throws FileException {
		long size1 = abstractObjectWapper.getSize();
		long lastMod1 = abstractObjectWapper.getLastMod();
		long size0 = this.getSize();
		long lastMod0 = this.getLastMod();
		return size1 == size0 && lastMod0 == lastMod1;
	}

	@Override
	protected boolean doIsDir() throws FileException {
		String uri = getUri();
		return '/' == uri.charAt(uri.length() - 1);
		// return OBJECT_ADAPTER.isDir(getObject());
	}

	@Override
	public List<AbstractObjectWapper<OssObject>> listChildren() throws FileException {
		List<AbstractObjectWapper<OssObject>> abstractObjectWappers = new ArrayList<>();
		Client<OssObject> client = getEndpoint().getRandomClient();
		OssObject[] childrens = client.list(getUri());
		for (OssObject ossObject : childrens) {
			OssObjectWapper wapper = new OssObjectWapper(ossObject, getEndpoint());
			abstractObjectWappers.add(wapper);
		}
		return abstractObjectWappers;
	}

	@Override
	public Map<String, AbstractObjectWapper<OssObject>> mapChildren() throws FileException {
		Map<String, AbstractObjectWapper<OssObject>> abstractObjectWappers = new HashMap<>();
		Client<OssObject> client = getEndpoint().getRandomClient();
		OssObject[] childrens = client.list(getUri());
		for (OssObject ossObject : childrens) {
			OssObjectWapper wapper = new OssObjectWapper(ossObject, getEndpoint());
			wapper.setUri(ossObject.getUri());
			abstractObjectWappers.put(wapper.getBaseFileName(), wapper);
		}
		return abstractObjectWappers;
	}

	@Override
	protected String doGetUri() throws FileException {
		return getObject().getUri();
	}

	@Override
	protected String doGetBaseName() throws FileException {
		return OBJECT_ADAPTER.getBaseFileName(getUri());
	}

	@Override
	protected long doGetSize() throws FileException {

		return getObject().getSize();
	}

	@Override
	public AbstractObjectWapper<OssObject> createChild(String srcBaseName,boolean isDir) throws FileException {
		OssObject object = new OssObject();
		if (isDir) {
			object.setSize(0);
		}
		OssObject obj0 = getObject();
		String bucket0 = obj0.getBucketName();
		String key0 = obj0.getKey();
		object.setOss(obj0.getOss());
		
		StringBuilder keySB = new StringBuilder(key0).append(srcBaseName);
		
		if (isDir) {
			keySB.append("/");
		}
		object.setKey(keySB.toString());
		object.setBucketName(bucket0);
		String uri = isDir?  getUri() + srcBaseName + "/" :  getUri() + srcBaseName ;
		object.setUri(uri);
		OssObjectWapper objectWapper = new OssObjectWapper(object, getEndpoint());
		objectWapper.setUri(uri);
		objectWapper.setDirectory(isDir);
		objectWapper.setExist(null);
		return objectWapper;
	}

	@Override
	public OssObject doGetObject() throws FileException {
		Client<OssObject> client = getEndpoint().getRandomClient();
		return client.resolve(getUri());
	}

	@Override
	protected long doGetLastMod() throws FileException {
		return this.getObject().getLastModified().getTime();
	}

	@Override
	public void copyFrom(AbstractObjectWapper<?> objectWapper) throws FileException {
		doCopyFromWithTimeStamp(objectWapper);

	}

	@Override
	protected Boolean doCheckExist() throws FileException {

		OssObject object = this.getObject();
		return OBJECT_ADAPTER.checkExist(object);
		
	}

    @Override
    public void remove() throws FileException {
        OssObject object = this.getObject();
        OBJECT_ADAPTER.remove(object);
        
    }

}
