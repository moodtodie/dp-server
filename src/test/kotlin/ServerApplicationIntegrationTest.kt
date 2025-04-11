//import com.diploma.server.ServerApplication
//import com.diploma.server.controller.auth.AuthenticationRequest
//import com.diploma.server.controller.auth.AuthenticationResponse
//import com.diploma.server.service.TokenService
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.mock.mockito.SpyBean
//import org.springframework.security.core.userdetails.UserDetailsService
//import org.springframework.test.context.TestPropertySource
//import org.springframework.test.web.servlet.MockMvc
//import kotlin.test.Test
//
//@SpringBootTest(classes = [ServerApplication::class])
//@AutoConfigureMockMvc
//@TestPropertySource("classpath:application-test.properties")
//class ServerApplicationIntegrationTest {
//    @Value("\${jwt.expiredToken}")
//    private lateinit var oldToken: String
//
//    @Autowired
//    private lateinit var mockMvc: MockMvc
//
//    @SpyBean
//    private lateinit var tokenService: TokenService
//
//    @SpyBean
//    private lateinit var userDetailsService: UserDetailsService
//
////    @Test
////    fun `access secured endpoint with new token from the refresh token after token expiration`() {
////        val authRequest = AuthenticationRequest("email-1@gmail.com", "pass1")
////        var jsonRequest = jacksonObjectMapper().writeValueAsString(authRequest)
////
////        var response = mockMvc.perform(
////            post("/api/auth")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(jsonRequest)
////        )
////            .andExpect(status().isOk)
////            .andExpect(jsonPath("$.accessToken").isNotEmpty)
////            .andExpect(jsonPath("$.refreshToken").isNotEmpty).andReturn().response.contentAsString
////
////        val authResponse = jacksonObjectMapper().readValue(response, AuthenticationResponse::class.java)
////
////        // access secured endpoint
////        mockMvc.perform(
////            get("/api/hello")
////                .header("Authorization", "Bearer ${authResponse.accessToken}")
////        )
////            .andExpect(status().isOk)
////            .andExpect(content().string("Hello, Authorized User!"))
////
////        // simulate access token expiration
////        `when`(tokenService.extractUsername(authResponse.accessToken))
////            .thenThrow(ExpiredJwtException::class.java)
////
////        mockMvc.perform(
////            get("/api/hello")
////                .header("Authorization", "Bearer ${authResponse.accessToken}")
////        )
////            .andExpect(status().isForbidden)
////
////        // create a new access token from the refresh token
////        val refreshTokenRequest = RefreshTokenRequest(authResponse.refreshToken)
////        jsonRequest = jacksonObjectMapper().writeValueAsString(refreshTokenRequest)
////
////        response = mockMvc.perform(
////            post("/api/auth/refresh")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(jsonRequest)
////        )
////            .andExpect(status().isOk)
////            .andExpect(jsonPath("$.token").isNotEmpty).andReturn().response.contentAsString
////
////        val newAccessToken = jacksonObjectMapper().readValue(response, TokenResponse::class.java)
////
////        reset(tokenService)
////
////        // access secured endpoint with the new token
////        mockMvc.perform(
////            get("/api/hello")
////                .header("Authorization", "Bearer ${newAccessToken.token}")
////        )
////            .andExpect(status().isOk)
////            .andExpect(content().string("Hello, Authorized User!"))
////    }
//
////    @Test
////    fun `should return unauthorized for unauthenticated user`() {
////        val authRequest = AuthenticationRequest("some-user", "pass1")
////        val jsonRequest = jacksonObjectMapper().writeValueAsString(authRequest)
////
////        mockMvc.perform(
////            post("/api/auth")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(jsonRequest)
////        )
////            .andExpect(status().isUnauthorized)
////    }
//}
