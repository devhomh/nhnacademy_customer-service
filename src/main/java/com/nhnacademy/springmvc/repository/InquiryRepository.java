package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.Inquiry;
import java.util.Map;

public interface InquiryRepository {
    Map<Long, Inquiry> findAll();
    boolean exists(Long id);
    Inquiry getInquiry(Long id);
    Inquiry registerInquiry(Inquiry inquiry);
    Inquiry modifyInquiry(Inquiry inquiry);
    void removeInquiry(Long id);
}
