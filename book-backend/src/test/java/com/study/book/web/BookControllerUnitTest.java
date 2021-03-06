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

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.book.domain.Book;
import com.study.book.service.BookService;

import lombok.extern.slf4j.Slf4j;

// 단위 테스트 (Controller 관련 로직만 띄우기)
// filter, ControllerAdvice

// 단위 테스트에 필요한 어노테이션
@WebMvcTest
@Slf4j
public class BookControllerUnitTest {

	// 주소 호출을 해서 테스트를 도와줌
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean // Ioc 환경에 등록됨. 가짜 서비스를 올린것. Mock 환경에.
	private BookService bookService;
	
	// BDDMockito 패턴 given, when, then
	@Test
	public void save_테스트() throws Exception {
		
		// given (테스트를 하기위한 준비.)
		// save 함수 호출할때 던져줘야 되는것이 json 데이터 이기 때문에 Book 객체를 json으로 미리 만들어둠
		Book book = new Book(null,"스프링따라하기","코스");
		String content = new ObjectMapper().writeValueAsString(book);
		log.info(content);
		// bookService.저장하기 메서드에서 미리 만들어놓은 book 객체를 집어넣으면 그 기대값을 미리 만들어 놓았다.
		// thenReturn() 에 new Book으로 똑같은값이 작성되어있는것을 확인할수있다.
		when(bookService.저장하기(book)).thenReturn(new Book(1L,"스프링따라하기","코스")); // null
		
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
		
		when(bookService.모두가져오기()).thenReturn(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$" , Matchers.hasSize(2)))
			.andExpect(jsonPath("$.[0].title").value("스프링"))
			.andDo(MockMvcResultHandlers.print());
			
	}
	
	@Test
	public void findById_테스트() throws Exception {
		
		//given
		Long id = 1L;
		when(bookService.한건가져오기(id)).thenReturn(new Book(1L,"자바공부하기","쌀"));
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book/{id}",id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("자바공부하기"))
			.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	public void update_테스트() throws Exception {
		
		//given
		Long id = 1L;
		Book book = new Book(null,"C++따라하기","코스");
		String content = new ObjectMapper().writeValueAsString(book);
		when(bookService.수정하기(id,book)).thenReturn(new Book(1L,"C++따라하기","코스"));
		
		//when
		ResultActions resultAction = mockMvc.perform(put("/book/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				);
			
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("C++따라하기"))
			.andDo(MockMvcResultHandlers.print());
		
	}
	
	@Test
	public void delete_테스트() throws Exception {
		
		//given
		Long id = 1L;
		
		when(bookService.삭제하기(id)).thenReturn("ok");
		
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
