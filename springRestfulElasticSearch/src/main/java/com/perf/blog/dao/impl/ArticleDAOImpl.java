package com.perf.blog.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import com.perf.blog.constant.PerfConstant;
import com.perf.blog.model.Book;
import com.perf.blog.to.ResponseTO;
import com.perf.blog.util.CommanUtil;

@Component
public class ArticleDAOImpl {

	
	@Autowired
	private Client client;
	
	@Resource
	private ElasticsearchTemplate elasticsearchTemplate;
	
	public ResponseTO getEntityUsingQueryStringBetweenMultipleIndices(Client client, String [] indices,String [] types,QueryBuilder builder){
		SearchResponse response = null;
		SearchRequestBuilder searchRequestBuilder = null;
		ResponseTO responseTO = new ResponseTO();
		try {
				long startTime = System.currentTimeMillis();
				searchRequestBuilder = client.prepareSearch().setIndices("book").setTypes("library").setQuery(builder);
			    response = searchRequestBuilder.execute().actionGet();
			    long endTime   = System.currentTimeMillis();
			    responseTO.setProcesstime(endTime-startTime);
			    responseTO.setStatus(PerfConstant.RESPONSE_SUCCESS);
			    responseTO.setResponseContent(response.toString());
			} catch (Exception e) {
				responseTO.setStatus(PerfConstant.RESPONSE_FAILURE);
			}
		return 	responseTO;
	}

	
	public ResponseTO findById(String id){
		ResponseTO responseTO = new ResponseTO();
		try {
				//SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.termQuery("id", id)).build();
				SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(this.getIdQuery(id)).build();
				long startTime = System.currentTimeMillis();
				List<Book> list = elasticsearchTemplate.queryForList(searchQuery,Book.class);
			    long endTime   = System.currentTimeMillis();
			    responseTO.setProcesstime(endTime-startTime);
			    responseTO.setStatus(PerfConstant.RESPONSE_SUCCESS);
			    responseTO.setResponseContent(CommanUtil.convertToJson(list));
			} catch (Exception e) {
				responseTO.setStatus(PerfConstant.RESPONSE_FAILURE);
			}
		return 	responseTO;
	}
	
	public ResponseTO findByMessage(String message){
		ResponseTO responseTO = new ResponseTO();
		try {
				SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(this.getMessageQuery(message)).build();
				long startTime = System.currentTimeMillis();
				List<Book> list = elasticsearchTemplate.queryForList(searchQuery,Book.class);
			    long endTime   = System.currentTimeMillis();
			    responseTO.setProcesstime(endTime-startTime);
			    responseTO.setStatus(PerfConstant.RESPONSE_SUCCESS);
			    responseTO.setResponseContent(CommanUtil.convertToJson(list));
			} catch (Exception e) {
				responseTO.setStatus(PerfConstant.RESPONSE_FAILURE);
			}
		return 	responseTO;
	}
	
