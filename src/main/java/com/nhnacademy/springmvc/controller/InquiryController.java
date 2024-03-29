package com.nhnacademy.springmvc.controller;


import com.nhnacademy.springmvc.domain.Answer;
import com.nhnacademy.springmvc.domain.AnswerRequest;
import com.nhnacademy.springmvc.domain.Inquiry;
import com.nhnacademy.springmvc.domain.InquiryRequest;
import com.nhnacademy.springmvc.domain.User;
import com.nhnacademy.springmvc.exception.EnumTypeNotFoundException;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.AnswerRepository;
import com.nhnacademy.springmvc.repository.InquiryRepository;
import com.nhnacademy.springmvc.repository.UserRepository;
import com.nhnacademy.springmvc.validator.FileExtensionValidator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/inquiry")
public class InquiryController {
    private final InquiryRepository inquiryRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final FileExtensionValidator validator;

    public InquiryController(InquiryRepository inquiryRepository,
                             AnswerRepository answerRepository,
                             UserRepository userRepository,
                             FileExtensionValidator fileExtensionValidator) {
        this.answerRepository = answerRepository;
        this.inquiryRepository = inquiryRepository;
        this.userRepository = userRepository;
        this.validator = fileExtensionValidator;
    }

    @GetMapping("/{inquiryId}")
    public String viewInquiry(@PathVariable("inquiryId") long inquiryId,
                             HttpServletRequest request,
                             ModelMap modelMap){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("login");
        String auth = user.getAuth() == User.Auth.ROLE_ADMIN ? "admin" : "customer";

        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);
        String type = inquiry.getType().value();

        if(Objects.nonNull(inquiry.getFiles())){
            List<String> fileNameList = new ArrayList<>(inquiry.getFiles().keySet());
            modelMap.addAttribute("inquiryFileNameList", fileNameList);
        }

        if(answerRepository.exists(inquiryId)){
            Answer answer = answerRepository.getAnswer(inquiryId);
            modelMap.addAttribute("answer", answer);

            String answerAdminId = answer.getUserId();
            User admin = userRepository.getUser(answerAdminId);
            modelMap.addAttribute("adminName", admin.getName());

            if(Objects.nonNull(answer.getFiles())){
                List<String> fileNameList = new ArrayList<>(answer.getFiles().keySet());
                modelMap.addAttribute("answerFileNameList", fileNameList);
            }
        }

        modelMap.addAttribute("auth", auth);
        modelMap.addAttribute("type", type);
        modelMap.addAttribute("inquiry", inquiry);


