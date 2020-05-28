package ink.codflow.sync.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ink.codflow.transfer.fsclient.VFSClient;

public class ClientEndpoint {

	static final Random RANDOM = new Random();

	int id;

	String root;

	List<VFSClient> clientList = new ArrayList<>();

	public int getId() {

		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public synchronized void addClient(VFSClient client) {
		clientList.add(client);
	}

	public AbstractObjectWapper<?> resolve(String uri) {
		int size = clientList.size();

		AbstractObjectWapper<?> abstractObjectWapper = new VfsObjectWapper(uri, this);

		return abstractObjectWapper;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	// TODO read lock
	public synchronized VFSClient getRandomClient() {
		int size = this.clientList.size();
		int index = size > 1 ? RANDOM.nextInt(size) : 0;
		return this.clientList.get(index);
	}

}
