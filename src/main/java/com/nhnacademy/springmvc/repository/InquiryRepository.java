package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.Inquiry;
import java.util.Map;

public interface InquiryRepository {
    Map<Long, Inquiry> findAll();
    boolean exists(long id);
    Inquiry getInquiry(long id);
    Inquiry registerInquiry(Inquiry inquiry);
    Inquiry modifyInquiry(Inquiry inquiry);
    void removeInquiry(long id);
}
