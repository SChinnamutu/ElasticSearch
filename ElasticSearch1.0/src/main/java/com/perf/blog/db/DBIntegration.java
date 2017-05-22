package com.perf.blog.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.perf.blog.constant.PerfConstant;
import com.perf.blog.model.Article;

public class DBIntegration {

	 	private final static Logger LOGGER = Logger.getLogger(DBIntegration.class.getName());
	    private Connection conn = null;
	   
	    
	    DBIntegration(String username, String password) {
	        try {
	            Class.forName(PerfConstant.JDBC_DRIVER);
	            LOGGER.info("Connecting to database!");
	            conn = DriverManager.getConnection(PerfConstant.DB_URL, username, password);
	        } catch (Exception e) {
	            LOGGER.severe(e.getMessage());
	        }
	    }

	    public void insert(){
    	 	try {
    	 		for (int i = 0; i < 1000; i++) {
    	 			PreparedStatement preparedStmt = conn.prepareStatement(PerfConstant.INSERT_QUERY_ARTICLE, Statement.RETURN_GENERATED_KEYS);
                    preparedStmt.setString(1, "Isco");
                    preparedStmt.setString(2, "I will kill you buddy");
                    preparedStmt.setString(3,String.valueOf( new Date().getTime()));
                    preparedStmt.setString(4,"It does not matter who is just right ****");
                    preparedStmt.setString(5,"Neymar");
                    preparedStmt.execute();
				}
                System.out.println("Success");
			} catch (Exception e) {
				System.out.println("Failure");
			}
	    }
	    
	    public  List<Article> getArticles(){
	    	List<Article> articles = new ArrayList<Article>();
    	 	try {
    	 		PreparedStatement preparedStmt = conn.prepareStatement(PerfConstant.SELECT_QUERY_ARTICLE);
                ResultSet resultSet = preparedStmt.executeQuery(PerfConstant.SELECT_QUERY_ARTICLE);
                while(resultSet.next()){
                	Article article = new Article();
                	article.setId(String.valueOf(resultSet.getInt("id")));
                	article.setTitle(resultSet.getString("title"));
                	article.setContent(resultSet.getString("content"));
                	article.setPostDate(resultSet.getString("postDate"));
                	article.setTags(resultSet.getString("tags"));
                	article.setAuthor(resultSet.getString("author"));
                	articles.add(article);
                	if(articles!= null && articles.size() == 25){
                		break;
                	}
                }
                System.out.println("Success and article is "+articles.size());
			} catch (Exception e) {
				System.out.println("Failure");
			}
    	 	return articles;
	    }
	    
	    public static Timestamp getCurrentDate(){
	    	Calendar calendar = Calendar.getInstance();
	    	java.util.Date now = calendar.getTime();
	    	java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
	    	return currentTimestamp;
	    }
	    
	  
	   
}
