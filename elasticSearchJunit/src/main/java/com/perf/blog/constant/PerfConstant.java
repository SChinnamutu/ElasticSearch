package com.perf.blog.constant;

public class PerfConstant {

	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/database_name";
	public static final String INSERT_QUERY = "INSERT INTO tweets (tweet_id,user_name,tweet_text,created_at) values (?,?,?,?)";
	public static final String INSERT_QUERY_ARTICLE = "INSERT INTO article (title,content,postDate,tags,author) values (?,?,?,?,?)";
	public static final String SELECT_QUERY_ARTICLE = "select * from article";
}
