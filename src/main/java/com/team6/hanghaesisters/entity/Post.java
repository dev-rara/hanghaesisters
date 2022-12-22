package com.team6.hanghaesisters.entity;

import com.team6.hanghaesisters.dto.PostDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Post extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String imageBefore;

    @Column(nullable = false)
    private String imageAfter;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String hospitalAddress;

    @Column(nullable = false)
    private String doctor;

    public Post(PostDto.RequestDto postRequestDto, String username) {
        this.username = username;
        this.title = postRequestDto.title();
        this.category = postRequestDto.category();
        this.imageBefore = postRequestDto.imageBefore();
        this.imageAfter = postRequestDto.imageAfter();
        this.content = postRequestDto.content();
        this.price = postRequestDto.price();
        this.hospitalAddress = postRequestDto.hospitalAddress();
        this.doctor = postRequestDto.doctor();
    }

    public void update(PostDto.RequestDto postRequestDto) {
        this.title = postRequestDto.title();
        this.category = postRequestDto.category();
        this.imageBefore = postRequestDto.imageBefore();
        this.imageAfter = postRequestDto.imageAfter();
        this.content = postRequestDto.content();
        this.price = postRequestDto.price();
        this.hospitalAddress = postRequestDto.hospitalAddress();
        this.doctor = postRequestDto.doctor();
    }
}
