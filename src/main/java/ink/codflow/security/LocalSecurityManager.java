package ink.codflow.security;

import java.io.File;
import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import ink.codflow.exception.SecurityInitFailureException;
import ink.codflow.transfer.fsclient.*;

public class LocalSecurityManager {

    


    public static byte[] seekLocalUserPrivateKey() throws SecurityInitFailureException {

        LocalVfsClient client = new LocalVfsClient();
        String userhome = System.getProperty("user.home");
        String keyDirPath = userhome + "//.ssh//id_rsa";
        FileObject resFileObject = client.resolve(keyDirPath);

        try {

            if (resFileObject.exists()) {
                return resFileObject.getContent().getByteArray();
            }

        } catch (IOException e) {

            // continue
        }
        throw new SecurityInitFailureException();

    }

    public static String seekLocalUserPrivateKeyPath(){
        String userhome = System.getProperty("user.home");
        String keyDirPath = userhome + "/.ssh/id_rsa";
        return keyDirPath;
    }
    
    public static String getCurrentUserName(){

        return System.getProperty("user.name");
    }
    
    

    public static void main(String[] args) throws SecurityInitFailureException {
        byte[] key = seekLocalUserPrivateKey();
        System.out.println(key);
    }

}
