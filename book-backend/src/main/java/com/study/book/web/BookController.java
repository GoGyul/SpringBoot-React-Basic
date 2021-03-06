package com.study.book.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.study.book.domain.Book;
import com.study.book.service.BookService;

@RestController
public class BookController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final BookService bookService;
	
	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}
	
	/*
	  sercurity(라이브러리 적용) - CORS 정책을 가지고있어 (시큐리티가 CORS를 해제)
	  BookController진입 직전
	 */
	@PostMapping("/book")
	@CrossOrigin
	public ResponseEntity<?> save(@RequestBody Book book){
		logger.info("BookController save input param ==== "+book.toString());
		return new ResponseEntity<>(bookService.저장하기(book),HttpStatus.CREATED);
	}
	
	// 리스폰스 엔티티 리턴할때 제너릭줘서 편하게하기
	@GetMapping("/book")
	@CrossOrigin
	public ResponseEntity<?> findByAll(){
		logger.info("BookController findByAll input");
		return new ResponseEntity<>(bookService.모두가져오기(),HttpStatus.OK);
	}
	
	@GetMapping("/book/{id}")
	@CrossOrigin
	public ResponseEntity<?> findId(@PathVariable Long id){
		logger.info("BookController findId input param ==== "+id);
		return new ResponseEntity<>(bookService.한건가져오기(id),HttpStatus.OK);
	}
	
	@PutMapping("/book/{id}")
	@CrossOrigin
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Book book){
		logger.info("BookController update input param ==== "+id +", bookDTO"+ book.toString());
		return new ResponseEntity<>(bookService.수정하기(id, book),HttpStatus.OK);
	}
	
	@DeleteMapping("/book/{id}")
	@CrossOrigin
	public ResponseEntity<?> deleteById(@PathVariable Long id) throws JsonProcessingException{
		logger.info("BookController deleteById input param ==== "+id);
		return new ResponseEntity<>(bookService.삭제하기(id),HttpStatus.OK);
	}
	
	
}
