package com.team6.hanghaesisters.dto;

import com.team6.hanghaesisters.entity.Post;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Builder;

public class PostDto {

	public record RequestDto(@NotBlank(message = "제목이 입력되지 않았습니다.") String title,
							 @NotBlank(message = "카테고리가 입력되지 않았습니다.") String category,
							 @NotBlank(message = "성형 전 이미지가 필요합니다.") String imageBefore,
							 @NotBlank(message = "성형 후 이미지가 필요합니다.") String imageAfter,
							 @NotBlank(message = "본문 내용이 입력되지 않았습니다.") String content,
							 @NotNull(message = "금액이 입력되지 않았습니다.") Integer price,
							 @NotBlank(message = "병원 주소가 입력되지 않았습니다.") String hospitalAddress,
							 @NotBlank(message = "의사 정보가 입력되지 않았습니다.") String doctor) {
	}

	public record CreateResponseDto(Long postId, String username, String title, String category,
							  String imageBefore, String imageAfter, String content,
									Integer price, String hospitalAddress, String doctor) {

		public CreateResponseDto(Post post) {
			this(post.getId(), post.getUsername(), post.getTitle(), post.getCategory(), post.getImageAfter(),
				post.getImageBefore(), post.getContent(), post.getPrice(), post.getHospitalAddress(), post.getDoctor());
		}

	}

	public record AllResponseDto(Long postId, String username, String title, String category,
									String imageBefore, String imageAfter, String content,
								 Integer price, String hospitalAddress, String doctor,
								 List<CommentDto.ResponseDto> commentList) {

		public AllResponseDto(Post post, CommentDto.ResponseListDto commentList) {
			this(post.getId(), post.getUsername(), post.getTitle(), post.getCategory(), post.getImageAfter(),
				post.getImageBefore(), post.getContent(), post.getPrice(), post.getHospitalAddress(), post.getDoctor(),
				commentList.commentList());
		}
	}

	@Builder
	public record PreviewResponseDto(Long postId, String imageAfter, String title, Integer price) {

	}
}
