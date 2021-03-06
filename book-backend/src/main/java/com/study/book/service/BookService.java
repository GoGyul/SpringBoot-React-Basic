package com.study.book.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.book.domain.Book;
import com.study.book.domain.BookRepository;

//	기능을 정의할수 있고, 트랙잭션을 관리할수있음

@Service
public class BookService {

	//함수 => 송금() -> 레파지토리에 여러개의 함수를 실행 -> commit or rollback
	
	private final BookRepository bookRepository;
	
	@Autowired
	public BookService (BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	@Transactional // 서비스 함수가 종료될때 commit할지 rollback 할지 트랜잭션 관리 하겠다.
	public Book 저장하기(Book book) {
		return bookRepository.save(book);
	}
	
	// JPA 변경감지라는 내부 기능 활성화 X, update시의 정합성을 유지해줌,
	// insert의 유령 데이터 현상(팬텀현상) 못막음
	@Transactional(readOnly = true) 
	public Book 한건가져오기(Long id) {
		return bookRepository.findById(id).orElseThrow(() ->
			new IllegalArgumentException("id 를 확인해 주세요")
		);
	}
	@Transactional(readOnly = true) 
	public List<Book> 모두가져오기(){
		return bookRepository.findAll();
	}
	
	@Transactional
	public Book 수정하기(Long id, Book book) {
		//더티체킹
		Book bookEntity = bookRepository.findById(id)
				//	영속화 (book 오브젝트) -> 영속성 컨텍스트 보관
				.orElseThrow(()-> new IllegalArgumentException("id 를 확인해 주세요"));
		bookEntity.setTitle(book.getTitle());
		bookEntity.setAuthor(book.getAuthor());
		return bookEntity;
		//함수종료 -> 트랜잭션 종료 -> 영속화 되어있는 데이터를 DB로 갱신(flush) -> commit -> 더티체킹
	}
	
	@Transactional
	public Object 삭제하기(Long id) throws JsonProcessingException {
		bookRepository.deleteById(id);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String,String> map = new HashMap<>();
		map.put("message", "ok");
		objectMapper.writeValueAsString(map);
		return map;
	}
	
}
