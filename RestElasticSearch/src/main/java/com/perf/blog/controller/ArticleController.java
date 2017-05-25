package com.perf.blog.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perf.blog.service.imp.ArticleServiceImpl;
import com.perf.blog.util.CommanUtil;

@RestController
public class ArticleController {

	@Autowired
	private ArticleServiceImpl articleServiceImpl;

    @RequestMapping(value={"/getArticle.do"})
    public String validateLoginDetails(HttpServletRequest request) throws ParseException {
		String response = null;
        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String postDate = request.getParameter("postDate");
        String tags = request.getParameter("tags");
        String author = request.getParameter("author");
        try {
        	response = articleServiceImpl.getEntityUsingElasticSearch(id,title,content,postDate,tags,author);
        }catch (Exception e) {
        	 return CommanUtil.convertToJson(e.getMessage());
        }
        return response;
    }
}


//String parameters = "?uname="+uname+"&pswd="+pswd;