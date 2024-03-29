package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.Inquiry;
import com.nhnacademy.springmvc.exception.InquiryAlreadyExistsException;
import com.nhnacademy.springmvc.exception.InquiryNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InquiryRepositoryImpl implements InquiryRepository{
    private final Map<Long, Inquiry> inquiryMap = new HashMap<>();

    @Override
    public Map<Long, Inquiry> findAll() {
        return inquiryMap;
    }

    @Override
    public boolean exists(long id) {
        return inquiryMap.containsKey(id);
    }

    @Override
    public Inquiry getInquiry(long id) {
        if(!exists(id)){
            throw new InquiryNotFoundException();
        }

        return inquiryMap.get(id);
    }

    @Override
    public Inquiry registerInquiry(Inquiry inquiry) {
        if(Objects.isNull(inquiry)){
            throw new NullPointerException("Object is null");
        }

        if (exists(inquiry.getId())) {
            throw new InquiryAlreadyExistsException();
        }
        inquiryMap.put(inquiry.getId(), inquiry);

        return inquiryMap.get(inquiry.getId());
    }

    @Override
    public Inquiry modifyInquiry(Inquiry inquiry) {
        if(Objects.isNull(inquiry)){
            throw new NullPointerException("Object is null");
        }

        long id = inquiry.getId();

        if (!exists(id)) {
            throw new InquiryNotFoundException();
        }

        Inquiry older = inquiryMap.get(id);
        inquiryMap.replace(id, older, inquiry);

        return inquiryMap.get(id);
    }

    @Override
    public void removeInquiry(long id) {
        if (!exists(id)) {
            throw new InquiryNotFoundException();
        }

        inquiryMap.remove(id);
    }
}
