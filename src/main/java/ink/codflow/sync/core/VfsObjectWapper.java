package ink.codflow.sync.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.sync.core.adapter.ObjectAdapter;
import ink.codflow.sync.core.adapter.Vfs2VfsObjectManipulationAdapter;
import ink.codflow.sync.core.adapter.VfsObjectAdapter;
import ink.codflow.sync.exception.FileException;
import ink.codflow.transfer.vfs.Client;

public class VfsObjectWapper extends AbstractObjectWapper<FileObject> {

	private static final Logger log = LoggerFactory.getLogger(VfsObjectWapper.class);

	static final ObjectAdapter<FileObject> OBJECT_ADAPTER = new VfsObjectAdapter();
	Vfs2VfsObjectManipulationAdapter objectManipulationAdapter = new Vfs2VfsObjectManipulationAdapter();

	public VfsObjectWapper(String root) {
		super(root, null);
	}
	
	public VfsObjectWapper(String root, ClientEndpoint<FileObject> endpoint) {
		super(root, endpoint);
	}

	public VfsObjectWapper(FileObject fileObject, ClientEndpoint<FileObject> endpoint) {
		super(fileObject, endpoint);
		String uri = fileObject.getName().getPath();
		this.setUri(uri);
	}

	@Override
	public void copyFrom(AbstractObjectWapper<?> objectWapper) throws FileException {

		copyFromWithTimeStamp(objectWapper);
	}

	void copyFromWithTimeStamp(AbstractObjectWapper<?> objectWapper) throws FileException {

		FileObject dest = getObject();
		FileObject src = (FileObject) objectWapper.getObject();
		objectManipulationAdapter.copy(src, dest);
	}

	@Override
	public boolean doIsDir() throws FileException {

		try {
			return getObject().isFolder();
		} catch (FileSystemException e) {
			throw new FileException();
		}

	}

	@Override
	public List<AbstractObjectWapper<FileObject>> listChildren() throws FileException {

		try {
			List<AbstractObjectWapper<FileObject>> abstractObjectWappers = new ArrayList<>();
			Client<FileObject> client = getEndpoint().getRandomClient();
			FileObject[] fileObjects = client.list(this.uri);

			if (fileObjects != null && fileObjects.length > 0) {
				for (FileObject fileObject : fileObjects) {
					// TODO cache obj
					VfsObjectWapper objectWapper = new VfsObjectWapper(fileObject, getEndpoint());
					abstractObjectWappers.add(objectWapper);
				}
			}
			return abstractObjectWappers;

		} catch (FileException e) {
			throw new FileException(e);
		}
	}

	@Override
	protected String doGetUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doGetBaseName() {
		return this.object.getName().getBaseName();
	}

	@Override
	public Map<String, AbstractObjectWapper<FileObject>> mapChildren() throws FileException {

		try {
			Map<String, AbstractObjectWapper<FileObject>> abstractObjectWappers = new HashMap<>();
			if (exist) {
				Client<FileObject> client = getEndpoint().getRandomClient();
				// client.list(this.uri);

				FileObject[] fileObjects = client.list(this.uri);
				// FileObject[] fileObjects = this.object.getChildren();
				if (fileObjects != null && fileObjects.length > 0) {
					for (FileObject fileObject : fileObjects) {
						// TODO cache obj
						VfsObjectWapper objectWapper = new VfsObjectWapper(fileObject, getEndpoint());
						String baseName = objectWapper.getBaseFileName();
						abstractObjectWappers.put(baseName, objectWapper);
					}
				}

			}

			return abstractObjectWappers;

		} catch (FileException e) {
			log.error(uri);
			throw new FileException(e);
		}
	}

	@Override
	public boolean isDiff(AbstractObjectWapper<?> abstractObjectWapper) throws FileException {

		Object object = abstractObjectWapper.getObject();

		if (this.getObject() != null && object instanceof FileObject) {
			FileObject fileObject = (FileObject) object;
			return objectManipulationAdapter.checkDiff(this.getObject(), fileObject);
		}
		throw new FileException();
	}

	@Override
	protected long doGetSize() throws FileException {

		try {
			return this.getObject().getContent().getSize();

		} catch (FileSystemException e) {
			throw new FileException(e);
		}
	}

	@Override
	public AbstractObjectWapper<FileObject> createChildDir(String srcBaseName) throws FileException {
		FileObject newChild;
		try {
			newChild = this.getObject().resolveFile(srcBaseName);

			VfsObjectWapper objectWapper = new VfsObjectWapper(newChild, getEndpoint());
			objectWapper.setDirectory(true);
			objectWapper.setExist(false);
			return objectWapper;
		} catch (FileSystemException e) {
			throw new FileException();
		}

	}

	@Override
	public FileObject doGetObject() throws FileException {
		Client<FileObject> client = getEndpoint().getRandomClient();
		return client.resolve(uri);
	}

}
