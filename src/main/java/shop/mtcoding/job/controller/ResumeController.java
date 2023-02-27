package shop.mtcoding.job.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.job.dto.ResponseDto;
import shop.mtcoding.job.dto.resume.ResumeReqDto.ResumeUpdateReqDto;
import shop.mtcoding.job.dto.resume.ResumeReqDto.SaveResumeReqDto;
import shop.mtcoding.job.handler.exception.CustomApiException;
import shop.mtcoding.job.model.resume.Resume;
import shop.mtcoding.job.model.resume.ResumeRepository;
import shop.mtcoding.job.model.user.User;
import shop.mtcoding.job.service.ResumeService;

@Controller
public class ResumeController {
    @Autowired
    private HttpSession session;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ResumeRepository resumeRepository;

    @GetMapping("/resumeList")
    public String resumeList(Model model) {
        List<Resume> resumeList = resumeRepository.findAll();
        model.addAttribute("resumeList", resumeList);
        return "resume/resumeList";
    }

    @GetMapping("/resumeForm")
    public String resumeForm() {
        return "resume/resumeForm";
    }

    @GetMapping("/resumeMain")
    public String resumeMain() {
        return "resume/main";
    }

    @PostMapping("/resume")
    public @ResponseBody ResponseEntity<?> save(@RequestBody SaveResumeReqDto saveResumeReqDto) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 실패했습니다", HttpStatus.UNAUTHORIZED);
        }

        if (saveResumeReqDto.getTitle() == null ||
                saveResumeReqDto.getTitle().isEmpty()) {
            throw new CustomApiException("제목을 작성해주세요");
        }

        if (saveResumeReqDto.getTitle().length() > 100) {
            throw new CustomApiException("제목의 길이는 100자 이하까지 가능합니다");
        }

        if (saveResumeReqDto.getBirthdate() == null ||
                saveResumeReqDto.getBirthdate().isEmpty()) {
            throw new CustomApiException("생년월일을 작성해주세요");
        }

        if (saveResumeReqDto.getAddress() == null ||
                saveResumeReqDto.getAddress().isEmpty()) {
            throw new CustomApiException("주소를 작성해주세요");
        }

        if (saveResumeReqDto.getContent() == null ||
                saveResumeReqDto.getContent().isEmpty()) {
            throw new CustomApiException("간단 소개글을 작성해주세요");
        }

        if (saveResumeReqDto.getCareer() == null ||
                saveResumeReqDto.getCareer().isEmpty()) {
            throw new CustomApiException("경력을 작성해주세요");
        }

        if (saveResumeReqDto.getEducation() == null ||
                saveResumeReqDto.getEducation().isEmpty()) {
            throw new CustomApiException("학력을 작성해주세요");
        }

        if (saveResumeReqDto.getSkill() == null ||
                saveResumeReqDto.getSkill().isEmpty()) {
            throw new CustomApiException("스킬을 작성해주세요");
        }

        if (saveResumeReqDto.getAward() == null ||
                saveResumeReqDto.getAward().isEmpty()) {
            throw new CustomApiException("수상내역을 작성해주세요");
        }

        if (saveResumeReqDto.getLanguage() == null ||
                saveResumeReqDto.getLanguage().isEmpty()) {
            throw new CustomApiException("외국어 능력을 작성해주세요");
        }

        if (saveResumeReqDto.getLink() == null ||
                saveResumeReqDto.getLink().isEmpty()) {
            throw new CustomApiException("링크를 작성해주세요");
        }

        resumeService.이력서쓰기(saveResumeReqDto, principal.getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "이력서 쓰기 성공", null), HttpStatus.CREATED);
    }

    @DeleteMapping("/resume/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 실패했습니다", HttpStatus.UNAUTHORIZED);
        }

        resumeService.이력서삭제(id, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "삭제 성공", null), HttpStatus.OK);

    }

    @GetMapping("/resume/updateForm")
    public String updateForm() {
        return "resume/updateForm";
    }

    @PutMapping("/resume/{id}")
    public @ResponseBody ResponseEntity<?> update(@PathVariable int id,
            @RequestBody ResumeUpdateReqDto resumeUpdateReqDto) throws Exception {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }

        resumeService.이력서수정(id, resumeUpdateReqDto, principal.getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "수정성공", null), HttpStatus.OK);
    }
}
