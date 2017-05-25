package com.perf.blog.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.perf.blog.model.Article;

public class CompareLIst {

	
	private List<Article> articleList;
	
	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	public static void main(String[] args) {
		//articleList.clear();
		System.out.println("ooh");
	}

	public void compare(){
		List<Article> articleList = new ArrayList<Article>();
		articleList.clear();
		List<String> wordList = null;
		String[] words = {"ace", "boom", "crew", "dog", "eon"};  
		wordList =  Arrays.asList(words);
		for (int i = 0; i < 10; i++) {
			Article article = new Article();
			article.setId(String.valueOf(i));
			article.setTitle("Two States");
			article.setContent("It is all about love");
			article.setPostDate(String.valueOf(new Date()));
			article.setTags("Love");
			article.setAuthor(wordList.toString());
			articleList.add(article);
		}
		System.out.println(articleList);
		boolean relationCondition = false;
		int j = 0;
		int k = 5;
		int bacth = articleList.size()/5;
		for (int i = 0; i < bacth;i++) {
			for (int l = j; l < k-1; l++) {
				/*if((articleList.get(l).getAuthors().get(l).equalsIgnoreCase(articleList.get(l+1).getAuthors())) ){
					relationCondition = true;
				}*/
				if(relationCondition){
					buildObject(k-1,articleList);
					j = j+ 5;
					k = k + 5;
					break;
				}
			}
			
		}
		System.out.println(articleList);
	}
	
	private  void buildObject(int k, List<Article> articleList) {
		/*for (int i = j; i < k; i++) {
			articleList.get(i).setTitle("Half Girl Friend");
		}*/
		articleList.get(k).setTitle("Half Girl Friend");
	}
	
	
	
}
