package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.Answer;
import java.util.Map;

public interface AnswerRepository {
    Map<Long, Answer> findAll();
    boolean exists(long id);
    Answer getAnswer(long id);
    Answer registerAnswer(Answer answer);
}
