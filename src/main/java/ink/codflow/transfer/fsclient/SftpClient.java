package ink.codflow.transfer.fsclient;

import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.IdentityRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SftpClient {
    
    String username;
    String password;
    Session session;
    
    
    JSch jsch = new JSch();

    boolean connect() {
        String host = null;
        int port =22;
        try {
            
            jsch.getSession(username, host, port);
            session.setPassword(password);
            IdentityRepository id = null;
            id.getIdentities();
            session.setIdentityRepository(id);
            Properties sshConfig = new Properties();
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            return false;
        }
        return false;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
    
    
    

}
