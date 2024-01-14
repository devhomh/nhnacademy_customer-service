package com.nhnacademy.springmvc.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class Inquiry {
    public enum Type{
        TYPE_COMPLAIN("불만 접수"), TYPE_SUGGEST("제안"), TYPE_EXCHANGE_REFUND("교환/환불"), TYPE_COMPLIMENT("칭찬"), TYPE_OTHER("기타");

        private final String value;
        Type(String value){
            this.value = value;
        }
        public String value(){
            return value;
        }
    }
    private static final AtomicLong counter = new AtomicLong();
    private final long id;
    private final String userId;
    @Setter
    private String date;
    private Type type;
    private String title;
    private String comment;
    private Map<String, MultipartFile> files;
    private boolean answered;

    public static Inquiry create(String userId){
        return new Inquiry(userId);
    }

    private Inquiry(String userId){
        this.id = Inquiry.counter.incrementAndGet();
        this.date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.userId = userId;
    }

    public Inquiry setType(Type type){
        this.type = type;
        return this;
    }

    public Inquiry setTitle(String title){
        this.title = title;
        return this;
    }

    public Inquiry setComment(String comment){
        this.comment = comment;
        return this;
    }

    public Inquiry uploadFiles(Map<String, MultipartFile> files){
        this.files = files;
        return this;
    }

    public Inquiry answered(){
        this.answered = true;
        return this;
    }

}
