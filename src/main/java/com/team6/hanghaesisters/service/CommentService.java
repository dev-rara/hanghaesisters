package com.team6.hanghaesisters.service;

import com.team6.hanghaesisters.dto.CommentDto;
import com.team6.hanghaesisters.dto.MsgResponseDto;
import com.team6.hanghaesisters.entity.Comment;
import com.team6.hanghaesisters.entity.User;
import com.team6.hanghaesisters.exception.CustomException;
import com.team6.hanghaesisters.exception.ErrorCode;
import com.team6.hanghaesisters.repository.CommentRepository;
import com.team6.hanghaesisters.repository.PostRepository;
import com.team6.hanghaesisters.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;

	public CommentDto.ResponseDto createComment(Long postId, CommentDto.RequestDto requestDto, Long userId) {
		log.info("started to create comment");
		//게시글 존재여부 확인
		checkPost(postId);

		Comment comment = Comment.builder()
								.content(requestDto.content())
								.postId(postId)
								.userId(userId)
								.build();
		commentRepository.save(comment);

		User user = getUserByIdIfExists(userId);
		log.info("end to create comment");

		return new CommentDto.ResponseDto(user.getUsername(), comment);
	}

	public CommentDto.ResponseDto updateComment(Long postId, Long commentId,
		CommentDto.RequestDto requestDto, Long userId) {

		//게시글 존재여부 확인
		checkPost(postId);

		//댓글 존재여부 확인 후 가져오기
		Comment comment = getCommentByIdIfExists(commentId);

		if (!comment.getPostId().equals(postId)) {
			throw new CustomException(ErrorCode.MISMATCH_COMMENT);
		}
		//댓글 작성자가 맞는지 확인
		checkOwner(comment, userId);
		comment.update(requestDto.content());

		User user = getUserByIdIfExists(userId);
		log.info("update comment");

		return new CommentDto.ResponseDto(user.getUsername(), comment);
	}

	public MsgResponseDto deleteComment(Long commentId, Long userId) {
		//댓글 존재여부 확인 후 가져오기
		Comment comment = getCommentByIdIfExists(commentId);

		//댓글 작성자가 맞는지 확인
		checkOwner(comment, userId);

		commentRepository.deleteById(commentId);

		return new MsgResponseDto("댓글 삭제 성공!", HttpStatus.OK.value());
	}

	private void checkPost(Long postId) {
		if (!postRepository.existsById(postId)) {
			throw new CustomException(ErrorCode.NOT_FOUND_POST);
		}
	}

	private void checkOwner(Comment comment, Long userId) {
		if (!comment.getUserId().equals(userId)) {
			throw new CustomException(ErrorCode.UNAVAILABLE_MODIFICATION);
		}
	}

	private User getUserByIdIfExists(Long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new CustomException(ErrorCode.USER_NOT_FOUND)
		);
	}

	private Comment getCommentByIdIfExists(Long commentId) {
		return commentRepository.findById(commentId).orElseThrow(
			() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT)
		);
	}
}