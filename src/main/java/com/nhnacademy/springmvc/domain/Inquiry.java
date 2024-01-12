package com.nhnacademy.springmvc.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Inquiry {
    public enum Type{
        TYPE_COMPLAIN, TYPE_SUGGEST, TYPE_EXCHANGE_REFUND, TYPE_COMPLIMENT, TYPE_OTHER
    }
    private static final AtomicLong counter = new AtomicLong();
    private final Long id;
    private final String userId;
    @Setter
    private String date;
    private Type type;
    private String title;
    private String comment;

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

}