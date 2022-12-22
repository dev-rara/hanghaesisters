package com.team6.hanghaesisters.service;

import com.team6.hanghaesisters.dto.CommentDto;
import com.team6.hanghaesisters.dto.HospitalDto;
import com.team6.hanghaesisters.dto.MsgResponseDto;
import com.team6.hanghaesisters.dto.PostDto;
import com.team6.hanghaesisters.entity.Comment;
import com.team6.hanghaesisters.entity.Post;
import com.team6.hanghaesisters.entity.User;
import com.team6.hanghaesisters.exception.CustomException;
import com.team6.hanghaesisters.exception.ErrorCode;
import com.team6.hanghaesisters.repository.CommentRepository;
import com.team6.hanghaesisters.repository.HospitalRepository;
import com.team6.hanghaesisters.repository.PostRepository;
import com.team6.hanghaesisters.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;

    public PostDto.CreateResponseDto create(PostDto.RequestDto postRequestDto, Long userId) {
        log.info("started to create post");

        User user = getUserByIdIfExists(userId);

        Post post = postRepository.saveAndFlush(new Post(postRequestDto, user.getUsername()));
        log.info("end to create post");

        return new PostDto.CreateResponseDto(post);
    }

    @Transactional(readOnly = true)
    public PostDto.AllResponseDto readOne(Long id, Long userId) {
        Post post = checkPost(id);

        User user = getUserByIdIfExists(userId);

        CommentDto.ResponseListDto commentList = getCommentList(id, user.getUsername());
        log.info("read post");

        return new PostDto.AllResponseDto (post, commentList);
    }

    public PostDto.AllResponseDto update(Long id, PostDto.RequestDto postRequestDto, Long userId) {
        Post post = checkPost(id);

        User user = getUserByIdIfExists(userId);
        checkOwner(post, user.getUsername());

        post.update(postRequestDto);

        CommentDto.ResponseListDto commentList = getCommentList(id, user.getUsername());
        log.info("update post");

        return new PostDto.AllResponseDto(post, commentList);
    }

    public MsgResponseDto delete(Long id, Long userId) {
        Post post = checkPost(id);

        User user = getUserByIdIfExists(userId);
        checkOwner(post, user.getUsername());

        deletePostAll(id, post);
        return new MsgResponseDto("게시글 삭제 성공!!", HttpStatus.OK.value());
    }

    @Transactional(readOnly = true)
    public List<PostDto.PreviewResponseDto> readByCategory(String category) {
        List<Post> postByCategory = postRepository.findByCategoryOrderByModifiedAtDesc(category);
        List<PostDto.PreviewResponseDto> previewList = new ArrayList<>();

        for (Post post : postByCategory) {
            PostDto.PreviewResponseDto dto = PostDto.PreviewResponseDto.builder()
                                                                .postId(post.getId())
                                                                .imageAfter(post.getImageAfter())
                                                                .title(post.getTitle())
                                                                .price(post.getPrice())
                                                                .build();
            previewList.add(dto);
        }
        log.info("read post by category");

        return previewList;
    }

    @Transactional(readOnly = true)
    public MsgResponseDto checkHospital(String hospitalName) {
        log.info(hospitalRepository.findByHospitalName(hospitalName).toString());
        if (hospitalRepository.findByHospitalName(hospitalName).size() == 0) {
            return new MsgResponseDto("존재하지 않는 병원입니다.", HttpStatus.BAD_REQUEST.value());
        }else{
            return new MsgResponseDto("존재하는 병원입니다.", HttpStatus.OK.value());
        }
    }

    //userId 로 유저정보 가져오기
    private User getUserByIdIfExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
    }

    //post 유무 확인
    private Post checkPost(Long id) {
        return postRepository.findById(id).orElseThrow(
            () -> new CustomException(ErrorCode.NOT_FOUND_POST)
        );
    }

    //게시글 작성자 확인
    private void checkOwner(Post post, String username) {
        if (!post.getUsername().equals(username)) {
            throw new CustomException(ErrorCode.UNAVAILABLE_MODIFICATION);
        }
    }

    //post 삭제(comment 삭제 -> post 삭제)
    private void deletePostAll(Long id, Post post) {
        List<Long> commentIds = commentRepository.findIdsByPostId(id);
        commentRepository.deleteByCommentId(commentIds);
        postRepository.delete(post);
    }

    //commentList 가져오기
    private CommentDto.ResponseListDto getCommentList(Long id, String username) {
        CommentDto.ResponseListDto commentList = new CommentDto.ResponseListDto(new ArrayList<>());
        List<Comment> comments = commentRepository.findAllByPostId(id);
        for (Comment comment: comments) {
            commentList.addComment(new CommentDto.ResponseDto(username, comment));
        }
        log.info("get commentList");

        return commentList;
    }

/*    private MsgResponseDto checkHospital(String hospitalName) {
        if (hospitalRepository.existsByHospitalName(hospitalName) == null) {
            return new MsgResponseDto("존재하지 않는 병원입니다.", HttpStatus.BAD_REQUEST.value());
        }else{
            return new MsgResponseDto("존재하는 병원입니다.", HttpStatus.OK.value());
        }
    }*/
}