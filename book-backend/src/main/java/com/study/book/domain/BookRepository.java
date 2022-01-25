package com.study.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;

//@Repository 를 적어야 스프링이 IoC에 빈으로 등록이 되지만
//JpaRepository를 extends하면 생략 가능함
//JpaRepository는 CRUD 함수를 들고있음
public interface BookRepository extends JpaRepository<Book,Long> {

}
