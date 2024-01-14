package com.nhnacademy.springmvc.domain;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Value
public class AnswerRequest {
    @NotBlank
    String title;

    @NotBlank
    @Length(max = 40000)
    String comment;

    @Nullable
    List<MultipartFile> files;
}
