package com.study.book.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity // 서버 실행시에 Object Relation Mapping이 됨 (테이블이 생성되는 느낌)
public class Book {

	@Id // PK를 해당 변수로 하겠다는 뜻
	@GeneratedValue(strategy = GenerationType.IDENTITY)// 해당 데이터베이스 번호증가 전략을 따라가겠다
	private Long id;

	private  String title;
	private String author;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + "]";
	}
	
	
	
}
