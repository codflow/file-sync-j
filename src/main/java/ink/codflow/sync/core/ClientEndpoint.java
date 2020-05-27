package ink.codflow.sync.core;

import java.util.concurrent.ConcurrentSkipListSet;

import ink.codflow.transfer.fsclient.SftpVfsClient;

public class ClientEndpoint {
    int id;
    
    String root;

    ConcurrentSkipListSet<SftpVfsClient> clientList;

    
    public int getId() {
        
        return id;
    }

    public void setId(int id) {
        
        this.id = id;
    }


    
    public void addClient(SftpVfsClient client){
        clientList.add(client);
    }
    
    
    public AbstractObjectWapper<?> resolve(String uri){
        int size =  clientList.size();
        AbstractObjectWapper<?> abstractObjectWapper =  new  SftpObjectWapper(uri,this);
        return abstractObjectWapper;
    }
    
    
}