        return "thymeleaf/inquiry";
    }

    private List<String> getTypeEnumList(){
        return EnumSet.allOf(Inquiry.Type.class)
                .stream()
                .map(Inquiry.Type::value)
                .collect(Collectors.toList());
    }

    @GetMapping("/register")
    public String registerInquiryForm(ModelMap modelMap){
        modelMap.addAttribute("typeList", getTypeEnumList());

        return "thymeleaf/inquiryRegisterForm";
    }

    private Inquiry.Type getStringToTypeEnum(String value){
        for (Inquiry.Type type : Inquiry.Type.values()) {
            if(type.value().equals(value)){
                return type;
            }
        }

        throw new EnumTypeNotFoundException();
    }

    private String getDirectory(HttpServletRequest request){
        String relativeDirectory = "/uploads";

        String appRoot = request.getServletContext().getRealPath("/");

        String externalDirectory = appRoot + relativeDirectory;

        File directory = new File(externalDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return externalDirectory;
    }

    public String resolveDuplicateName(Map<String, MultipartFile> fileMap, String fileName){
        if(!fileMap.isEmpty() && fileMap.containsKey(fileName)){
            int extensionIndex = fileName.lastIndexOf(".");
            String baseName = fileName.substring(0, extensionIndex);
            String extension = fileName.substring(extensionIndex);
            return baseName + "_1" + extension;
        }

        return fileName;
    }

    private String getUserid(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("login");
        return user.getId();
    }

    private void uploadFiles(HttpServletRequest request, String userId, Inquiry inquiry, List<MultipartFile> files){
        if(Objects.nonNull(files)){
            Map<String, MultipartFile> fileMap = Objects.nonNull(inquiry.getFiles())
                    ? inquiry.getFiles()
                    : new HashMap<>();

            for (MultipartFile file : files) {
                if(!Objects.equals(file.getOriginalFilename(), "")){
                    String directory = getDirectory(request);
                    String fileName = userId + "_" + inquiry.getId() + "_" + file.getOriginalFilename();
                    fileName = resolveDuplicateName(fileMap, fileName);

                    Path filePath = Paths.get(directory, fileName);

                    try {
                        file.transferTo(filePath.toFile());
                    } catch (IOException exception){
                        log.error("File Upload Error");
                    }
                    fileMap.put(fileName, file);
                }
            }
            inquiry.uploadFiles(fileMap);
        }
    }

    @PostMapping("/register")
    public String registerInquiry(@Valid @ModelAttribute InquiryRequest inquiryRequest,
                                  BindingResult bindingResult,
                                  HttpServletRequest request){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }

        String userId = getUserid(request);

        Inquiry inquiry = Inquiry.create(userId)
                .setTitle(inquiryRequest.getTitle())
                .setType(getStringToTypeEnum(inquiryRequest.getType()))
                .setComment(inquiryRequest.getComment());

        List<MultipartFile> files = inquiryRequest.getFiles();
        uploadFiles(request, userId, inquiry, files);

        inquiryRepository.registerInquiry(inquiry);

        return "redirect:/inquiry/" + inquiry.getId();
    }

    @GetMapping("/{inquiryId}/modify")
    public String modifyInquiryForm(@PathVariable("inquiryId") long inquiryId,
                                    ModelMap modelMap){
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);
        String selectedType = inquiry.getType().value();

        if(Objects.nonNull(inquiry.getFiles())){
            List<String> fileNameList = new ArrayList<>(inquiry.getFiles().keySet());
            modelMap.addAttribute("fileNameList", fileNameList);
        }

        modelMap.addAttribute("typeList", getTypeEnumList());
        modelMap.addAttribute("selected", selectedType);
        modelMap.addAttribute("inquiry", inquiry);

        return "thymeleaf/inquiryRegisterForm";
    }

    @PostMapping("/{inquiryId}/modify")
    public String modifyInquiry(@PathVariable("inquiryId") long id,
                                @Valid @ModelAttribute InquiryRequest inquiryRequest,
                                BindingResult bindingResult,
                                HttpServletRequest request){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }

        String userId = getUserid(request);

        Inquiry inquiry = inquiryRepository.getInquiry(id);

        inquiry.setTitle(inquiryRequest.getTitle())
                .setComment(inquiryRequest.getComment())
                .setType(getStringToTypeEnum(inquiryRequest.getType()))
                .setDate(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        List<MultipartFile> files = inquiryRequest.getFiles();
        uploadFiles(request, userId, inquiry, files);

        inquiryRepository.modifyInquiry(inquiry);

        return "redirect:/inquiry/" + inquiry.getId();
    }

    @PostMapping("/{inquiryId}/remove")
    public String removeInquiry(@PathVariable("inquiryId") long id){
        inquiryRepository.removeInquiry(id);

        return "redirect:/";
    }

    @GetMapping("/{inquiryId}/answer")
    public String answerForm(@PathVariable("inquiryId") long id,
                             ModelMap modelMap){
        Inquiry inquiry = inquiryRepository.getInquiry(id);
        modelMap.addAttribute("inquiry", inquiry);

        return "thymeleaf/answerForm";
    }

    @PostMapping("/{inquiryId}/answer")
    public String answerInquiry(@PathVariable("inquiryId") long inquiryId,
                                HttpServletRequest request,
                                @Valid @ModelAttribute AnswerRequest answerRequest,
                                BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }

        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);
        String userId = getUserid(request);

        Answer answer = Answer.create(inquiryId, userId)
                .setTitle(answerRequest.getTitle())
                .setComment(answerRequest.getComment());

        List<MultipartFile> files = answerRequest.getFiles();

        if(Objects.nonNull(files)){
            Map<String, MultipartFile> fileMap = new HashMap<>();

            for (MultipartFile file : files) {
                if(!Objects.equals(file.getOriginalFilename(), "")){
                    String directory = getDirectory(request);
                    String fileName = userId + "_" + inquiry.getId() + "_" + file.getOriginalFilename();
                    fileName = resolveDuplicateName(fileMap, fileName);

                    Path filePath = Paths.get(directory, fileName);

                    try {
                        file.transferTo(filePath.toFile());
                    } catch (IOException exception){
                        log.error("File Upload Error");
                    }
                    fileMap.put(fileName, file);
                }
            }
            answer.uploadFiles(fileMap);
        }

        inquiry.answered();
        inquiryRepository.modifyInquiry(inquiry);
        answerRepository.registerAnswer(answer);

        return "redirect:/inquiry/" + inquiry.getId();
    }

    @InitBinder("inquiryRequest")
    protected void initBinder(WebDataBinder binder){
        binder.addValidators(validator);
    }
}
