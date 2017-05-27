package com.perf.blog.constant;


public class PerfConstant {
    public static final String RESPONSE_SUCCESS = "SUCCESS";
    public static final String RESPONSE_FAILURE = "FAILURE";
	public static final String URL = "http://localhost:8080/perficientPatientService/";
	
	
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/database_name";
	public static final String INSERT_QUERY = "INSERT INTO tweets (tweet_id,user_name,tweet_text,created_at) values (?,?,?,?)";
	public static final String INSERT_QUERY_ARTICLE = "INSERT INTO article (title,content,postDate,tags,author) values (?,?,?,?,?)";
	public static final String SELECT_QUERY_ARTICLE = "SELECT * FROM article  ORDER BY id ASC LIMIT 15;";
	public static final String SELECT_ALL_QUERY_ARTICLE = "SELECT * FROM article;";
	public static final String indices[] = {"employee","role","address"};
	public static final String types[] = {"article","article","article"};
	
	//Elastic Serach
	public static final String ELS_DB_URL = "jdbc:mysql://localhost:3306/elasticsearch";
	public static final String ELS_INSERT_EMP_QUERY = "INSERT INTO employee (pk,fname,mname,lname,position) values (?,?,?,?,?)";
	public static final String ELS_INSERT_ADR_QUERY = "INSERT INTO address (id,place,country,pk) values (?,?,?,?)";
	public static final String ELS_INSERT_RL_QUERY = "INSERT INTO role (id,role,salary,pk) values (?,?,?,?)";
	public static final String ELS_SELECT_QUERY_EMP = "SELECT * FROM employee  ORDER BY pk ASC LIMIT 15;";
	public static final String ELS_SELECT_QUERY_ADR = "SELECT * FROM address  ORDER BY id ASC LIMIT 15;";
	public static final String ELS_SELECT_QUERY_RL = "SELECT * FROM role  ORDER BY id ASC LIMIT 15;";
	public static final String ELS_TYPE = "perf";
	public static final String ELS_INDICES_EMP = "emp";
	public static final String ELS_INDICES_ROL = "rol";
	public static final String ELS_INDICES_ADDR = "addr";
	public static final String ELS_INDICES [] = {"PerficientArticles","role","address"};
	public static final String ELS_TYPES[] = {"perf","perf","perf"};
	public static final String USERNAME = "root";
	public static final String PASSWORD = "admin";
	public static final String PERF_ARTICLE = "PerficientArticles";

}