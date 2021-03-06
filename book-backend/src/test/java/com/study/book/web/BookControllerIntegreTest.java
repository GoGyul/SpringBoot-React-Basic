package com.study.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.book.domain.Book;
import com.study.book.domain.BookRepository;

import lombok.extern.slf4j.Slf4j;

/*
 * 	통합 테스트 (모든 Bean들을 똑같이 Ioc에 올리고 테스트 하는것)
 *  WebEnvironment.MOCK = 실제 톰캣을 올리는게 아니라 다른 톰켓으로 테스트
 *  WebEnvironment.RANDOM_POR = 실제 톰켓으로 테스트
 *  @AutoConfigureMockMvc = MockMvc를 Ioc에 등록해줌.
 *  @Transactional = 각각의 테스트 함수가 종료될때 마다 트랜잭션을 rollback 해주는 어노테이션
 */


//통합 테스트에 필요한 어노테이션
@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) 
public class BookControllerIntegreTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager; 
	
	// 샐행되기 전에 increament 를 1로 초기화
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("ALTER TABLE book AUTO_INCREMENT = 1").executeUpdate();
	}
	
	@Test
	public void save_테스트() throws Exception {
		
		// given (테스트를 하기위한 준비.)
		// save 함수 호출할때 던져줘야 되는것이 json 데이터 이기 때문에 Book 객체를 json으로 미리 만들어둠
		Book book = new Book(null,"스프링따라하기","코스");
		String content = new ObjectMapper().writeValueAsString(book);
		log.info(content);
		// bookService.저장하기 메서드에서 미리 만들어놓은 book 객체를 집어넣으면 그 기대값을 미리 만들어 놓았다.
		// thenReturn() 에 new Book으로 똑같은값이 작성되어있는것을 확인할수있다.
		
		// when (테스트실행)
		ResultActions resultAction = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then (검증)
		resultAction
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.title").value("스프링따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findAll_테스트 () throws Exception {
		
		//given
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L,"스프링","자바"));
		books.add(new Book(2L,"스프링1","자바1"));
		books.add(new Book(3L,"스프링2","자바2"));
		bookRepository.saveAll(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$" , Matchers.hasSize(3)))
			.andExpect(jsonPath("$.[0].title").value("스프링"))
			.andDo(MockMvcResultHandlers.print());
			
	}
	
	@Test
	public void findById_테스트() throws Exception {
		
		Long id = 2L;
		
		//given
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L,"스프링","자바"));
		books.add(new Book(2L,"스프링1","자바1"));
		books.add(new Book(3L,"스프링2","자바2"));
		bookRepository.saveAll(books);

		//when
		ResultActions resultAction = mockMvc.perform(get("/book/{id}",id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("스프링1"))
			.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	public void update_테스트() throws Exception {
		
		//given
		Long id = 1L;
		
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L,"스프링","자바"));
		books.add(new Book(2L,"스프링1","자바1"));
		books.add(new Book(3L,"스프링2","자바2"));
		bookRepository.saveAll(books);
		
		Book book = new Book(null,"C++따라하기","코스");
		String content = new ObjectMapper().writeValueAsString(book);
		
		
		ResultActions resultAction = mockMvc.perform(put("/book/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				);
				
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("C++따라하기"))
			.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	public void delete_테스트() throws Exception {
		
		//given
		Long id = 1L;
		
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L,"스프링","자바"));
		books.add(new Book(2L,"스프링1","자바1"));
		books.add(new Book(3L,"스프링2","자바2"));
		bookRepository.saveAll(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(delete("/book/{id}",id)
				.accept(MediaType.TEXT_PLAIN)
				);
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
		
		//	문자를 응답할때는 이런식으로 받는다.
		MvcResult requestResult = resultAction.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		
		assertEquals("ok", result);
	}
	
}
