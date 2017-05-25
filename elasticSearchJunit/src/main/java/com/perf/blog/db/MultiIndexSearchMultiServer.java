package com.perf.blog.db;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.status.IndicesStatusResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.perf.blog.client.service.impl.SearchClientServiceMockImpl;
import com.perf.blog.constant.PerfConstant;
import com.perf.blog.model.Article;

public class MultiIndexSearchMultiServer {

	 public static void main(String[] args) {
		    MultiIndexSearchMultiServer index =  new MultiIndexSearchMultiServer();
	    	List<Article> articles = new DBIntegration().getArticles();
	    	index.doMechanism(articles);
	    	//index.checkIndexExist();
	  }
	 
	 public void checkIndexExist(){
		 SearchClientServiceMockImpl impl = new SearchClientServiceMockImpl();  
		 Client client = impl.getClientTest(); 
		 isIndexExist(client,"employee");
	 }
	 
	 public void delete(){
		 SearchClientServiceMockImpl impl = new SearchClientServiceMockImpl();  
		 Client client = impl.getClientTest(); 
		 this.deleteAllIndeices(client);
	 }
	 
	 //server2
	 public void createIndex(){
		 SearchClientServiceMockImpl impl = new SearchClientServiceMockImpl();  
		 Client client = impl.getClientTest(); 
		 CreateIndexResponse cir = null;
		 try {
			 cir = client.admin().indices().prepareCreate("library").execute().actionGet();
		 } catch (Exception e) {
			e.printStackTrace();
		 }
		 System.out.println("Is index create? " + cir.isAcknowledged());
	 } 
	 
			
	 public void doMechanism(List<Article> articles) {
		 SearchClientServiceMockImpl impl = new SearchClientServiceMockImpl();  
		 Client client = impl.getClientTest();
		  
		 boolean isParent = true,childRelation = true;
		 //Node node     = nodeBuilder().node();
		 //Client client = node.client();
		 //Client client = impl.getClient();
		 
		 this.deleteAllIndeices(client);
		 /*for (int i = 0; i < PerfConstant.ELS_INDICES.length; i++) {
			 System.out.println("Checking Index Name is " + PerfConstant.ELS_INDICES[i]);
	    	 if(this.isIndexExist(client, PerfConstant.indices[i])){
	    		 this.deleteIndex(client,PerfConstant.ELS_INDICES[i]);
			 }
			 System.out.println("After Exist Index " + client.admin().indices().prepareExists(PerfConstant.ELS_INDICES[i]).get().isExists());
		 }*/
		for (Article article : articles) {
	    	 String [] ar = new String[1]; 
		     ar[0] = article.getTags();
			 try {
				 client.prepareIndex().setIndex(PerfConstant.ELS_INDICES[0]).setType(PerfConstant.ELS_TYPES[0]).setId(article.getId())
				 .setSource(putJsonDocument(PerfConstant.ELS_INDICES[0],article)).execute().actionGet();
				 client.prepareIndex().setIndex(PerfConstant.ELS_INDICES[1]).setType(PerfConstant.ELS_TYPES[1]).setId(article.getId())
				 .setSource(putJsonDocument(PerfConstant.ELS_INDICES[1],article)).execute().actionGet();
				 client.prepareIndex().setIndex(PerfConstant.ELS_INDICES[2]).setType(PerfConstant.ELS_TYPES[2]).setId(article.getId())
				 .setSource(putJsonDocument(PerfConstant.ELS_INDICES[2],article)).execute().actionGet();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}   
	    try {
	    	getNumberOfDocuments(client,PerfConstant.ELS_INDICES[0]);
	    	getEntityUsingJoinBtweeenMultipleIndicesForeignKey(client,isParent,childRelation);
	    	getEntityUsingJoinBtweeenMultipleIndicesPrimaryKey(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	   // impl.closeAllNodes();
	    
		}
		
		public Map<String, Object> putJsonDocument(String index,Article article){
	        Map<String, Object> jsonDocument = new HashMap<String, Object>();
	        jsonDocument.put("title", article.getTitle());
	        jsonDocument.put("content", article.getContent());
	        jsonDocument.put("postDate", article.getPostDate());
	        jsonDocument.put("tags", article.getTags());
	        jsonDocument.put("author", article.getAuthor());
	        jsonDocument.put("id", article.getId());
	        if(index.equalsIgnoreCase("employee")){
	        	jsonDocument.put("pk", article.getId());
	        }
	        if(index.equalsIgnoreCase("role")){
	        	jsonDocument.put("rpk", article.getId());
	        }
	        if(index.equalsIgnoreCase("address")){
	        	jsonDocument.put("adpk", article.getId());
	        }
	        return jsonDocument;
	    }
	 
		public void getEntityUsingJoinBtweeenMultipleIndicesForeignKey(Client client, boolean isParent, boolean childRelation){
			QueryBuilder queryBuilder = null;
			SearchResponse response = null;
			String indexName = "employee" ;
			try {
				QueryBuilder builders = this.getQuery(indexName,isParent,childRelation);
				queryBuilder = QueryBuilders.indicesQuery(builders,PerfConstant.ELS_INDICES);
				Thread.sleep(100);
				response = client.prepareSearch(PerfConstant.indices).setQuery(queryBuilder).execute().actionGet();
				System.out.println(response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void getEntityUsingJoinBtweeenMultipleIndicesPrimaryKey(Client client){
			QueryBuilder queryBuilder = null;
			SearchResponse response = null;
			try {
				queryBuilder = QueryBuilders.indicesQuery(this.getQuery(),PerfConstant.ELS_INDICES);
				Thread.sleep(100);
				response = client.prepareSearch(PerfConstant.indices).setQuery(queryBuilder).execute().actionGet();
				System.out.println(response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		private QueryBuilder getQuery() {
			return QueryBuilders.boolQuery().should(QueryBuilders.fieldQuery("id", "10"));
		}

		private QueryBuilder getQuery(String indexName, boolean isParent, boolean childRelation) {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			QueryBuilder builder =  null;
			if(indexName.equalsIgnoreCase(PerfConstant.indices[0])){
				builder  = boolQueryBuilder.should(QueryBuilders.fieldQuery("pk", "10"));
			}
			if(isParent){
				builder = boolQueryBuilder.should(QueryBuilders.fieldQuery("rpk", "10"));
			}
			if(childRelation){
				builder = boolQueryBuilder.should(QueryBuilders.fieldQuery("adpk", "10"));
			}
			System.out.println("Query is  " + builder);
			return builder;
		}

		//Get All Indices Name
		private void deleteAllIndeices(Client client){
			try {
				  String[] indexList = client.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().concreteAllIndices();
				  System.out.println("Index List Size size : " + indexList.length);
				  for (String index : indexList) {
					  System.out.println("Index Name " + index);
					  deleteIndex(client,index);
			      }
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		 
		private void deleteIndex(Client client,String index){
			 DeleteIndexResponse deleteResponse = client.admin().indices().delete(new DeleteIndexRequest(index)).actionGet();
			 System.out.println(index + " is deleted "+ deleteResponse.isAcknowledged());
		 }
		 
		public boolean isIndexExist(Client client,String indexName){
			 boolean isExist  = client.admin().indices().prepareExists(indexName).get().isExists();
			 System.out.println(indexName  + " is Exist? " + isExist);
			 return isExist;
		 }
		
		public long getNumberOfDocuments(Client client,String indexName) {
			 Long count  = null;
			 IndicesStatusResponse response = null;
		     try {
		    	 response = client.admin().indices().prepareStatus(indexName).execute().actionGet();
			     count = response.getIndex(indexName).getDocs().getNumDocs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		    return count;
		 }
}
