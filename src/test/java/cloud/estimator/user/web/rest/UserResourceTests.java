package cloud.estimator.user.web.rest;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.awt.print.Printable;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cloud.estimator.user.domain.User;
import cloud.estimator.user.repository.UserRepository;
import cloud.estimator.user.service.UserService;
import cloud.estimator.user.web.rest.errors.ExceptionTranslator;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(secure = true, print = MockMvcPrint.SYSTEM_OUT)
public class UserResourceTests {

	@Autowired
	private MockMvc restMvcAuthenticated;

	@Autowired
	private MockMvc restMvcUnauthenticated;

	@Autowired
	protected ObjectMapper mapper;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepo;

	@Mock
	private UserService mockUserService;

	@Autowired
	private HttpMessageConverter[] httpMessageConverters;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	private MockMvc restUserMockMvc;

	private String adminJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJvcmdhbml6YXRpb25JZCI6ImMyOWM1ODZlLWU5YTAtNDI0Mi04NThhLWU4ZWFjMTVjNjQzZCIsImF1dGgtbmFtZSI6ImRkaGFybmEiLCJ1c2VyX25hbWUiOiJkZGhhcm5hIiwic2NvcGUiOlsid2ViY2xpZW50IiwibW9iaWxlY2xpZW50Il0sImV4cCI6MTUyNjk4MjA2NiwiYXV0aG9yaXRpZXMiOlsiOGE4MDgxOTY2MzM3ZTkxMjAxNjMzN2U5M2Q4NTAwMDE6Uk9MRV9BRE1JTiJdLCJqdGkiOiJmYmIwMjQxYy01MWEwLTQzMDktOTBiNi1iZDg2ZTU1NmRhYzgiLCJjbGllbnRfaWQiOiJlYWdsZWV5ZSJ9.4ppTLzIGP7TQtOdQPo_liBkFeU4tnuHZMKLJnnraYH8";
	private String userJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJvcmdhbml6YXRpb25JZCI6Ijk4NmQ2MGVlLWU4NWEtNDViZC1iYjU1LTgzOWVmMDE4MWNhOCIsImF1dGgtbmFtZSI6InZkaGFybmEiLCJ1c2VyX25hbWUiOiJ2ZGhhcm5hIiwic2NvcGUiOlsid2ViY2xpZW50IiwibW9iaWxlY2xpZW50Il0sImV4cCI6MTUyNjEyMDQ3NSwiYXV0aG9yaXRpZXMiOlsiOGE4MDgxOTY2MzM3ZTkxMjAxNjMzN2U5M2Q4NTAwMDE6Uk9MRV9VU0VSIl0sImp0aSI6IjdkMTI0OGE2LTZlMjItNDgzZi1hZDI1LTI0NDZmZjE0NzA4OSIsImNsaWVudF9pZCI6ImVhZ2xlZXllIn0.rEo37W92zgvuqrCaL9R-4rwAkcBCgUb7vrUvVrTj92M";

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);
		UserResource userResource = new UserResource(userService, userRepo);

		UserResource userMockResource = new UserResource(mockUserService, userRepo);

		this.restMvcAuthenticated = MockMvcBuilders.standaloneSetup(userResource)
				.setMessageConverters(httpMessageConverters)
				.setControllerAdvice(exceptionTranslator).build();

		this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userMockResource)
				.setControllerAdvice(exceptionTranslator).build();
	}

	@Test
	public void testGetAllUsersForAccountByAdminShouldPass() throws Exception {

		ResultActions result = restMvcUnauthenticated
				.perform(get("/api/users/8a8081966337e912016337e93d850001")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + adminJwt)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		List<User> users = mapper.readValue(
				result.andReturn().getResponse().getContentAsString(), new TypeReference<List<User>>() {
				});

		assertTrue(users.size() > 0);
		assertTrue(users.get(0).getLogin().equals("ddharna"));

	}

	@Test
	public void testGetAllUsersForAccountByUserShouldFail() throws Exception {

		restMvcUnauthenticated
				.perform(get("/api/users/8a8081966337e912016337e93d850001")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + userJwt)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isForbidden());

	}
}
