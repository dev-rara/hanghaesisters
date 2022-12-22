package com.team6.hanghaesisters.controller;

import com.team6.hanghaesisters.dto.MsgResponseDto;
import com.team6.hanghaesisters.dto.PostDto;
import com.team6.hanghaesisters.service.PostService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
@Slf4j
public class PostController {
    private final PostService postService;

    //글 작성
    @PostMapping
    public PostDto.CreateResponseDto createPost(@Valid @RequestBody PostDto.RequestDto postRequestDto,
                                      @AuthenticationPrincipal UserDetails userDetails){
        return postService.create(postRequestDto, Long.parseLong(userDetails.getUsername()));
    }

    //글 조회
    @GetMapping("/{id}")
    public PostDto.AllResponseDto readOnePost(@PathVariable Long id,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        return postService.readOne(id, Long.parseLong(userDetails.getUsername()));
    }

    //글 수정
    @PutMapping("/{id}")
    public PostDto.AllResponseDto  updatePost(@PathVariable Long id, @Valid @RequestBody PostDto.RequestDto postRequestDto,
                                      @AuthenticationPrincipal UserDetails userDetails){
        return postService.update(id, postRequestDto, Long.parseLong(userDetails.getUsername()));
    }
    //글 삭제
    @DeleteMapping("/{id}")
    public MsgResponseDto deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return postService.delete(id, Long.parseLong(userDetails.getUsername()));
    }

    @GetMapping("/category")
    public List<PostDto.PreviewResponseDto> readPostByCategory(@RequestParam("category") String category) {
        return postService.readByCategory(category);
    }

    @GetMapping("/hospital")
    public MsgResponseDto checkHospital(@Valid @RequestParam("hospital-name") String hospitalName) {
        return postService.checkHospital(hospitalName);
    }
}





