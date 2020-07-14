package ink.codflow.sync.transfer.vfs;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.provider.local.WindowsFileName;
import org.apache.commons.vfs2.util.RandomAccessMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ink.codflow.sync.core.AbstractObjectWapper;
import ink.codflow.sync.core.AbstractStreamObjectWapper;
import ink.codflow.sync.core.ClientEndpoint;
import ink.codflow.sync.core.adapter.ObjectAdapter;
import ink.codflow.sync.core.adapter.ObjectManipulationAdapter;
import ink.codflow.sync.core.adapter.Vfs2VfsObjectManipulationAdapter;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.transfer.Client;

public class VfsObjectWapper extends AbstractStreamObjectWapper<FileObject> {

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
		FileName name = fileObject.getName();

		if (name instanceof WindowsFileName) {
			WindowsFileName windowsFileName = (WindowsFileName) name;
			String uri = windowsFileName.getURI();
			uri = uri.substring(8);
			this.setUri(uri);
		} else {
			String uri = fileObject.getName().getPath();
			this.setUri(uri);

		}

	}

	@Override
	public void copyFrom(AbstractObjectWapper<?> objectWapper) throws FileException {
		doCopyFromWithTimeStamp(objectWapper);
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
	protected String doGetUri() throws FileException {
		try {
			return getObject().getName().getPath();
		} catch (FileException e) {
			throw new FileException(e);
		}
	}

	@Override
	public String doGetBaseName() {
		return this.object.getName().getBaseName();
	}

	@Override
	public Map<String, AbstractObjectWapper<FileObject>> mapChildren() throws FileException {

		try {
			Map<String, AbstractObjectWapper<FileObject>> abstractObjectWappers = new HashMap<>();
			if (isExist()) {
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean isDiff(AbstractObjectWapper<?> abstractObjectWapper) throws FileException {

		Object destObject = abstractObjectWapper.getObject();
		Object srcObject = this.getObject();

		@SuppressWarnings("rawtypes")
		ObjectManipulationAdapter adapter = getObjectManipulationAdapter(srcObject.getClass(), destObject.getClass());
		return adapter.checkDiff(srcObject, destObject);

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
	public AbstractObjectWapper<FileObject> createChild(String srcBaseName, boolean isDir) throws FileException {
		FileObject newChild;
		try {
			newChild = this.getObject().resolveFile(srcBaseName);

			VfsObjectWapper objectWapper = new VfsObjectWapper(newChild, getEndpoint());
			objectWapper.setExist(newChild.exists());
			FileType type = newChild.getType();
			if (FileType.IMAGINARY.equals(type) || FileType.FOLDER.equals(type) == isDir) {
				objectWapper.setDirectory(isDir);
				return objectWapper;
			}
			throw new FileException("Target object type is not match");
		} catch (FileSystemException e) {
			throw new FileException();
		}

	}

	@Override
	public FileObject doGetObject() throws FileException {
		Client<FileObject> client = getEndpoint().getRandomClient();
		return client.resolve(getUri());
	}

	@Override
	protected long doGetLastMod() throws FileException {
		try {
			return this.getObject().getContent().getLastModifiedTime();
		} catch (FileSystemException | FileException e) {
			throw new FileException();
		}
	}

	@Override
	protected Boolean doCheckExist() throws FileException {
		try {
			return this.doGetObject().exists();
		} catch (FileSystemException | FileException e) {
			throw new FileException(e);
		}
	}

	@Override
	public void remove() throws FileException {
		FileObject object = this.getObject();
		try {
			object.deleteAll();
		} catch (FileSystemException e) {
			throw new FileException();
		}
	}

	public void create() throws FileException {
		try {
			FileObject object = this.getObject();
			if (isDir()) {
				object.createFolder();

			} else {
				object.createFile();

			}
		} catch (FileSystemException | FileException e) {
			e.printStackTrace();
			throw new FileException(e);
		}

	}

	@Override
	public void setTimeStamp(long timestamp) throws FileException {
		try {
			FileObject object = this.getObject();

			object.getContent().setLastModifiedTime(timestamp);
		} catch (FileSystemException | FileException e) {
			throw new FileException(e);

		}
	}

	@Override
	public InputStream fileInputStream() throws FileException {
		FileObject object = this.getObject();

		try {
			return object.getContent().getInputStream();
		} catch (FileSystemException e) {
			throw new FileException(e);
		}
	}

	@Override
	public void copyContentFrom(InputStream in) throws FileException {
		FileObject object = this.getObject();
		
		if (!isExist()) {
			if (isFile()) {
				try {
					object.createFile();
					object.getContent().getOutputStream();

				} catch (FileSystemException e) {
				
				}
			}
		}
	}

}
