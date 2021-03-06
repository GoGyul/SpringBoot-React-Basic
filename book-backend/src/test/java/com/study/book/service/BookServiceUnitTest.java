package com.study.book.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.study.book.domain.BookRepository;

/**
 *  단위테스트 (Service와 관련된 애들만 메모리에 띄우면됨.)
 *  bookRepository -> 가짜 객체로 만들수 있음.
 *  기본적으로 MockitoExtension 이라고 어노테이션을 붙이면
 *  Mockito 환경에서 테스트가 실행되기 때문에
 *  BookService를 테스트 할때 bookRepository는 Mockito 환경에선
 *  Ioc가 되어있지 않기떄문 null 객체이다.
 *  
 *  따라서 @injectMocks 어노테이션을 붙여주면
 *  해당 파일에 @Mock 으로 등록된 모든 애들을 주입받게끔 해준다.
 * 	bookService 객체가 만들어 질때,
 * 	BookServiceUnitTest 파일에 @Mock으로 등록된 모든 애들을 주입받는다.
 */


@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

	@InjectMocks 
	private BookService bookService;
	
	@Mock
	private BookRepository bookRepository;
	
}
