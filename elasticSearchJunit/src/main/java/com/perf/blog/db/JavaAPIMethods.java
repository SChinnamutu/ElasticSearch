package com.perf.blog.db;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.fieldQuery;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.status.IndicesStatusResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.perf.blog.constant.PerfConstant;
import com.perf.blog.model.Article;
import com.perf.blog.util.ElasticSearchReservedWords;
import com.perf.blog.util.SearchDateUtils;

public class JavaAPIMethods {
	
	private Logger logger  = Logger.getLogger(JavaAPIMethods.class.getSimpleName());

	public static void main(String[] args) {
		 new JavaAPIMethods().beganSearch();
	}
	
	 public  void beganSearch() {
		 createNode();
	 }
	 
	 public Client createClient(){
		
		 String searchServerClusterName = "perficient" ;
		 Client client = null;
	     
		 if(client == null){
	            if (logger.isLoggable(Level.INFO))
	            {
	                logger.info("Creating client for Search!");
	            }            
	            //Try starting search client at context loading
	            try
	            {
	              
					Settings settings = ImmutableSettings.settingsBuilder().put(ElasticSearchReservedWords.CLUSTER_NAME.getText(), searchServerClusterName).build();
	                
	                TransportClient transportClient = new TransportClient(settings);

	                transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9200));
	                
	                if(transportClient.connectedNodes().size() == 0)
	                {
	                    logger.severe("There are no active nodes available for the transport, it will be automatically added once nodes are live!");
	                }
	                client = transportClient;
	            }
	            catch(Exception ex)
	            {
	                //ignore any exception, dont want to stop context loading
	                logger.severe("Error occured while creating search client! " + ex);
	            }
	        }
		 	logger.info("Client is create successfully and the client is " +client );
	        return client;
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
	 
	//Create anode
	 public void createNode(){
		 Node node     = nodeBuilder().node();
		 Client client = node.client();
		 System.out.println("Client created successfully and the client is " + client);
	 }
	 
	//Create anode
	 public Client getClient(){
		 Node node     = nodeBuilder().node();
		 Client client = node.client();
		 System.out.println("Client created successfully and the client is " + client);
		 return client;
	 }
	 
	 public  void dropIndex(){
	     String  indexName = null;   
	     Client client = getClient();
	     for (int i = 0; i < PerfConstant.indices.length; i++) {
	    	   indexName  = PerfConstant.indices[i];
	    	   if(this.isIndexExists(client,indexName)){
		            this.deleteIndex(client,indexName);
		       }
	     } 
	 }
	 
	 //Exist Index
    public boolean isIndexExists(Client  client , String indexName){
        return client.admin().indices().prepareExists(indexName).get().isExists();
    }
	 
	 //Delete index
	 public void deleteIndex(Client client,String index){
		 DeleteIndexResponse deleteResponse = client.admin().indices().delete(new DeleteIndexRequest(index)).actionGet();
		 System.out.println(index + " is deleted? "+ deleteResponse.isAcknowledged());
	 }
	 
	 //Get all documents from index  
	 public void getAllDocumentFromIndex(Client client){
		 int scrollSize = 1000;
		 int startsFrom = 0;
		 List<Map<String, Object>> esData = new ArrayList<Map<String,Object>>();
		 SearchResponse response = null;
		 try {
			response = client.prepareSearch("role").setTypes("perf")
												   .setQuery(QueryBuilders.matchAllQuery())
												   .setFrom(startsFrom * scrollSize)
												   .setSize(scrollSize)
												   .execute().actionGet();
			for(SearchHit hit : response.getHits()){
				System.out.println(hit);
				esData.add(hit.getSource());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
	 }
	
	//Get filed value
	public String getFieldValueOrNull(SearchHit searchHit, String fieldName){
        final SearchHitField searchHitField = searchHit.field(fieldName);
        if (searchHitField != null && searchHitField.value() != null) {
            return searchHitField.value().toString();
        }
        return null;
	} 
	
	public void findEntityUsingTermQueryById(Client client, String [] indices,String [] types){
		System.out.println("Execution of method findEntityUsingTermQueryWithMap");
		 try {
    		 SearchResponse response = client.prepareSearch(indices).setTypes(types).setSearchType(SearchType.QUERY_AND_FETCH)
        			 .setQuery(fieldQuery("pk", "10"))
                     .setFrom(0).setSize(100).setExplain(true)
        			 .execute().actionGet();
    		SearchHit[] results = response.getHits().getHits();
    		System.out.println("Current results: " + results.length);
    		for (SearchHit hit : results) {
    			System.out.println("------------------------------");
    			Map<String,Object> result = hit.getSource();   
    			System.out.println(result);
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Excisting of method findEntityUsingTermQueryWithMap");
	}
	
	
	public void findEntityUsingTermQueryWithMap(Client client, String [] indices,String [] types){
		 System.out.println("Execution of method findEntityUsingTermQueryWithMap");
		 Map<String,Object> queryBuilder = new HashMap<String,Object>();
		 queryBuilder.put("pk","10");
		 queryBuilder.put("author","Ronaldo");
    	 try {
    		 SearchResponse response = client.prepareSearch(indices).setTypes(types).setSearchType(SearchType.QUERY_AND_FETCH)
        			 .setQuery(queryBuilder)
        			 .setFrom(0).setSize(100).setExplain(true)
    		 		 .execute().actionGet();
    		SearchHit[] results = response.getHits().getHits();
    		System.out.println("Current results: " + results.length);
    		for (SearchHit hit : results) {
    			System.out.println("------------------------------");
    			Map<String,Object> result = hit.getSource();   
    			System.out.println(result);
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	System.out.println("Excisting of method findEntityUsingTermQueryWithMap");
	}
	
	//Intersection Single Index
	public void getEntityByMustMatchValuesInSingleIndex(Client client){
		try {
			SearchRequest request = Requests.searchRequest("employee").types("perf")
				    	.source(SearchSourceBuilder.searchSource().query(QueryBuilders.boolQuery()
				        .should(QueryBuilders.matchQuery("id", "3"))
				        .should(QueryBuilders.matchQuery("title", "Ronaldo"))
				        //.should(QueryBuilders.matchQuery("emails.canonical", "ben@ben.com"))
				        .minimumShouldMatch("1")));
			SearchResponse response2 =  client.search(request).actionGet();
			System.out.println(response2);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		System.out.println("Excisting of method getEntityByMustMatchValues");
	}
	
	//Intersection Multiple Index	
	public void getEntityByMustMatchValuesBetweenMultipleIndices(Client client){
		try {
			SearchRequest request = Requests.searchRequest(PerfConstant.indices).types(PerfConstant.types)
							.source(SearchSourceBuilder.searchSource().query(QueryBuilders.boolQuery()
					        .should(QueryBuilders.matchQuery("title", "Isco"))));
				        //.should(QueryBuilders.matchQuery("pk", "10"))
				       // .should(QueryBuilders.matchQuery("title", "Ronaldo"))
				        //.should(QueryBuilders.matchQuery("rpk", "10"))
				        //.should(QueryBuilders.matchQuery("adpk", "10"))
				        //.should(QueryBuilders.matchQuery("emails.canonical", "ben@ben.com"))
				        //.minimumShouldMatch("1")));
			SearchResponse response =  client.search(request).actionGet();
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Request Builder 
	//Match and retrieve entity in index
	public void getEntityUsingQueryString(Client client){
		try {
			SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
		            .setIndices("role")
		            .setTypes("perf")
		            .setQuery(QueryBuilders.queryString("Isco"))
		            .addField("title");
		    SearchResponse response = searchRequestBuilder.execute().actionGet();
		    System.out.println(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Request Builder
	public void getEntityUsingQueryStringBetweenMultipleIndices(Client client, String [] indices,String [] types){
		try {
			SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
		            .setIndices(indices)
		            .setTypes(types)
		            .setQuery(QueryBuilders.queryString("Isco"))
		            .addField("author");
		    SearchResponse response = searchRequestBuilder.execute().actionGet();
		    System.out.println(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	//Request Builder
	public void getEntityUsingQueryWithMatchFiledStringBetweenMultipleIndices(Client client, String [] indices,String [] types){
		try {
			SearchRequestBuilder searchRequestBuilder = client.prepareSearch()
		            .setIndices(indices)
		            .setTypes(types)
		            .setQuery(QueryBuilders.termQuery("id","10"));
		           // .addField("file.file");
		    SearchResponse response = searchRequestBuilder.execute().actionGet();
		    System.out.println(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Get All Indices Name
	public void listAllIndices(Client client){
		try {
			  String[] indexList = client.admin().cluster().prepareState().execute().actionGet().getState().getMetaData().concreteAllIndices();
			  System.out.println("Index List:");
			  for (String index : indexList) {
			       System.out.println(index);
		      }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Request builder for Multiple indices
	public void getEntityUsingSearchBuilder(Client client){
		try {
			SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(client);
			searchRequestBuilder.setIndices(PerfConstant.indices);
			searchRequestBuilder.setTypes(PerfConstant.types);
			//searchRequestBuilder.setIndices("role");
			//searchRequestBuilder.setTypes("perf");
			//searchRequestBuilder.setIndices("address");
			//searchRequestBuilder.setTypes("perf");
			QueryStringQueryBuilder queryStringQueryBuilder1 = new QueryStringQueryBuilder("10");
			queryStringQueryBuilder1.field("pk");
			QueryStringQueryBuilder queryStringQueryBuilder2 = new QueryStringQueryBuilder("10");
			queryStringQueryBuilder2.field("adpk");
			QueryStringQueryBuilder queryStringQueryBuilder3 = new QueryStringQueryBuilder("10");
			queryStringQueryBuilder3.field("rpk");
			searchRequestBuilder.setQuery(queryStringQueryBuilder1);
			searchRequestBuilder.setQuery(queryStringQueryBuilder2);
			searchRequestBuilder.setQuery(queryStringQueryBuilder3);
			SearchResponse response = searchRequestBuilder.execute().actionGet();
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Multi Search Response
	public void getEntityFromMultipleIndices(Client client){
		try {
			SearchRequestBuilder srb1 = client
				    .prepareSearch().setIndices("employee").setTypes("perf").setQuery(QueryBuilders.termQuery("pk", "10")).setSize(1);
			SearchRequestBuilder srb2 = client
				    .prepareSearch().setIndices("role").setTypes("perf").setQuery(QueryBuilders.termQuery("rpk", "10")).setSize(1);
			SearchRequestBuilder srb3 = client
				    .prepareSearch().setIndices("address").setTypes("perf").setQuery(QueryBuilders.termQuery("adpk", "10")).setSize(1);
						
			MultiSearchResponse sr = client.prepareMultiSearch()
			        .add(srb1)
			        .add(srb2)
			        .add(srb3)
			      //  .add(srb4)
			        .get();
			// You will get all individual responses from MultiSearchResponse#getResponses()
			long nbHits = 0;
			for (MultiSearchResponse.Item item : sr.getResponses()) {
			    SearchResponse response = item.getResponse();
			    System.out.println(response);
			    nbHits += response.getHits().getTotalHits();
			}
			System.out.println(nbHits);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Not working
	public void getEntityFromMultipleIndicesUsingArray(Client client){
		try {
			SearchRequestBuilder srb = client.prepareSearch().setIndices(PerfConstant.indices).setTypes(PerfConstant.types).setQuery(QueryBuilders.queryString("Isco")).setSize(1);	
			MultiSearchResponse sr = client.prepareMultiSearch().add(srb).get();
			// You will get all individual responses from MultiSearchResponse#getResponses()
			long nbHits = 0;
			for (MultiSearchResponse.Item item : sr.getResponses()) {
			    SearchResponse response = item.getResponse();
			    System.out.println(response);
			    nbHits += response.getHits().getTotalHits();
			}
			System.out.println(nbHits);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Indices with queryString
	public void indicesQueryWithtremQuery(Client client){
		QueryBuilder queryBuilder = null;
		try {
			queryBuilder = QueryBuilders.indicesQuery(QueryBuilders.termQuery("id", "10"),"employee", "role","address");//;.noMatchQuery(QueryBuilders.termQuery("tag", "kow"));
			SearchResponse response = client.prepareSearch("employee", "role","address").setQuery(queryBuilder).execute().actionGet();
			System.out.println("Fuck Off" +response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Indices with queryString
	public void indicesQueryWithQueryString(Client client){
		QueryBuilder queryBuilder = null;
		try {
			queryBuilder = QueryBuilders.indicesQuery(QueryBuilders.queryString("10"),"employee", "role","address");//;.noMatchQuery(QueryBuilders.termQuery("tag", "kow"));
			SearchResponse response3 = client.prepareSearch("employee", "role","address").setQuery(queryBuilder).execute().actionGet();
			System.out.println("Fuck Off3 " + response3);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	//Query without indices
	public void queryWithoutIndices(Client client){
		QueryBuilder queryBuilder = null;
		try {
			queryBuilder = QueryBuilders.indicesQuery(QueryBuilders.termQuery("id", "10"));//;.noMatchQuery(QueryBuilders.termQuery("tag", "kow"));
			SearchResponse response2 = client.prepareSearch("employee", "role","address").setQuery(queryBuilder).execute().actionGet();
			System.out.println(response2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//This is fuck i wanted actually
	public void getEntityUsingJoinBtweeenMultipleIndices(Client client){
		QueryBuilder queryBuilder = null;
		SearchResponse response = null;
		try {
			queryBuilder = QueryBuilders.indicesQuery(QueryBuilders.boolQuery()
					.should(QueryBuilders.fieldQuery("pk", "10"))
					.should(QueryBuilders.fieldQuery("rpk", "10"))
					.should(QueryBuilders.fieldQuery("adpk", "10")),"employee", "role","address");
			response = client.prepareSearch(PerfConstant.indices).setQuery(queryBuilder).execute().actionGet();
			System.out.println("Fuck Off4" +response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 public XContentBuilder getEmployeeBuilder(){
		 XContentBuilder contentBuilder = null;
		 try {
			 contentBuilder = jsonBuilder().prettyPrint().startObject();
			 contentBuilder.startObject(PerfConstant.indices[0]).field("pk", "10").endObject();
		 } catch (IOException e) {
			e.printStackTrace();
		 }
		 return contentBuilder;
	 }
	 
	 public XContentBuilder getRoleBuilder(){
		 XContentBuilder contentBuilder = null;
		 try {
			 contentBuilder = jsonBuilder().prettyPrint().startObject();
			 contentBuilder.startObject(PerfConstant.indices[1]).field("rpk", "10").endObject();
		 } catch (IOException e) {
			e.printStackTrace();
		 }
		 return contentBuilder;
	 }
	 
	 public XContentBuilder getAddressBuilder(){
		 XContentBuilder contentBuilder = null;
		 try {
			 contentBuilder = jsonBuilder().prettyPrint().startObject();
			 contentBuilder.startObject(PerfConstant.indices[2]).field("adpk", "10").endObject();
		 } catch (IOException e) {
			e.printStackTrace();
		 }
		 return contentBuilder;
	 }
	
	public IndexRequestBuilder getRequestEmployeeBuilder(Client client){
		XContentBuilder contentBuilder = null;
		IndexRequestBuilder indexRequestBuilder  = null;
		try {
			contentBuilder = getEmployeeBuilder();
			indexRequestBuilder = client.prepareIndex("employee", "pk", "10");
	        indexRequestBuilder.setSource(contentBuilder);
		} catch (Exception e) {
			
		}   
		return indexRequestBuilder;
	}
	
	 public BulkResponse processBulkRequests(List<IndexRequestBuilder> requests,Client client) {
        if (requests.size() > 0){
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            for (IndexRequestBuilder indexRequestBuilder : requests){
                bulkRequest.add(indexRequestBuilder);
            }
            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            if (bulkResponse.hasFailures()){
              System.out.println("bulk operation indexing has failures:" + bulkResponse.buildFailureMessage());
            }
            return bulkResponse;
        }else{
        	System.out.println("Executing bulk index request for size: 0");
            return null;
        }
	} 

	public void createIndexAndPutJsonDocument(List<Article> articles){
		 Node node     = nodeBuilder().node();
		 Client client = node.client();
		 for (int i = 0; i < 3; i++) {
			 System.out.println("Checking Index Name is " + PerfConstant.ELS_INDICES[i]);
	    	 if(client.admin().indices().prepareExists(PerfConstant.ELS_INDICES[i]).get().isExists()){
					deleteIndex(client,PerfConstant.ELS_INDICES[i]);
			 }
			 System.out.println("After Exist Index " + client.admin().indices().prepareExists(PerfConstant.ELS_INDICES[i]).get().isExists());
		 }
		 for (Article article : articles) {
	    	 String [] ar = new String[1]; 
		     ar[0] = article.getTags();
			 try {
				 client.prepareIndex().setIndex(PerfConstant.ELS_INDICES[0]).setType(PerfConstant.ELS_TYPES[0]).setId(article.getId())
				 .setSource(this.putJsonDocument(PerfConstant.ELS_INDICES[0],article)).execute().actionGet();
				 client.prepareIndex().setIndex(PerfConstant.ELS_INDICES[1]).setType(PerfConstant.ELS_TYPES[1]).setId(article.getId())
				 .setSource(putJsonDocument(PerfConstant.ELS_INDICES[1],article)).execute().actionGet();
				 client.prepareIndex().setIndex(PerfConstant.ELS_INDICES[2]).setType(PerfConstant.ELS_TYPES[2]).setId(article.getId())
				 .setSource(putJsonDocument(PerfConstant.ELS_INDICES[2],article)).execute().actionGet();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
	 
	 protected String getStringFieldValue(GetField field){
	        if(field !=null){
	            return String.valueOf(field.getValue());
	        }
	        return null;
	 }

    protected Date getDateFieldValueOrNull(GetField field){
        if(field !=null){
            final String dateString = String.valueOf(field.getValue());
            if(dateString !=null && !dateString.isEmpty()){
                return SearchDateUtils.getFormattedDate(dateString);
            }
        }
        return null;
    }

    protected boolean getBooleanFieldValueOrFalse(GetField field){
        if(field !=null){
            return Boolean.valueOf(String.valueOf(field.getValue()));
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    protected List<String> getListFieldValueOrNull(GetField field){
        if(field !=null){
            final List<String> list = new ArrayList<String>();
            for (final Object object : field.getValues())
            {
                if(object instanceof List)
                {
                    for (final String valueString : (List<String>)object)
                    {
                        list.add(String.valueOf(valueString));
                    }
                }
                else
                {
                    list.add(String.valueOf(object));
                }
            }
            return list;
        }
        return null;
  }
 
	
}
