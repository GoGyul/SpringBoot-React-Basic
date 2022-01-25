import React, { useEffect, useState } from 'react';
import { Button, Form } from 'react-bootstrap';

const UpdateForm = (props) => {
  const id = props.match.params.id;

  //상태보관
  const [book, setBook] = useState({
    title: '',
    author: '',
  });

  useEffect(() => {
    fetch('http://localhost:8080/book/' + id)
      .then((res) => res.json())
      .then((res) => {
        setBook(res);
      });
  }, []);

  // 이벤트가 발생할때마다넘어옴
  const changeValue = (e) => {
    setBook({
      ...book,
      [e.target.name]: e.target.value,
    });
  };

  const updateBook = (e) => {
    e.preventDefault();
    fetch('http://localhost:8080/book/' + id, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(book),
    })
      .then((res) => {
        console.log(1, res);
        if (res.status === 200) {
          return res.json();
        } else {
          return null;
        }
      })
      .then((res) => {
        //Catch는 여기서 오류가 나야 실행됨.
        if (res !== null) {
          alert('수정에 성공했습니다.');
          props.history.push('/book/' + id);
        } else {
          alert('책 수정에 실패하였습니다.');
        }
      });
  };

  return (
    <Form onSubmit={updateBook}>
      <Form.Group controllId="formBasicId">
        <Form.Label>제목</Form.Label>
        <Form.Control
          type="text"
          placeholder="제목입력"
          onChange={changeValue}
          name="title"
          value={book.title}
        ></Form.Control>
      </Form.Group>

      <Form.Group controllId="formBasicId">
        <Form.Label>저자</Form.Label>
        <Form.Control
          type="text"
          placeholder="저자입력"
          onChange={changeValue}
          name="author"
          value={book.author}
        ></Form.Control>
      </Form.Group>

      <Button variant="primary" type="submit">
        저장
      </Button>
    </Form>
  );
};

export default UpdateForm;
