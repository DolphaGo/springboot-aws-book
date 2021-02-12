package me.dolphago.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import me.dolphago.domain.posts.Posts;
import me.dolphago.domain.posts.PostsRepository;
import me.dolphago.web.dto.PostsSaveRequestDto;
import me.dolphago.web.dto.PostsUpdateRequestDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @DisplayName("Posts_등록된다.")
    @Test
    public void posts_save() throws Exception {
        String title = "title";
        String content = "content";
        String author = "author";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                                                            .title(title)
                                                            .content(content)
                                                            .author(author)
                                                            .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody());

        List<Posts> all = postsRepository.findAll();
        assertEquals(title, all.get(0).getTitle());
        assertEquals(content, all.get(0).getContent());
        assertEquals(author, all.get(0).getAuthor());
    }

    @DisplayName("Posts_수정된다")
    @Test
    public void post_update() throws Exception {
        Posts savedPosts = postsRepository.save(Posts.builder()
                                                     .title("title")
                                                     .content("content")
                                                     .author("dolphago")
                                                     .build());

        Long updateId = savedPosts.getId();

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                                                                .title("title2")
                                                                .content("content2")
                                                                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L,responseEntity.getBody());

        List<Posts> all = postsRepository.findAll();
        assertEquals("title2",all.get(0).getTitle());
        assertEquals("content2",all.get(0).getContent());
    }
}