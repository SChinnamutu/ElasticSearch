package com.perf.blog.controller.rest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.perf.blog.model.Book;
import com.perf.blog.repository.BookRepository;
import com.perf.blog.service.imp.ArticleServiceImpl;

@RestController
public class BookRestController {
	@Resource
	private BookRepository bookRepository;
	
	@Autowired
	private Client client;

	private static final Logger logger = LoggerFactory
			.getLogger(BookRestController.class);

	@RequestMapping(value = "/rest/book1", method = RequestMethod.GET)
	public Page<Book> usePageable(@RequestParam(value = "query", required = false) String query,@PageableDefault(size = 10) Pageable pageable) {
		logger.info("/rest/book1 -> pageSize = {}, pageNumber = {}",pageable.getPageNumber(), pageable.getPageSize());
		if (StringUtils.isBlank(query)) {
			return bookRepository.findAll(pageable);
		}
		return bookRepository.findByMessage(query, pageable);
	}

	@RequestMapping(value = "/rest/book2", method = RequestMethod.GET)
	public Page<Book> useHttpServletRequest(
			@RequestParam(value = "query", required = false) String query,
			HttpServletRequest request) {
		
		int size = 10;
		int page = Integer.parseInt(request.getParameter("page"));

		logger.info("/rest/book2 -> pageNumber = {}", page);
		
		Pageable pageable = new PageRequest(page, size);

		if (StringUtils.isBlank(query)) {
			return bookRepository.findAll(pageable);
		}
		return bookRepository.findByMessage(query, pageable);
	}
	
	@RequestMapping(value = "/rest/book3", method = RequestMethod.GET)
	public Page<Book> useRequestParam(
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "page") int page, @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

		logger.info("/rest/book3 -> pageNumber = {}", page);
		
		Pageable pageable = new PageRequest(page, size);

		if (StringUtils.isBlank(query)) {
			return bookRepository.findAll(pageable);
		}
		return bookRepository.findByMessage(query, pageable);
	}

	@Autowired
	private ArticleServiceImpl articleServiceImpl;
	
	@RequestMapping(value = "/rest/getByName", method = RequestMethod.GET)
	public String getByName(HttpServletRequest request) {
		String name  =  request.getParameter("name");
		return articleServiceImpl.findByName(name);
	}
	
	

	@RequestMapping(value = "/rest/getById", method = RequestMethod.GET)
	public String getById(HttpServletRequest request) {
		String id  =  request.getParameter("id");
		System.out.println(client);
		return articleServiceImpl.findById(id);
	}
	
	@RequestMapping(value = "/rest/getByMessage", method = RequestMethod.GET)
	public String getByMessage(HttpServletRequest request) {
		String message  =  request.getParameter("message");
		return articleServiceImpl.findByMessage(message);
	}

	
	@RequestMapping(value = "/rest/getByPrice", method = RequestMethod.GET)
	public String getByPrice(HttpServletRequest request) {
		String priceAsStr = request.getParameter("price");
		Double price = Double.valueOf(priceAsStr);
		String response  = articleServiceImpl.findByPrice(price);
		return response;
	}
	
	@RequestMapping(value = "/rest/getDocumentsFromIndex", method = RequestMethod.GET)
	public String getDocumentsFromIndex(HttpServletRequest request) {
		String indexName = request.getParameter("indexName");
		return articleServiceImpl.findAllFromIndex(indexName);
	}
	
	
}
