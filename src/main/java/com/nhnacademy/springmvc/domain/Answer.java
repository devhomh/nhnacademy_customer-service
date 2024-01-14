package com.nhnacademy.springmvc.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class Answer {
    private final long inquiryId;
    private final String userId;
    @Setter
    private String date;
    private String title;
    private String comment;
    private Map<String, MultipartFile> files;

    private Answer(long inquiryId, String userId) {
        this.inquiryId = inquiryId;
        this.date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.userId = userId;
    }

    public static Answer create(long inquiryId, String userId){
        return new Answer(inquiryId, userId);
    }

    public Answer setTitle(String title){
        this.title = title;
        return this;
    }

    public Answer setComment(String comment){
        this.comment = comment;
        return this;
    }

    public Answer uploadFiles(Map<String, MultipartFile> files){
        this.files = files;
        return this;
    }


}
