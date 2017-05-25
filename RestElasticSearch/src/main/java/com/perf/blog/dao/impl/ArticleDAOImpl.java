package com.perf.blog.dao.impl;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.stereotype.Component;

import com.perf.blog.constant.PerfConstant;
import com.perf.blog.to.ResponseTO;

@Component
public class ArticleDAOImpl {

	public ResponseTO getEntityUsingQueryStringBetweenMultipleIndices(Client client, String [] indices,String [] types,QueryBuilder builder){
		SearchResponse response = null;
		SearchRequestBuilder searchRequestBuilder = null;
		ResponseTO responseTO = new ResponseTO();
		try {
				long startTime = System.currentTimeMillis();
				searchRequestBuilder = client.prepareSearch().setIndices("shah1_test").setTypes("shah1_type").setQuery(builder);
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

}
