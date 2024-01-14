package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Inquiry;
import com.nhnacademy.springmvc.repository.InquiryRepository;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/uploads")
public class ImageFileController {
    private final InquiryRepository inquiryRepository;

    public ImageFileController(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    @ModelAttribute("mediaType")
    public MediaType getMediaType(@PathVariable("fileName") String fileName){
        if(fileName.contains(".jpg") || fileName.contains("jpg")){
            return MediaType.IMAGE_JPEG;
        } else if (fileName.contains(".png")) {
            return MediaType.IMAGE_PNG;
        } else{
            return MediaType.IMAGE_GIF;
        }
    }

    @ResponseBody
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getImageFile(@PathVariable("fileName") String fileName,
                                       @ModelAttribute("mediaType") MediaType mediaType,
                                       HttpServletRequest request) throws IOException {
        String root = request.getServletContext().getRealPath("/");
        String imgPath = root + "/uploads/" + fileName;

        Resource resource = new UrlResource(Paths.get(imgPath).toUri());

        if(resource.exists() && resource.isReadable()){
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{fileName}/remove")
    public String removeImageFile(@PathVariable("fileName") String fileName,
                                  @RequestParam("inquiryId") long inquiryId){
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);
        Map<String, MultipartFile> fileMap = inquiry.getFiles();
        fileMap.remove(fileName);

        inquiry.uploadFiles(fileMap);
        inquiryRepository.modifyInquiry(inquiry);

        return "redirect:/inquiry/" + inquiryId + "/modify";
    }
}
