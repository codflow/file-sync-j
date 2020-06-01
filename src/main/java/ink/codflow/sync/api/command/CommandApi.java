package ink.codflow.sync.api.command;

import org.apache.commons.vfs2.FileObject;

import ink.codflow.sync.core.VfsSynchronzer;
import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.exception.SecurityInitFailureException;
import ink.codflow.sync.security.LocalSecurityManager;
import ink.codflow.sync.transfer.vfs.LocalVfsClient;
import ink.codflow.sync.transfer.vfs.SftpVfsClient;

public class CommandApi {

    void createSimpleTask(String srchost, String userName ,String password ,String srcPath, String localDstPath) {



        try {
            String privateKeyPath = LocalSecurityManager.seekLocalUserPrivateKeyPath();
             
            SftpVfsClient sftpClient = new SftpVfsClient(srchost);

            if (password!= null && !password.isEmpty() ){
                sftpClient.addPasswordIdentity(srchost,userName, password);
            }else{
                sftpClient.addPublicKeyIdentity(userName,privateKeyPath);

            }

            LocalVfsClient localClient = new LocalVfsClient();

            FileObject srcObject = sftpClient.resolve(srcPath);

            FileObject distObject = localClient.resolve(localDstPath);
            VfsSynchronzer syncer = new VfsSynchronzer();
            syncer.syncIncreaseFile(srcObject, distObject);

        } catch (Error e) {
            e.printStackTrace();
            throw new  RuntimeException("sync failed!");

        } catch (FileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


    }











    public void dispatch(String[] args) {
        String srcPath = null;

        String srchost = null;

        String localDstPath = null;

        String dstUser = null;

        String password = null;
        for (int i = 0; i < args.length; i++) {

            switch (args[i]) {
                case "-u":
                    dstUser = args[i + 1];
                    break;
                case "-p":
                    password = args[i + 1];
                    break;

                case "-D":
                    localDstPath = args[i + 1];
                    break;
                case "-S":
                    srcPath = args[i + 1];
                    break;
                case "-remote":
                    srchost = args[i + 1];
                    break;

                default:
                    break;
            }
        }

        if (srchost!= null && dstUser != null  && srcPath != null && localDstPath !=null && !srchost.isEmpty() && !dstUser.isEmpty()&&!srcPath.isEmpty()&&  !localDstPath.isEmpty() ) {
            createSimpleTask(srchost,dstUser,password,srcPath,localDstPath);
        }else{
            throw new  RuntimeException("args are not complete!");
        }


    }




}
