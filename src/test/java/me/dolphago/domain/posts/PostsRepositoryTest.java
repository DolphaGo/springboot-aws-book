package me.dolphago.domain.posts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach
    public void cleanUp() {
        postsRepository.deleteAll();
    }

    @DisplayName("게시글저장_불러오기")
    @Test
    public void post_save_test() throws Exception {
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder()
                                  .title(title)
                                  .content(content)
                                  .author("adamdoha@naver.com")
                                  .build());

        List<Posts> postsList = postsRepository.findAll();

        Posts posts = postsList.get(0);
        assertEquals(title, posts.getTitle());
        assertEquals(content, posts.getContent());
        assertEquals("adamdoha@naver.com", posts.getAuthor());
    }

    @DisplayName("BaseTimeEntity 등록 테스트")
    @Test
    public void auditing_test() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        postsRepository.save(Posts.builder()
                                  .title("title")
                                  .content("content")
                                  .author("dolphago")
                                  .build());

        List<Posts> all = postsRepository.findAll();

        Posts posts = all.get(0);

        System.out.println(posts.getCreatedDate() + "===========" + posts.getModifiedDate());

        Assertions.assertThat(posts.getCreatedDate()).isAfter(now);
        Assertions.assertThat(posts.getModifiedDate()).isAfter(now);
    }
}