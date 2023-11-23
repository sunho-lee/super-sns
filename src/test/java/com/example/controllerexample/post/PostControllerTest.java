package com.example.controllerexample.post;

import com.example.controllerexample.config.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(PostController.class)
@ComponentScan(basePackageClasses = PostMapper.class)
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    @Autowired
    private PostMapper postMapper;

    private ValidatorFactory validatorFactory;
    private Validator validator;

    @BeforeEach
    void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @WithAnonymousUser
    @DisplayName("id 3인 Post 1개 가져오기, 성공하여 200 반환")
    void getPostById() throws Exception {
        Post p = new Post(3L, "desc22");
        PostResponse res = new PostResponse(3L, "desc22");
        Mockito.when(postService.getPost(eq(3L))).thenReturn(p);
        Mockito.when((postMapper.postToPostResponseDto(eq(p)))).thenReturn(res);

        this.mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/api/v1/posts/{postId}", 3L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.content").value("desc22"))
                .andDo(document("find-post",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("postId").description("조회할 게시물 id")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("게시물 id"),
                                        fieldWithPath("content").description("게시물 내용")
                                )
                        )
                )
                .andDo(print());

    }

    @Test
    @WithAnonymousUser
    @DisplayName("id 4인 Post 1개 가져오기, 실패하여 404 반환")
    void testFailureGetPostById() throws Exception {
        Mockito.when(postService.getPost(eq(4L))).thenThrow(new PostNotFoundException(4L));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/posts/{postId}", 4L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").isNotEmpty())
                .andDo(print());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("Post 목록 가져오기 with paging")
    void testSuccessGetPostList() throws Exception {
        PageRequest page = PageRequest.of(1, 10);
        List<Post> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(new Post(i, "content" + i));
        }
        Page<Post> posts = new PageImpl<>(list, page, list.size());

        Mockito.when(postService.getAllPostsWithPage(any())).thenReturn(posts);
        Mockito.when(postMapper.postToPostResponseDto(any(Post.class)))
                .thenAnswer(invocation -> {
                    Post post = invocation.getArgument(0);
                    return new PostResponse(post.getId(), post.getContent());
                });

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts?page=1&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[5].id").value(6))
                .andExpect(jsonPath("$.content[5].content").value("content6"))
                .andExpect(jsonPath("$.pageable").isNotEmpty())
                .andExpect(jsonPath("$.last").isBoolean())
                .andExpect(jsonPath("$.first").isBoolean())
                .andExpect(jsonPath("$.size").isNumber())
                .andExpect(jsonPath("$.numberOfElements").isNumber())
                .andExpect(jsonPath("$.totalPages").isNumber())
                .andExpect(jsonPath("$.totalElements").isNumber())
                .andExpect(jsonPath("$.sort").isNotEmpty())
                .andDo(print());

    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Post 생성하기, 성공 시 201 반환")
    void createPost() throws Exception {
        String reqJson = objectMapper.writeValueAsString(new PostRequest("content"));

        Mockito.when(postService.createPost(any(), any())).thenReturn(new Post(3L, "content"));
        Mockito.when(postMapper.postToPostResponseDto(any())).thenReturn(new PostResponse(3L, "content"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/posts/" + 3L)))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.content").value("content"))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Post 수정하기, 성공 시 200 반환")
    void testReplacePostReturn200Ok() throws Exception {
        PostRequest postRequest = new PostRequest("content");
        Post post = new Post("content");
        Post savedPost = new Post(3L, "content");

        Mockito.when(postMapper.postRequestDtoToPost(eq(postRequest))).thenReturn(post);
        Mockito.when(postService.replaceMyPost(eq(3L), eq(post), any())).thenReturn(savedPost);
        Mockito.when(postMapper.postToPostResponseDto(eq(savedPost))).thenReturn(new PostResponse(3L, "content"));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/posts/{postId}", 3L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.content").value("content"))
                .andDo(print());

    }

    @Test
    @WithMockCustomUser
    @DisplayName("Post 수정시 postId 없어서 실패 시 EntityNotFound 발생, 404반환")
    void testReplacePostThrowEntityNotFoundReturnNotFound() throws Exception {
        PostRequest postRequest = new PostRequest("description");
        Post post = new Post("description");

        Mockito.when(postMapper.postRequestDtoToPost(eq(postRequest)))
                .thenReturn(post);
        Mockito.when(postService.replaceMyPost(eq(3L), eq(post), any()))
                .thenThrow(new PostNotFoundException(3L));

        mockMvc.perform(MockMvcRequestBuilders.
                        put("/api/v1/posts/{postId}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Post 수정시 PostRequest desc가 비어있어 유효성 검증 실패, 400반환")
    void testReplacePostReturnBadRequest() throws Exception {
        PostRequest postRequest = new PostRequest("   ");

        mockMvc.perform(MockMvcRequestBuilders.
                        put("/api/v1/posts/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.errors.content")
                        .value("content cannot be blank"))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Post 수정시 PostRequest desc가 180자를 초과하여 유효성 검증 실패, 422반환")
    void testReplacePostPostReturn422() throws Exception {
        String s = "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij" +
                "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghi" +
                "abcdefghijabcdefghijabcdefghij212345678901";
        PostRequest postRequest = new PostRequest(s);
        Post post = new Post(s);

        Mockito.when(postMapper.postRequestDtoToPost(eq(postRequest))).thenReturn(post);
        Mockito.when(postService.replaceMyPost(eq(3L), eq(post), any()))
                .thenThrow(new ConstraintViolationException(validator.validate(post)));

        mockMvc.perform(MockMvcRequestBuilders.
                        put("/api/v1/posts/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.errors.content").value("content is should not be greater 1 and less than 180"))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("id에 맞는 Post 1건 삭제, 성공 204 반환")
    void testDeletePostByIdReturn200OK() throws Exception {
        Mockito.doNothing().when(postService).removeMyPostById(eq(3L), any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/3"))
                .andExpect(status().isNoContent())
                .andDo(print());

        Mockito.verify(postService, times(1)).removeMyPostById(eq(3L), any());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("id에 맞는 Post가 없어서 삭제 실패, 404 반환")
    void testDeletePostByIdReturn404NotFound() throws Exception {
        Mockito.doThrow(new PostNotFoundException(3L))
                .when(postService).removeMyPostById(eq(3L), any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/{postId}", 3L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").isNotEmpty())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.instance").isNotEmpty())
                .andDo(print());
    }


}