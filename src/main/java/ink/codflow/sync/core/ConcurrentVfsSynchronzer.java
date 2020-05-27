package ink.codflow.sync.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import ink.codflow.transfer.fsclient.*;

public class ConcurrentVfsSynchronzer extends VfsSynchronzer {

    

    List<SftpVfsClient> sourceClients = new ArrayList<>();

    LinkedList<ObjectPairNode> filePairNodeList = new LinkedList<>();



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
        System.out.println("do copy to filedir");
    }

    private void doCopy(FileObject fileObject, FileObject tagetObject) {
        try {
            // long modTime = fileObject.getContent().getLastModifiedTime();
            // tagetObject.copyFrom(fileObject, allFileSelector);

            copyWithTimestamp(tagetObject, fileObject);

            // tagetObject.getContent().setLastModifiedTime(modTime);
            String name = tagetObject.getName().getBaseName();
            System.out.println(name);

        } catch (FileSystemException e) {
            e.printStackTrace();
        }
    }

}