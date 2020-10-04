package ink.codflow.sync.core.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.vfs2.FileSystemException;

import ink.codflow.sync.consts.ObjectType;
import ink.codflow.sync.core.AbstractStreamObjectWapper;
import ink.codflow.sync.exception.FileException;

public class DefaultStreamObjectManiputationAdapter
        implements ObjectManipulationAdapter<AbstractStreamObjectWapper, AbstractStreamObjectWapper> {

    @Override
    public Class<AbstractStreamObjectWapper> getSrcClassType() {
        return AbstractStreamObjectWapper.class;
    }

    @Override
    public Class<AbstractStreamObjectWapper> getDestClassType() {
        return AbstractStreamObjectWapper.class;

    }

    @Override
    public void copy(AbstractStreamObjectWapper src, AbstractStreamObjectWapper dest) throws FileException {

    }

    @Override
    public void copyFileToFile(AbstractStreamObjectWapper srcFile, AbstractStreamObjectWapper destFile)
            throws FileException {
        InputStream in = srcFile.fileInputStream();
        destFile.copyContentFrom(in);

    }

    @Override
    public void copyFileToDir(AbstractStreamObjectWapper srcFile, AbstractStreamObjectWapper destDir)
            throws FileException {
        // TODO ck type
        String srcBaseName = srcFile.getBaseFileName();
      
        AbstractStreamObjectWapper destFile = destDir.createChild(srcBaseName, false);
        if (destFile.isExist() && !destFile.isFile()) {
            destFile.remove();
        }
        copyFileToFile(srcFile, destFile);

    }

    @Override
    public void copyDirToDir(AbstractStreamObjectWapper srcDir, AbstractStreamObjectWapper destDir)
            throws FileException {
                
                if (!srcDir.isExist()) {
                    throw new FileException();
                }
    
                // Locate the files to copy across
                List<AbstractStreamObjectWapper> srcList= srcDir.listChildren();
                // Copy everything across
                for (final AbstractStreamObjectWapper srcFile : srcList) {
                    // Determine the destination file
                    final String srcBaseName = srcDir.getBaseFileName();
                    final AbstractStreamObjectWapper destFile0 = destDir.createChild(srcBaseName, true);
                    
                    // Clean up the destination file, if necessary
                    if (destFile0.isExist() && !(destFile0.getType().equals( srcFile.getType()))) {
                        // The destination file exists, and is not of the same ltype,
                        // so delete it
                        // TODO - add a pluggable policy for deleting and overwriting existing files
                        destFile0.remove();
                    }
    
                    // Copy across
                    // try {
                    //     // if (srcFile.getType().equals(ObjectType.FILE)) {

                    //     //     InputStream in = srcFile.fileInputStream();
                    //     //     destFile0.copyContentFrom(in);
                    //     //     long modTime = srcFile.getLastMod();
                    //     //     //destFile0.setModTime
                    //     // } else if (srcFile.getType().hasChildren()) {
                    //     //     destFile0.createFolder();
                    //     //     // change dir ts ?
                    //     //     // long modTime = srcFile.getContent().getLastModifiedTime();
                    //     //     // destFile.getContent().setLastModifiedTime(modTime);
                    //     // }
                    // } catch (final IOException e) {
    
                    //    // throw new FileSystemException("vfs.provider/copy-file.error", e, srcFile, destFile0);
                    // }
                }

    }

    @Override
    public boolean checkDiff(AbstractStreamObjectWapper fileObject0, AbstractStreamObjectWapper fileObject1)
            throws FileException {
        // TODO Auto-generated method stub
        return false;
    }

}