	public ResponseTO findByName(String name){
		ResponseTO responseTO = new ResponseTO();
		try {
				SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.termQuery("name", name)).build();
				long startTime = System.currentTimeMillis();
				List<Book> list = elasticsearchTemplate.queryForList(searchQuery,Book.class);
			    long endTime   = System.currentTimeMillis();
			    responseTO.setProcesstime(endTime-startTime);
			    responseTO.setStatus(PerfConstant.RESPONSE_SUCCESS);
			    responseTO.setResponseContent(CommanUtil.convertToJson(list));
			} catch (Exception e) {
				responseTO.setStatus(PerfConstant.RESPONSE_FAILURE);
			}
		return 	responseTO;
	}
	
	public ResponseTO findByPrice(Double price){
		ResponseTO responseTO = new ResponseTO();
		try {
				SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.termQuery("price", price)).build();
				long startTime = System.currentTimeMillis();
				List<Book> list = elasticsearchTemplate.queryForList(searchQuery,Book.class);
			    long endTime   = System.currentTimeMillis();
			    responseTO.setProcesstime(endTime-startTime);
			    responseTO.setStatus(PerfConstant.RESPONSE_SUCCESS);
			    responseTO.setResponseContent(CommanUtil.convertToJson(list));
			    getEntityByMustMatchValuesBetweenMultipleIndices();
			    getEntityByMustMatchValuesBetweenMultipleIndicesWithFK();
			} catch (Exception e) {
				responseTO.setStatus(PerfConstant.RESPONSE_FAILURE);
			}
		return 	responseTO;
		
	}
	
	public ResponseTO findAllFromIndex(String indexName) {
		ResponseTO responseTO = new ResponseTO();
		try {
				SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(QueryBuilders.matchAllQuery()).build();
				long startTime = System.currentTimeMillis();
				List<Book> list = elasticsearchTemplate.queryForList(searchQuery,Book.class);
			    long endTime   = System.currentTimeMillis();
			    responseTO.setProcesstime(endTime-startTime);
			    responseTO.setStatus(PerfConstant.RESPONSE_SUCCESS);
			    responseTO.setResponseContent(CommanUtil.convertToJson(list));
			} catch (Exception e) {
				responseTO.setStatus(PerfConstant.RESPONSE_FAILURE);
			}
		return 	responseTO;
	}
	
	
	//Success from MultiIndex
	public void getEntityByMustMatchValuesBetweenMultipleIndices(){
		try {
			SearchRequest request = Requests.searchRequest("book","role","address")
											.source(SearchSourceBuilder.searchSource().query(this.getQuery()));
			SearchResponse response =  client.search(request).actionGet();
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getEntityByMustMatchValuesBetweenMultipleIndicesWithFK(){
		try {
			SearchRequest request = Requests.searchRequest("book","role","address")
											.source(SearchSourceBuilder.searchSource().query(this.getQueryWithDifferentIds()));
			SearchResponse response =  client.search(request).actionGet();
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public QueryBuilder getIdQuery(String id){
		//QueryBuilder builders = QueryBuilders.indicesQuery(QueryBuilders.termQuery("id", 10),"book","book2","book3");
		QueryBuilder builders = QueryBuilders.indicesQuery(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("id", id))
																					.should(QueryBuilders.termQuery("id", id))
																					.should(QueryBuilders.termQuery("id", id))
																					,"book","role","address");		
		return builders;
	}
	
	public QueryBuilder getQuery(){
		//QueryBuilder builders = QueryBuilders.indicesQuery(QueryBuilders.termQuery("id", 10),"book","book2","book3");
		/*QueryBuilder builders = QueryBuilders.indicesQuery(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("id", 105))
																					.should(QueryBuilders.termQuery("id", 105))
																					.should(QueryBuilders.termQuery("id", 105))
																					,"book","role","address");*/
		QueryBuilder builders = QueryBuilders.boolQuery().should(QueryBuilders.termQuery("id", 105));
				//.should(QueryBuilders.termQuery("id", 105))
				//.should(QueryBuilders.termQuery("id", 105));
		return builders;
	}
	
	public QueryBuilder getQueryWithDifferentIds(){
		QueryBuilder builders = QueryBuilders.boolQuery().should(QueryBuilders.termQuery("id", 105))
														 .should(QueryBuilders.termQuery("fk", 105))
														 .should(QueryBuilders.termQuery("fk", 105));
		return builders;
	}
	
	public QueryBuilder getMessageQuery(String message){
		QueryBuilder builder = QueryBuilders.indicesQuery(QueryBuilders.termQuery("message", message),"book","role","address");
		QueryBuilder builders = QueryBuilders.indicesQuery(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("message", message))
																					.should(QueryBuilders.termQuery("message", message))
																					.should(QueryBuilders.termQuery("message", message))
																				,"book","role","address");	
		System.out.println("Query is " + builders);	
		return builder;
	}
	
	
	/*QueryBuilder builder = nestedQuery("car", boolQuery().must(termQuery("car.name",      "subaru")).must(termQuery("car.model", "imprezza")));
    SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(builder).build();
    List persons = elasticsearchTemplate.queryForList(searchQuery, Person.class);*/
	
	

}
