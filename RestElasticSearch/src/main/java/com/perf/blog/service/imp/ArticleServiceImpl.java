package com.perf.blog.service.imp;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.json.simple.parser.ParseException;
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
	
	public String getEntityUsingElasticSearch(String id, String title,String content, String postDate, String tags, String author) throws ParseException {
		QueryBuilder builder =  this.getQuery(id, title, author, content, postDate);
		ResponseTO responseTO = dAOImpl.getEntityUsingQueryStringBetweenMultipleIndices(serviceUtil.getClient(), PerfConstant.indices,PerfConstant.types,builder);
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_SUCCESS)){
			return CommanUtil.convertToJson(responseTO);
		}
		if(responseTO != null && responseTO.getStatus().equalsIgnoreCase(PerfConstant.RESPONSE_FAILURE)){
			return CommanUtil.convertToJson(responseTO);
		}
		return null;
	}
	
	private QueryBuilder getQuery(String id, String title, String author,String content,String postDate) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		QueryBuilder builder =  null;
		if(CommanUtil.isMandatory(id)){
			builder  = boolQueryBuilder.should(QueryBuilders.termQuery("pk", id));
		}
		if(CommanUtil.isMandatory(title)){
			builder  = boolQueryBuilder.should(QueryBuilders.termQuery("title", title));
		}
		if(CommanUtil.isMandatory(author)){
			builder  = boolQueryBuilder.should(QueryBuilders.termQuery("author", author));
		}
		if(CommanUtil.isMandatory(content)){
			builder  = boolQueryBuilder.should(QueryBuilders.termQuery("content", content));
		}
		if(CommanUtil.isMandatory(postDate)){
			builder  = boolQueryBuilder.should(QueryBuilders.termQuery("postDate", postDate));
		}
		System.out.println("Query is  " + builder);
		return builder;
	}
	
	
}
