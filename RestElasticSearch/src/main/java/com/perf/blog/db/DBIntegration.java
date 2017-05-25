package com.perf.blog.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.perf.blog.constant.PerfConstant;
import com.perf.blog.model.Article;

@Component
public class DBIntegration {

	 	private final static Logger LOGGER = Logger.getLogger(DBIntegration.class.getName());
	    
	 	private Connection conn = null;
	   
	    
	    DBIntegration() {
	        try {
	            Class.forName(PerfConstant.JDBC_DRIVER);
	            conn = DriverManager.getConnection(PerfConstant.DB_URL, PerfConstant.USERNAME, PerfConstant.PASSWORD);
	        } catch (Exception e) {
	            LOGGER.severe(e.getMessage());
	        }
	    }

	    public  List<Article> getArticles(){
	    	List<Article> articles = new ArrayList<Article>();
    	 	try {
    	 		PreparedStatement preparedStmt = conn.prepareStatement(PerfConstant.SELECT_ALL_QUERY_ARTICLE);
                ResultSet resultSet = preparedStmt.executeQuery(PerfConstant.SELECT_ALL_QUERY_ARTICLE);
                while(resultSet.next()){
                	Article article = new Article();
                	article.setId(String.valueOf(resultSet.getInt("id")));
                	article.setTitle(resultSet.getString("title"));
                	article.setContent(resultSet.getString("content"));
                	article.setPostDate(resultSet.getString("postDate"));
                	article.setTags(resultSet.getString("tags"));
                	article.setAuthor(resultSet.getString("author"));
                	articles.add(article);
                }
			} catch (Exception e) {
				System.out.println("Failure");
			}
    	 	return articles;
	    }
	    
}
