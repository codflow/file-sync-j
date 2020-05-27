package ink.codflow.sync.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import ink.codflow.sync.core.adapter.ObjectAdapter;
import ink.codflow.sync.core.adapter.Vfs2VfsObjectManipulationAdapter;
import ink.codflow.sync.core.adapter.VfsObjectAdapter;
import ink.codflow.sync.exception.FileException;

public class SftpObjectWapper extends AbstractObjectWapper<FileObject> {

    static ObjectAdapter<FileObject> OBJECT_ADAPTER = new VfsObjectAdapter();
    Vfs2VfsObjectManipulationAdapter objectManipulationAdapter = new Vfs2VfsObjectManipulationAdapter();

    public SftpObjectWapper(String root, ClientEndpoint endpoint) {
        super(root, endpoint);
    }

    public SftpObjectWapper(FileObject fileObject) {
        super(fileObject);
        String uri = fileObject.getName().getPath();
        this.setUri(uri);
    }

    @Override
    public HashMap<String, AbstractObjectWapper<FileObject>> subFilesMap() {

        return null;
    }

    @Override
    public void copyFrom(AbstractObjectWapper<?> objectWapper) throws FileException {

        copyFromWithTimeStamp(objectWapper);
    }

    void copyFromWithTimeStamp(AbstractObjectWapper<?> objectWapper) throws FileException {

        FileObject currentObject = getObject();
        FileObject dst = (FileObject) objectWapper.getObject();
        objectManipulationAdapter.copy(currentObject, dst);
    }

    @Override
    public boolean doIsDir() throws FileException {

        try {
            return this.object.isFolder();
        } catch (FileSystemException e) {
            throw new FileException();
        }

    }

    @Override
    public List<AbstractObjectWapper<?>> listChildren() throws FileException {

        try {
            List<AbstractObjectWapper<?>> abstractObjectWappers = new ArrayList<AbstractObjectWapper<?>>();

            FileObject[] fileObjects = this.object.getChildren();
            if (fileObjects != null && fileObjects.length > 0) {
                for (FileObject fileObject : fileObjects) {
                    // TODO cache obj
                    SftpObjectWapper objectWapper = new SftpObjectWapper(fileObject);
                    abstractObjectWappers.add(objectWapper);
                }
            }
            return abstractObjectWappers;

        } catch (FileSystemException e) {
            throw new FileException();
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
    public Map<String, AbstractObjectWapper<?>> mapChildren() throws FileException {

        try {
            Map<String, AbstractObjectWapper<?>> abstractObjectWappers = new HashMap<>();

            FileObject[] fileObjects = this.object.getChildren();
            if (fileObjects != null && fileObjects.length > 0) {
                for (FileObject fileObject : fileObjects) {
                    // TODO cache obj
                    SftpObjectWapper objectWapper = new SftpObjectWapper(fileObject);
                    String baseName = objectWapper.getBaseFileName();
                    abstractObjectWappers.put(baseName, objectWapper);
                }
            }

            return abstractObjectWappers;

        } catch (FileSystemException e) {
            throw new FileException();
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
            return this.object.getContent().getSize();

        } catch (FileSystemException e) {
            throw new FileException();
        }
    }

    @Override
    public AbstractObjectWapper<?> createChildDir(String srcBaseName) throws FileException {
        FileObject newChild;
        try {
            newChild = this.object.resolveFile(srcBaseName);
            
            SftpObjectWapper objectWapper = new SftpObjectWapper(newChild);
            objectWapper.setDirectory(true);
            objectWapper.setExist(false);
            return objectWapper;
        } catch (FileSystemException e) {
            throw new FileException();
        }

    }

}
