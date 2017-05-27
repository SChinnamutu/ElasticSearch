package com.perf.blog.service.imp;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.perf.blog.constant.PerfConstant;
import com.perf.blog.dao.impl.ArticleDAOImpl;
import com.perf.blog.to.ResponseTO;
import com.perf.blog.util.ClientServiceUtil;
import com.perf.blog.util.CommanUtil;


@Component
public class ArticleServiceImpl {

	@Autowired
	private ArticleDAOImpl dAOImpl;
	
	@Autowired
	private ClientServiceUtil serviceUtil;
	
	public String getEntityUsingElasticSearch(String id, String name)  {
		QueryBuilder builder =  this.getQuery(id, name);
		ResponseTO responseTO = dAOImpl.getEntityUsingQueryStringBetweenMultipleIndices(serviceUtil.getClient(), PerfConstant.indices,PerfConstant.types,builder);
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_SUCCESS)){
			return CommanUtil.convertToJson(responseTO);
		}
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_FAILURE)){
			return CommanUtil.convertToJson(responseTO);
		}
		return null;
	}
	
	
	public String findByPrice(Double price){
		ResponseTO responseTO = dAOImpl.findByPrice(price);
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_SUCCESS)){
			return CommanUtil.convertToJson(responseTO);
		}
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_FAILURE)){
			return CommanUtil.convertToJson(responseTO);
		}
		return null;
	}
	
	public String findById(String id){
		ResponseTO responseTO = dAOImpl.findById(id);
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_SUCCESS)){
			return CommanUtil.convertToJson(responseTO);
		}
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_FAILURE)){
			return CommanUtil.convertToJson(responseTO);
		}
		return null;
	}
	
	public String findByMessage(String message){
		ResponseTO responseTO = dAOImpl.findByMessage(message);
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_SUCCESS)){
			return CommanUtil.convertToJson(responseTO);
		}
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_FAILURE)){
			return CommanUtil.convertToJson(responseTO);
		}
		return null;
	}
	
		
	public String findByName(String name){
		ResponseTO responseTO = dAOImpl.findByName(name);
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_SUCCESS)){
			return CommanUtil.convertToJson(responseTO);
		}
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_FAILURE)){
			return CommanUtil.convertToJson(responseTO);
		}
		return null;
	}

	
	public String findAllFromIndex(String indexName) {
		ResponseTO responseTO = dAOImpl.findAllFromIndex(indexName);
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_SUCCESS)){
			return CommanUtil.convertToJson(responseTO);
		}
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_FAILURE)){
			return CommanUtil.convertToJson(responseTO);
		}
		return null;
	}
	
	private QueryBuilder getQuery(String id, String name) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		QueryBuilder builder =  null;
		if(CommanUtil.isMandatory(id)){
			builder  = boolQueryBuilder.should(QueryBuilders.termQuery("id", id));
		}
		if(CommanUtil.isMandatory(name)){
			builder  = boolQueryBuilder.should(QueryBuilders.termQuery("name", name));
		}
		System.out.println("Query is  " + builder);
		return builder;
	}

	
	
	public QueryBuilder findByNameQuery(String name) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		QueryBuilder builder =  null;
		if(CommanUtil.isMandatory(name)){
			builder  = boolQueryBuilder.should(QueryBuilders.termQuery("name", name));
		}
		System.out.println("Query is  " + builder);
		return builder;
	}

	public QueryBuilder findByPriceQuery(Double price) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		QueryBuilder builder =  null;
		if(CommanUtil.isMandatory(price)){
			builder  = boolQueryBuilder.should(QueryBuilders.termQuery("price", price));
		}
		System.out.println("Query is  " + builder);
		return builder;
	}

	public QueryBuilder findAllQuery() {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		QueryBuilder builder = boolQueryBuilder.should(QueryBuilders.matchAllQuery());
		System.out.println("Query is  " + builder);
		return builder;
	}
	
}
