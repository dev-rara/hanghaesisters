package com.team6.hanghaesisters.repository;

import com.team6.hanghaesisters.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCategoryOrderByModifiedAtDesc(String category);
}
