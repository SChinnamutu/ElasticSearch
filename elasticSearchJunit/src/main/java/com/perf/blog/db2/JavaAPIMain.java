package com.perf.blog.db2;

import static org.elasticsearch.index.query.QueryBuilders.fieldQuery;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;

import com.perf.blog.constant.PerfConstant;
import com.perf.blog.model.Address;
import com.perf.blog.model.EmployeeDetail;
import com.perf.blog.model.Role;


public class JavaAPIMain {
    
	 private List<Address> addressAsLIst;
	 private List<EmployeeDetail> employeeDetailAsLIst;
	 private List<Role> roleAsLIst;
	 
	 public static void main(String[] args) {
		new JavaAPIMain().prepareData();
	 }
	
	public void prepareData(){
		 addressAsLIst  = new DBIntegration("root", "admin").getAddress();
		 roleAsLIst  = new DBIntegration("root", "admin").getRole();
		 employeeDetailAsLIst  = new DBIntegration("root", "admin").getEmployee();
		 doMechanism() ;
	}
	 
  	
	public void doMechanism() {
		 Node test     = nodeBuilder().node();
	     Client client = test.client();
	     //IndexRequestBuilder requestBuilderEmp =  client.prepareIndex("emp", "perf");
	     //IndexRequestBuilder requestBuilderAdr =  client.prepareIndex("addr", "perf");
	     //IndexRequestBuilder requestBuilderRol =  client.prepareIndex("rol", "perf");
	     //requestBuilderAdr.setId(String.valueOf(i)).setSource(JavaAPIMain.putEmpJsonDocument(employeeDetailAsLIst.get(i))).execute().actionGet();
    	 //requestBuilderRol.setId(String.valueOf(i)).setSource(JavaAPIMain.putRolJsonDocument(roleAsLIst.get(i))).execute().actionGet();
    	 //requestBuilderEmp.setId(String.valueOf(i)).setSource(JavaAPIMain.putAddrJsonDocument(addressAsLIst.get(i))).execute().actionGet();
    	 
	     for (int i = 0; i < 62; i++) {
	    	 client.prepareIndex("emp", "perf",String.valueOf(i)).setSource(JavaAPIMain.putEmpJsonDocument(employeeDetailAsLIst.get(i))).execute().actionGet();
	    	 client.prepareIndex("addr", "perf",String.valueOf(i)).setSource(JavaAPIMain.putRolJsonDocument(roleAsLIst.get(i))).execute().actionGet();
	    	 client.prepareIndex("rol", "perf",String.valueOf(i)).setSource(JavaAPIMain.putAddrJsonDocument(addressAsLIst.get(i))).execute().actionGet();
		 }
	     
	        
	     bulkIndex(client,PerfConstant.ELS_INDICES,PerfConstant.ELS_TYPES);
	     
	    //getDocumentFromMultipleIndex(client,PerfConstant.indices,PerfConstant.types,"10");
	    /*for (Article article : articles) {
	    	//getDocument(client, "kodcucom", "article", article.getId());
	    	getDocumentFromMultipleIndex(client,PerfConstant.indices,PerfConstant.types,"10");
		}*/ 
        /*updateDocument(client, "kodcucom", "article", "1", "title", "ElasticSearch: Java API");
        updateDocument(client, "kodcucom", "article", "1", "tags", new String[]{"bigdata"});

        getDocument(client, "kodcucom", "article", "1");

        searchDocument(client, "kodcucom", "article", "title", "ElasticSearch");
        
        deleteDocument(client, "kodcucom", "article", "1");
*/        
	     test.close();

		
	}
	
	 private static Map<String,Object> putAddrJsonDocument(Address address) {
	 	Map<String, Object> jsonDocument = new HashMap<String, Object>();
		jsonDocument.put("id", address.getId());
        jsonDocument.put("place", address.getPlace());
        jsonDocument.put("country", address.getCountry());
        jsonDocument.put("pk", address.getPk());
        return jsonDocument;
	}

	private static Map<String,Object> putRolJsonDocument(Role role) {
		Map<String, Object> jsonDocument = new HashMap<String, Object>();
		jsonDocument.put("id", role.getId());
        jsonDocument.put("role", role.getRole());
        jsonDocument.put("salary", role.getSalary());
        jsonDocument.put("pk", role.getPk());
        return jsonDocument;
	}

