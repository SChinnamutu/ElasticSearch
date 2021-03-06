package com.perf.blog.db;

import static org.elasticsearch.index.query.QueryBuilders.fieldQuery;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;

import com.perf.blog.model.Article;


public class JavaAPIMain {
    
	
	public static void main(String[] args) {
		List<Article> articles = new DBIntegration().getArticles();
		new  JavaAPIMain().beganSearch(articles);
	}
	
	public Client getClient(){
		Node node     = nodeBuilder().node();
		return node.client();
	}
	
	 //Exist Index
    public boolean isIndexExists(Client  client , String indexName){
    	boolean isExist = client.admin().indices().prepareExists(indexName).get().isExists();
        return isExist;
    }
	 
	 //Delete index
	 public void deleteIndex(Client client,String index){
		 DeleteIndexResponse deleteResponse = client.admin().indices().delete(new DeleteIndexRequest(index)).actionGet();
		 System.out.println(index + " is deleted? "+ deleteResponse.isAcknowledged());
	 }
	 
	public void beganSearch(List<Article> articles) {
		 Client client = getClient();
		 if(isIndexExists(client, "kodcucom")){
			 System.out.println("Yes Index is existing and goign to delete");
			 deleteIndex(client, "kodcucom");
		 }
		 
	     for (Article article : articles) {
	    	 String [] ar = new String[1]; 
		     ar[0] = article.getTags();
	    	 client.prepareIndex("kodcucom", "article", article.getId()).setSource(putJsonDocument(article)).execute().actionGet();
		}   
	    getDocument(client, "kodcucom", "article", "10");
	    updateDocument(client, "kodcucom", "article", "1", "title", "ElasticSearch: Java API");
        updateDocument(client, "kodcucom", "article", "1", "tags", new String[]{"bigdata"});
        getDocument(client, "kodcucom", "article", "1");
        searchDocument(client, "kodcucom", "article", "title", "ElasticSearch");
        deleteDocument(client, "kodcucom", "article", "1");
        //node.close();
	}
	
	 public Map<String, Object> putJsonDocument(Article article){
	        Map<String, Object> jsonDocument = new HashMap<String, Object>();
	        jsonDocument.put("title", article.getTitle());
	        jsonDocument.put("content", article.getContent());
	        jsonDocument.put("postDate", article.getPostDate());
	        jsonDocument.put("tags", article.getTags());
	        jsonDocument.put("author", article.getAuthor());
	        jsonDocument.put("pk", article.getId());
	        return jsonDocument;
	    }
	    
	    public void getDocument(Client client, String index, String type, String id){
	        GetResponse getResponse = client.prepareGet(index, type, id).execute().actionGet();
	        Map<String, Object> source = getResponse.getSource();
	        System.out.println("------------------------------");
	        System.out.println("Index: " + getResponse.getIndex());
	        System.out.println("Type: " + getResponse.getType());
	        System.out.println("Id: " + getResponse.getId());
	        System.out.println("Version: " + getResponse.getVersion());
	        System.out.println(source);
	        System.out.println("------------------------------");
	        System.out.print("getDocument() Success");
	    }
	    
	    public void updateDocument(Client client, String index, String type,String id, String field, String newValue){
	        Map<String, Object> updateObject = new HashMap<String, Object>();
	        updateObject.put(field, newValue);
	        client.prepareUpdate(index, type, id)
	              .setScript("ctx._source." + field + "=" + field)
	              .setScriptParams(updateObject).execute().actionGet();
	        System.out.print("updateDocument() Success");
	    }

	    public void updateDocument(Client client, String index, String type,String id, String field, String[] newValue){
	        String tags = "";
	        for(String tag :newValue)
	            tags += tag + ", ";
	        tags = tags.substring(0, tags.length() - 2);
	        Map<String, Object> updateObject = new HashMap<String, Object>();
	        updateObject.put(field, tags);
	        client.prepareUpdate(index, type, id)
	                .setScript("ctx._source." + field + "+=" + field)
	                .setScriptParams(updateObject).execute().actionGet();
	    }

	    public void searchDocument(Client client, String index, String type,String field, String value){
	        SearchResponse response = client.prepareSearch(index)
	                                        .setTypes(type)
	                                        .setSearchType(SearchType.QUERY_AND_FETCH)
	                                        .setQuery(fieldQuery(field, value))
	                                        .setFrom(0).setSize(60).setExplain(true)
	                                        .execute()
	                                        .actionGet();
	        SearchHit[] results = response.getHits().getHits();
	        System.out.println("Current results: " + results.length);
	        for (SearchHit hit : results) {
	            System.out.println("------------------------------");
	            Map<String,Object> result = hit.getSource();   
	            System.out.println(result);
	        }
	        System.out.println("updateDocument() success");
	    }
	    
	    public void deleteDocument(Client client, String index, String type, String id){
	        DeleteResponse response = client.prepareDelete(index, type, id).execute().actionGet();
	        System.out.println("Information on the deleted document:");
	        System.out.println("Index: " + response.getIndex());
	        System.out.println("Type: " + response.getType());
	        System.out.println("Id: " + response.getId());
	        System.out.println("Version: " + response.getVersion());
	        System.out.println("deleteDocument() success");
	    }

		


}
