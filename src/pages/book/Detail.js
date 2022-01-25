import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';

const Detail = (props) => {
  console.log('Detail', props);
  const id = props.match.params.id;

  const [book, setBook] = useState({
    id: '',
    title: '',
    author: '',
  });

  useEffect(() => {
    fetch('http://localhost:8080/book/' + id, {
      method: 'GET',
    })
      //두번쨰 then()으로 리턴
      .then((res) => res.json())
      .then((res) => {
        console.log(res);
        setBook(res);
      });
  }, []);

  const deleteBook = (id) => {
    console.log('deleteBook', id);

    fetch('http://localhost:8080/book/' + id, {
      method: 'DELETE',
    })
      .then((res) => {
        console.log(res);
        return res.json();
      })
      .then((res) => {
        console.log(res);
        if (res.message === 'ok') {
          alert('삭제에 성공해였습니다.');
          props.history.push('/');
        } else {
          alert('삭제실패');
        }
      });
  };

  const updateBook = () => {
    props.history.push('/updateForm/' + id);
  };

  return (
    <div>
      <h1>책 상세보기</h1>
      <Button variant="warning" onClick={updateBook}>
        수정
      </Button>{' '}
      <Button variant="danger" onClick={() => deleteBook(book.id)}>
        삭제
      </Button>
      <hr />
      <h1>{book.author}</h1>
      <h1>{book.title}</h1>
    </div>
  );
};

export default Detail;