	private static Map<String,Object> putEmpJsonDocument(EmployeeDetail employeeDetail) {
	    Map<String, Object> jsonDocument = new HashMap<String, Object>();
        jsonDocument.put("id", employeeDetail.getId());
        jsonDocument.put("fname", employeeDetail.getFname());
        jsonDocument.put("mname", employeeDetail.getMname());
        jsonDocument.put("lname", employeeDetail.getLname());
        jsonDocument.put("position", employeeDetail.getPosition());
        return jsonDocument;
	}

	public static Map<String, Object> putJsonDocument(String title, String content, Date postDate,String[] tags, String author,String pk){
	        Map<String, Object> jsonDocument = new HashMap<String, Object>();
	        jsonDocument.put("title", title);
	        jsonDocument.put("content", content);
	        jsonDocument.put("postDate", postDate);
	        jsonDocument.put("tags", tags);
	        jsonDocument.put("author", author);
	        jsonDocument.put("pk", pk);
	        return jsonDocument;
	    }
	    
	    public static void getDocument(Client client, String index, String type, String id){
	        GetResponse getResponse = client.prepareGet(index, type, id)
	                                        .execute()
	                                        .actionGet();
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
	    
	    
	   
	    
	    public void bulkIndex(Client client, String [] indices, String [] types) {
	    	 SearchResponse response = client.prepareSearch(indices)
                     .setTypes(types)
                     .setSearchType(SearchType.QUERY_AND_FETCH)
                     .setQuery(fieldQuery("pk", "10"))
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
	    
	    public static void getDocumentFromMultipleIndex(Client client, String[] index, String [] type, String id){
	       BulkRequestBuilder bulkRequestBuilder = client.prepareBulk().setRefresh(true);
	       for (int i = 0; i < index.length; i++) {
	    	   bulkRequestBuilder.add(client.prepareIndex(index[i], type[i]));
	        }
	       BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
	        
	    	
	    /*	GetResponse getResponse = client.prepareGet(index, type, id)
	                                        .execute()
	                                        .actionGet();*/
	       for (BulkItemResponse bulkItemResponse : bulkResponse) {
	    	   System.out.println(bulkItemResponse);
		   }
	       
	       /*SearchHit[] results = bulkResponse.getItems();
	        
	        System.out.println("Current results: " + results.length);
	        for (SearchHit hit : results) {
	            System.out.println("------------------------------");
	            Map<String,Object> result = hit.getSource();   
	            System.out.println(result);
	        }
	        System.out.println("updateDocument() success");
	        
	        Map<String, Object> source = bulkResponse.getItems();
	        
	        System.out.println("------------------------------");
	        System.out.println("Index: " + getResponse.getIndex());
	        System.out.println("Type: " + getResponse.getType());
	        System.out.println("Id: " + getResponse.getId());
	        System.out.println("Version: " + getResponse.getVersion());
	        System.out.println(source);
	        System.out.println("------------------------------");
	        System.out.print("getDocument() Success");*/
	    }
	    
	    public static void updateDocument(Client client, String index, String type, 
	                                      String id, String field, String newValue){
	        
	        Map<String, Object> updateObject = new HashMap<String, Object>();
	        updateObject.put(field, newValue);
	        
	        client.prepareUpdate(index, type, id)
	              .setScript("ctx._source." + field + "=" + field)
	              .setScriptParams(updateObject).execute().actionGet();
	        System.out.print("updateDocument() Success");
	    }

	    public static void updateDocument(Client client, String index, String type,
	                                      String id, String field, String[] newValue){

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

	    public static void searchDocument(Client client, String index, String type,
	                                      String field, String value){
	        
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
	    
	    public static void deleteDocument(Client client, String index, String type, String id){
	        DeleteResponse response = client.prepareDelete(index, type, id).execute().actionGet();
	        System.out.println("Information on the deleted document:");
	        System.out.println("Index: " + response.getIndex());
	        System.out.println("Type: " + response.getType());
	        System.out.println("Id: " + response.getId());
	        System.out.println("Version: " + response.getVersion());
	        System.out.println("deleteDocument() success");
	    }


}
