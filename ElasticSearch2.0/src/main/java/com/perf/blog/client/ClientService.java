package com.perf.blog.client;

import org.elasticsearch.client.Client;

public interface ClientService {
	 Client getClient();
	 void addNewNode(String name);
	 void removeNode(String nodeName);
}
