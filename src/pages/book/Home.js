import React, { useEffect, useState } from 'react';
import BookItem from '../../components/BookItem';

const Home = () => {
  const [books, setBooks] = useState([]);

  //함수 실행시 최초 한번 실행되는것 + 상태값이 변경될때마다 실행
  useEffect(() => {
    //비동기 함수
    fetch('http://localhost:8080/book', {
      method: 'GET',
    })
      //프로미스 , 응답이 오면 json으로 바꿔준다
      .then((res) => res.json())
      .then((res) => {
        console.log(1, res);
        setBooks(res);
      });
    //의존성 해제
  }, []);

  return (
    <div>
      {books.map((book) => (
        <BookItem key={book.id} book={book}></BookItem>
      ))}
    </div>
  );
};

export default Home;
