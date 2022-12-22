package com.team6.hanghaesisters.dto;

import com.team6.hanghaesisters.entity.Comment;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class CommentDto {

	public record RequestDto(@NotBlank(message = "내용을 입력해주세요.")
							 String content) {
	}

	public record ResponseDto(Long commentId, String username,  String content) {

		public ResponseDto(String username, Comment comment) {
			this(comment.getId(), username, comment.getContent());
		}
	}

	public record ResponseListDto(List<ResponseDto> commentList) {
		public void addComment(ResponseDto responseDto) {
			commentList.add(responseDto);
		}
	}
}
