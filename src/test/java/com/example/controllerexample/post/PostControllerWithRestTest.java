package com.example.controllerexample.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = PostControllerWithRest.class)
@ComponentScan(basePackageClasses = PostMapper.class)
class PostControllerWithRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    @Autowired
    private PostMapper postMapper;

    @MockBean
    @Autowired
    private PostModelAssembler postAssembler;

    private ValidatorFactory validatorFactory;
    private Validator validator;

    @BeforeEach
    void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("id 3인 Post 1개 가져오기, 성공하여 200 반환")
    void getPostById() throws Exception {
        Post p = new Post(3L, "title1", "desc22");

        EntityModel<PostResponse> res = EntityModel.of(new PostResponse(3L, "title1", "desc22"),
                linkTo(methodOn(PostControllerWithRest.class).getPost(3L)).withSelfRel(),
                linkTo(PostControllerWithRest.class).withRel("posts"));

        Mockito.when(postService.getPost(eq(3L))).thenReturn(p);
        Mockito.when(postAssembler.toModel(eq(p))).thenReturn(res);

        this.mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/posts/{postId}", 3L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("title1"))
                .andExpect(jsonPath("$.desc").value("desc22"))
                .andDo(document("find-post",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("postId").description("조회할 게시물 id")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("게시물 id"),
                                        fieldWithPath("title").description("게시물 제목"),
                                        fieldWithPath("desc").description("게시물 내용"),
                                        fieldWithPath("_links.self.href").description("self 링크"),
                                        fieldWithPath("_links.posts.href").description("목록 링크")

                                )
                        )
                )
                .andDo(print());

    }

    @Test
    @DisplayName("id 4인 Post 1개 가져오기, 실패하여 404 반환")
    void testFailureGetPostById() throws Exception {
        Mockito.when(postService.getPost(4L)).thenThrow(new PostNotFoundException(4L));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/posts/4").accept(MediaType.APPLICATION_JSON))
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

        EntityModel<PostResponse> res = EntityModel.of(new PostResponse(1L, "제목", "내용"),
                linkTo(methodOn(PostControllerWithRest.class).getPost(1L)).withSelfRel(),
                linkTo(PostControllerWithRest.class).withRel("posts"));

        Mockito.when(postService.createPost(any())).thenReturn(newPost);
        Mockito.when(postAssembler.toModel(eq(newPost))).thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
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
        Post post = new Post("title", "description");
        Post savedPost = new Post(3L, "title", "description");
        EntityModel<PostResponse> res = EntityModel.of(new PostResponse(3L, "title", "description"),
                linkTo(methodOn(PostControllerWithRest.class).getPost(3L)).withSelfRel(),
                linkTo(PostControllerWithRest.class).withRel("posts"));

        Mockito.when(postMapper.postRequestDtoToPost(eq(postRequest))).thenReturn(post);
        Mockito.when(postService.replacePost(eq(3L), eq(post)))
                .thenReturn(savedPost);
        Mockito.when(postAssembler.toModel(eq(savedPost))).thenReturn(res);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/posts/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
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
        Post post = new Post("title", "description");

        Mockito.when(postMapper.postRequestDtoToPost(eq(postRequest)))
                .thenReturn(post);
        Mockito.when(postService.replacePost(eq(3L), eq(post)))
                .thenThrow(new PostNotFoundException(3L));

        mockMvc.perform(MockMvcRequestBuilders.
                        put("/posts/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andDo(print());
    }

    @Test
    @DisplayName("Post 수정시 PostRequest desc가 비어있어 유효성 검증 실패, 400반환")
    void testReplacePostReturnBadRequest() throws Exception {
        PostRequest postRequest = new PostRequest("title", "   ");

        mockMvc.perform(MockMvcRequestBuilders.
                        put("/posts/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.errors.desc").value("description cannot be blank"))
                .andDo(print());
    }

    @Test
    @DisplayName("Post 수정시 PostRequest desc가 180자를 초과하여 유효성 검증 실패, 422반환")
    void testReplacePostPostReturn422() throws Exception {
        String s = "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij" +
                "abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghi" +
                "abcdefghijabcdefghijabcdefghij212345678901";
        PostRequest postRequest = new PostRequest("title", s);
        Post post = new Post("title",s);

        Mockito.when(postMapper.postRequestDtoToPost(eq(postRequest))).thenReturn(post);
        Mockito.when(postService.replacePost(eq(3L), eq(post)))
                .thenThrow(new ConstraintViolationException(validator.validate(post)));

        mockMvc.perform(MockMvcRequestBuilders.
                        put("/posts/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.errors.desc").value("desc is should not be greater 1 and less than 180"))
                .andDo(print());
    }

    @Test
    @DisplayName("id에 맞는 Post 1건 삭제, 성공 204 반환")
    void testDeletePostByIdReturn200OK() throws Exception {
        Mockito.doNothing().when(postService).deletePost(3L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/3"))
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

        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.detail").isNotEmpty())
                .andExpect(jsonPath("$.instance").isNotEmpty())
                .andDo(print());
    }


}