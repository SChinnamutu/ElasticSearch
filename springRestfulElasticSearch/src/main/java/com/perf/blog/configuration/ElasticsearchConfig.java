package com.perf.blog.configuration;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.IOException;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.perf.blog.repository")
public class ElasticsearchConfig {
	
	private Client client = null;
	
	@Bean
	ElasticsearchOperations elasticsearchTemplate() throws IOException {

		/*// transport client
		Client client;
		try {
		client = TransportClient(Settings.put("cluster.name", "production").put("node.name","node1")).build().addTransportAddress(
		new InetSocketTransportAddress(InetAddress.getByName(""), 9300));
		 
		 return new ElasticsearchTemplate(client);
		}catch(Exception e){
			e.printStackTrace();
		}*/
		 // node client
		
		
		try {
			 client = nodeBuilder()
						.local(true)
						.settings(
								ImmutableSettings.settingsBuilder()
										.put("cluster.name", "elasticsearch")
										//.put("username", "root")
										//.put("password", "admin")
										.build()).node().client();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ElasticsearchTemplate(client);
	}
	
	@Bean
	Client getClient() throws IOException {

		/*// transport client
		Client client;
		try {
		client = TransportClient(Settings.put("cluster.name", "production").put("node.name","node1")).build().addTransportAddress(
		new InetSocketTransportAddress(InetAddress.getByName(""), 9300));
		 
		 return new ElasticsearchTemplate(client);
		}catch(Exception e){
			e.printStackTrace();
		}*/
		 // node client
		
		
		try {
			 client = nodeBuilder()
						.local(true)
						.settings(
								ImmutableSettings.settingsBuilder()
										.put("cluster.name", "elasticsearch")
										//.put("username", "root")
										//.put("password", "admin")
										.build()).node().client();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}
}