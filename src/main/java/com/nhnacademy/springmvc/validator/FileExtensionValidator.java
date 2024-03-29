package com.nhnacademy.springmvc.validator;

import com.nhnacademy.springmvc.domain.InquiryRequest;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class FileExtensionValidator implements Validator {
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("gif", "jpg", "jpeg", "png");

    @Override
    public boolean supports(Class<?> clazz) {
        return InquiryRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        InquiryRequest request = (InquiryRequest) target;

        List<MultipartFile> files = request.getFiles();

        if(Objects.isNull(files) || files.isEmpty()){
            return;
        }

        for (MultipartFile file :  files) {
            if(Objects.equals(file.getOriginalFilename(), "")){
                continue;
            }
            if(!isImageFile(file)){
                errors.rejectValue("files", "file.invalid", "Invalid file type. Only image files are allowed.");
                break;
            }
        }
    }

    private boolean isImageFile(MultipartFile file){
        String fileName = file.getOriginalFilename();
        log.info(fileName);
        if(Objects.nonNull(fileName)){
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            return ALLOWED_IMAGE_EXTENSIONS.contains(extension);
        }

        return false;
    }
}
