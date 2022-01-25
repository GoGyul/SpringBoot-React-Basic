import React, { useState } from 'react';
import { Button, Form } from 'react-bootstrap';

const SaveForm = (props) => {
  //상태보관
  const [book, setBook] = useState({
    title: '',
    author: '',
  });

  // 이벤트가 발생할때마다넘어옴
  const changeValue = (e) => {
    setBook({
      ...book,
      [e.target.name]: e.target.value,
    });
  };

  const submitBook = (e) => {
    e.preventDefault();
    fetch('http://localhost:8080/book', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json; charset=utf-8',
      },
      body: JSON.stringify(book),
    })
      .then((res) => {
        console.log(1, res);
        if (res.status === 201) {
          return res.json();
        } else {
          return null;
        }
      })
      .then((res) => {
        //Catch는 여기서 오류가 나야 실행됨.
        if (res !== null) {
          props.history.push('/');
        } else {
          alert('책 등록에 실패하였습니다.');
        }
      });
  };

  return (
    <Form onSubmit={submitBook}>
      <Form.Group controllId="formBasicId">
        <Form.Label>제목</Form.Label>
        <Form.Control
          type="text"
          placeholder="제목입력"
          onChange={changeValue}
          name="title"
        ></Form.Control>
      </Form.Group>

      <Form.Group controllId="formBasicId">
        <Form.Label>저자</Form.Label>
        <Form.Control
          type="text"
          placeholder="저자입력"
          onChange={changeValue}
          name="author"
        ></Form.Control>
      </Form.Group>

      <Button variant="primary" type="submit">
        저장
      </Button>
    </Form>
  );
};

export default SaveForm;
