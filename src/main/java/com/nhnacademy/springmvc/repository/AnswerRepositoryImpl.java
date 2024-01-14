package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.Answer;
import com.nhnacademy.springmvc.exception.AnswerAlreadyExistsException;
import com.nhnacademy.springmvc.exception.AnswerNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class AnswerRepositoryImpl implements AnswerRepository{
    private final Map<Long, Answer> answerMap = new HashMap<>();

    @Override
    public Map<Long, Answer> findAll() {
        return answerMap;
    }

    @Override
    public boolean exists(long id) {
        return answerMap.containsKey(id);
    }

    @Override
    public Answer getAnswer(long id) {
        if(!exists(id)){
            throw new AnswerNotFoundException();
        }

        return answerMap.get(id);
    }

    @Override
    public Answer registerAnswer(Answer answer) {
        long inquiryId = answer.getInquiryId();

        if(exists(inquiryId)){
            throw new AnswerAlreadyExistsException();
        }
        answerMap.put(inquiryId, answer);
        return answerMap.get(inquiryId);
    }
}
