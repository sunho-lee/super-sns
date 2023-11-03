package com.example.controllerexample.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = PostControllerWithRest.class)
@ComponentScan(basePackageClasses = PostMapper.class)
class PostControllerWithRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostMapper postMapper;


    @Test
    @DisplayName("id 1인 Post 1개 가져오기, 성공하여 200 반환")
    void getPostById() throws Exception {
        Post p = new Post(3, "title1", "desc22");
        Mockito.when(postService.getPost(3)).thenReturn(p);

        this.mockMvc.perform(get("/posts/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("title1"))
                .andExpect(jsonPath("$.desc").value("desc22"))
                .andDo(print());
    }

    @Test
    @DisplayName("id 4인 Post 1개 가져오기, 실패하여 404 반환")
    void testFailureGetPostById() throws Exception {
        Mockito.when(postService.getPost(4L)).thenThrow(new PostNotFoundException(4L));

        this.mockMvc.perform(get("/posts/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("Post 생성하기, 성공 시 201 반환")
    void createPost() throws Exception {
        PostRequest reqBody = new PostRequest("제목", "내용");
        String reqJson = objectMapper.writeValueAsString(reqBody);
        Post newPost = new Post(1, "제목", "내용");

        Mockito.when(postService.createPost(any())).thenReturn(newPost);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/posts/" + newPost.id)))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.desc").value("내용"))
                .andDo(print());
    }

    @Test
    @DisplayName("Post 수정하기, 성공 시 200 반환")
    void testReplacePostReturn200Ok() throws Exception {
        PostRequest postRequest = new PostRequest("title", "description");
        Post savedPost = new Post(3, "title", "description");
        Mockito.when(postService.replacePost(eq(3L), eq(postRequest)))
                .thenReturn(savedPost);

        String req = objectMapper.writeValueAsString(postRequest);

        mockMvc.perform(put("/posts/3").contentType(MediaType.APPLICATION_JSON).content(req))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.desc").value("description"))
                .andExpect(jsonPath("$._links.self.href").value(containsString("/posts/3")))
                .andExpect(jsonPath("$._links.posts.href").value(containsString("/posts")))
                .andDo(print());
    }

    @Test
    @DisplayName("Post 수정시 postId 없어서 실패 시 EntityNotFound 발생, 404반환")
    void testReplacePostThrowEntityNotFoundReturnNotFound() throws Exception {
        PostRequest postRequest = new PostRequest("title", "description");
        Mockito.when(postService.replacePost(any(), eq(postRequest)))
                .thenThrow(new PostNotFoundException(3L));

        String req = objectMapper.writeValueAsString(postRequest);

        mockMvc.perform(put("/posts/3").contentType(MediaType.APPLICATION_JSON).content(req))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andDo(print());
    }

    @Test
    @DisplayName("id에 맞는 Post 1건 삭제, 성공 204 반환")
    void testDeletePostByIdReturn200OK() throws Exception {
        Mockito.doNothing().when(postService).deletePost(3L);

        mockMvc.perform(delete("/posts/3"))
                .andExpect(status().isNoContent())
                .andDo(print());

        Mockito.verify(postService, times(1)).deletePost(3L);
    }

    @Test
    @DisplayName("id에 맞는 Post가 없어서 삭제 실패, 404 반환")
    void testDeletePostByIdReturn404NotFound() throws Exception {
        Long postId = 3L;
        Mockito.doThrow(new PostNotFoundException(postId))
                .when(postService).deletePost(postId);

        mockMvc.perform(delete("/posts/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.instance").isNotEmpty())
                .andDo(print());
    }


}