package ink.codflow.sync.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.vfs2.AllFileSelector;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileUtil;
import org.apache.commons.vfs2.NameScope;
import org.apache.commons.vfs2.util.FileObjectUtils;

import ink.codflow.exception.BackupFileChangedException;

public class VfsSynchronzer {

    
    
    public void sync(FileObject src, FileObject dst, boolean full) {
        if (full) {
            fullCopy(src, dst);
        } else {
            syncIncreaseFile(src, dst);
        }
    }

    void fullCopy(FileObject src, FileObject dst) {
        try {
            copyWithTimestamp(src, dst);
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
    }

    public void syncIncreaseFile(FileObject src, FileObject dst) {
        try {
            if (!src.isFile()) {

                FileObject[] srcSubFiles = src.getChildren();
                FileObject[] dstSubFiles = dst.getChildren();
                HashMap<String, FileObject> dstFileNameMap = fileNameHashMap(dstSubFiles);
                for (FileObject fileObject : srcSubFiles) {
                    String currentSourcefileName = getFileBaseName(fileObject);
                    if (dstFileNameMap.containsKey(currentSourcefileName)) {
                        if (fileObject.isFile()) {
                            FileObject object = dstFileNameMap.get(currentSourcefileName);
                            syncIncreaseFile(fileObject, object);
                        } else {
                            FileObject nextDir = dst.getChild(currentSourcefileName);
                            syncIncreaseFile(fileObject, nextDir);
                        }
                    } else {
                        doCopyToDir(fileObject, dst);
                    }
                    FileName fileName = fileObject.getName();
                    String name = fileName.getBaseName();
                    try {
                        dst.getChild(name);
                    } catch (FileSystemException e) {
                        System.out.println("file not exists");
                    }
                }
            } else { 
                boolean isDiff = isDiffFile(src, dst);
                if (isDiff) {
                    doCopy(src, dst);
                }
            }

        } catch (FileSystemException e) {
            e.printStackTrace();
        }
    }

    
    
    long countIncreaseFile(FileObject src, FileObject dst) {

        try {
            if (!src.isFile()) {

                FileObject[] srcSubFiles = src.getChildren();
                FileObject[] dstSubFiles = dst.getChildren();
                HashMap<String, FileObject> dstFileNameMap = fileNameHashMap(dstSubFiles);
                for (FileObject fileObject : srcSubFiles) {
                    String currentSourcefileName = getFileBaseName(fileObject);
                    if (dstFileNameMap.containsKey(currentSourcefileName)) {
                        if (fileObject.isFile()) {
                            FileObject object = dstFileNameMap.get(currentSourcefileName);
                            syncIncreaseFile(fileObject, object);
                        } else {
                            FileObject nextDir = dst.getChild(currentSourcefileName);
                            syncIncreaseFile(fileObject, nextDir);
                        }
                    } else {
                        return doCountDirFileNumber(fileObject);
                    }
                    FileName fileName = fileObject.getName();
                    String name = fileName.getBaseName();
                    try {
                        dst.getChild(name);
                    } catch (FileSystemException e) {
                        System.out.println("file not exists");
                    }
                }
            } else {
                boolean isDiff = isDiffFile(src, dst);
                if (isDiff) {
                    doCopy(src, dst);
                }
            }

        } catch (FileSystemException e) {
            e.printStackTrace();
        }
        return 0;
    }

    long doCountDirFileNumber(FileObject fileObject) throws FileSystemException {
        if (fileObject.isFile()) {
            FileObject[] fileObjects = fileObject.getChildren();
            long ctp = 0;
            for (int i = 0; i < fileObjects.length; i++) {
                FileObject obj = fileObjects[i];
                if (obj.isFile()) {
                    ctp++;
                } else {
                    ctp = ctp + doCountDirFileNumber(obj);
                }
            }
            return ctp;
        }
        return 1;
    }

    
    
    private void doCopyToDir(FileObject fileObject, FileObject tagetDir) {
        String fileName = fileObject.getName().getBaseName();
        try {
            FileObject targetFile = tagetDir.resolveFile(fileName);
            if (!targetFile.exists()) {

                copyWithTimestamp(fileObject, targetFile);
            }

        } catch (FileSystemException e) {
            e.printStackTrace();
        }
        String name = fileObject.getName().getBaseName();
        String msg = new StringBuilder("Do copy:").append(name).toString();
        System.out.println(msg);
    }

    private void doCopy(FileObject fileObject, FileObject tagetObject) {
        try {
            // long modTime = fileObject.getContent().getLastModifiedTime();
            // tagetObject.copyFrom(fileObject, allFileSelector);

            copyWithTimestamp( fileObject,tagetObject);

            // tagetObject.getContent().setLastModifiedTime(modTime);
            String name = tagetObject.getName().getBaseName();
            String msg = new StringBuilder("Do copy:").append(name).toString();
            System.out.println(msg);


        } catch (FileSystemException e) {
            e.printStackTrace();
        }
    }

    public boolean isDiffFile(FileObject src, FileObject dst) {

        long srcModTime;
        try {
            srcModTime = src.getContent().getLastModifiedTime();
            long dstModTime = dst.getContent().getLastModifiedTime();

            int compareInt = Long.compare(srcModTime, dstModTime);
            if (compareInt > 0) {
                return true;
            } else if (compareInt == 0) {
                return false;
            }
            // return true;
            throw new BackupFileChangedException();

        } catch (FileSystemException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    HashSet<String> fileNameHashSet(FileObject[] fileObjects) {

        HashSet<String> nameSet = new HashSet<String>();
        for (int i = 0; i < fileObjects.length; i++) {
            nameSet.add(fileObjects[i].getName().getBaseName());
        }
        return nameSet;

    }

    HashMap<String, FileObject> fileNameHashMap(FileObject[] fileObjects) {

        HashMap<String, FileObject> nameSet = new HashMap<>();
        for (int i = 0; i < fileObjects.length; i++) {
            nameSet.put(fileObjects[i].getName().getBaseName(), fileObjects[i]);
        }
        return nameSet;

    }

    String getFileBaseName(FileObject fileObject) {
        return fileObject.getName().getBaseName();
    }

    void copyWithTimestamp(FileObject file, FileObject dst) throws FileSystemException {

        AllFileSelector selector = new AllFileSelector();
        if (!FileObjectUtils.exists(file)) {
            throw new FileSystemException("vfs.provider/copy-missing-file.error", file);
        }

        // Locate the files to copy across
        final ArrayList<FileObject> files = new ArrayList<>();
        file.findFiles(selector, false, files);

        // Copy everything across
        for (final FileObject srcFile : files) {
            // Determine the destination file
            final String relPath = file.getName().getRelativeName(srcFile.getName());
            final FileObject destFile = dst.resolveFile(relPath, NameScope.DESCENDENT_OR_SELF);

            // Clean up the destination file, if necessary
            if (FileObjectUtils.exists(destFile) && destFile.getType() != srcFile.getType()) {
                // The destination file exists, and is not of the same type,
                // so delete it
                // TODO - add a pluggable policy for deleting and overwriting existing files
                destFile.deleteAll();
            }

            // Copy across
            try {
                if (srcFile.getType().hasContent()) {
                    FileUtil.copyContent(srcFile, destFile);
                    long modTime = srcFile.getContent().getLastModifiedTime();
                    destFile.getContent().setLastModifiedTime(modTime);
                } else if (srcFile.getType().hasChildren()) {
                    destFile.createFolder();
                    // long modTime = srcFile.getContent().getLastModifiedTime();
                    // destFile.getContent().setLastModifiedTime(modTime);
                }
            } catch (final IOException e) {
                throw new FileSystemException("vfs.provider/copy-file.error", e, srcFile, destFile);
            }
        }
    }
}
