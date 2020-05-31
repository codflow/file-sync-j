package ink.codflow.sync.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ink.codflow.transfer.vfs.Client;

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

	public AbstractObjectWapper<T> resolve(String uri) {

		AbstractObjectWapper<T> abstractObjectWapper0 = getRandomClient().resolveWapper(uri);
		abstractObjectWapper0.setEndpoint(this);

		return abstractObjectWapper0;
	}
	

	public AbstractObjectWapper<T> resolveRelatively(String uri) {

		String path =new  StringBuilder(this.getRoot()).append(uri).toString(); 

		return resolve(path);
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

}
