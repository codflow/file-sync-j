package ink.codflow.sync.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ink.codflow.sync.exception.FileException;
import ink.codflow.sync.transfer.Client;

public class ClientEndpoint<T> {

    static final Random RANDOM = new Random();

    int id;

    String root;

    List<Client<T>> clientList = new ArrayList<>();

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public synchronized void addClient(Client<T> client) {
        clientList.add(client);
    }

    public AbstractObjectWapper<T> doResolve(String uri) {

        AbstractObjectWapper<T> abstractObjectWapper0 = getRandomClient().resolveWapper(uri);
        abstractObjectWapper0.setEndpoint(this);

        return abstractObjectWapper0;
    }

    public AbstractObjectWapper<T> resolve(String path) throws FileException {

        if (path.charAt(0) != '/' && path.charAt(1)!=':' ) {
            if (this.getRoot() != null) {
                String path0 = new StringBuilder(this.getRoot()).append(path).toString();
                return doResolve(path0);
            } else {
                throw new FileException("root path is not set,cant resolve relative path");
            }
        }
        return doResolve(path);

    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    // TODO read lock
    public synchronized Client<T> getRandomClient() {
        int size = this.clientList.size();
        int index = size > 1 ? RANDOM.nextInt(size) : 0;
        return this.clientList.get(index);
    }

    public List<AbstractObjectWapper<?>> list(String uri) throws FileException {

        AbstractObjectWapper<?> current = resolve(uri);
        List<?> list = current.listChildren();
        List<AbstractObjectWapper<?>> list0 = new ArrayList<AbstractObjectWapper<?>>();
        //cast directly?
        for (Object object : list) {
            if (object instanceof AbstractObjectWapper<?>) {
                AbstractObjectWapper<?> wapper = (AbstractObjectWapper<?>) object;
                list0.add(wapper);
            }
        }
        return list0;
    }

}
