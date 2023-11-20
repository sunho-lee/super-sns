package com.example.controllerexample.user;

import com.example.controllerexample.role.Role;
import com.example.controllerexample.user.dto.SignUpRequest;
import com.example.controllerexample.user.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = UserController.class)
@ComponentScan(basePackageClasses = UserMapper.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    BCryptPasswordEncoder encoder;

    @MockBean
    @Autowired
    private UserMapper userMapper;

    @Test
    //@WithAnonymousUser
    @DisplayName("회원가입 성공 200 반환")
    void signUp() throws Exception {
        //given
        SignUpRequest request = new SignUpRequest("username", "nickname", "password");

        String password = encoder.encode("password");
        User user = new User("username", "nickname", password);
        Role role = new Role(1L, "ROLE_USER");
        user.addRole(role);

        User savedUser = new User(1L, "username", "nickname", password);

        Set<String> set = new HashSet<>();
        set.add("ROLE_USER");
        UserResponse res = new UserResponse(1L, "username", "nickname", set );

        Mockito.when(userService.saveUser(any())).thenReturn(savedUser);
        Mockito.when(userMapper.userToUserResponse(any())).thenReturn(res);

        this.mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/v1/users")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.nickname").value("nickname"))
                .andExpect(jsonPath("$.password").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"))
                .andDo(print());
    }

}
