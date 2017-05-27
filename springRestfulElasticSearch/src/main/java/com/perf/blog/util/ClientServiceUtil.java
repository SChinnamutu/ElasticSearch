package com.perf.blog.util;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.springframework.stereotype.Component;

@Component
public class ClientServiceUtil {

	public Client getClient(){
		Client client = null;
		try {
			Node node  = nodeBuilder().node();
			client = node.client(); 
		} catch (Exception e) {
			System.out.println("Error occured while creating client");
		}
		return client;
	}
	
}
