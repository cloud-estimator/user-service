package cloud.estimator.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.spring.web.advice.MediaTypes;

import com.fasterxml.jackson.databind.ObjectMapper;

import cloud.estimator.user.config.Constants;
import cloud.estimator.user.domain.User;
import cloud.estimator.user.repository.UserRepository;
import cloud.estimator.user.service.UserService;
import cloud.estimator.user.web.rest.errors.ExceptionTranslator;
import cloud.estimator.user.web.rest.vm.UserVM;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(secure = true, print = MockMvcPrint.SYSTEM_OUT)
public class AccountResourceTests {

	@Autowired
	private MockMvc restMvcAuthenticated;

	@Autowired
	private MockMvc restMvcUnauthenticated;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	protected ObjectMapper mapper;

	@Autowired
	private UserService userService;

	@Mock
	private UserService mockUserService;

	@Autowired
	private HttpMessageConverter[] httpMessageConverters;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	private MockMvc restUserMockMvc;

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);
		AccountResource accountResource = new AccountResource(userRepository, userService);

		AccountResource accountUserMockResource = new AccountResource(userRepository, mockUserService);

		this.restMvcAuthenticated = MockMvcBuilders.standaloneSetup(accountResource)
				.setMessageConverters(httpMessageConverters)
				.setControllerAdvice(exceptionTranslator).build();

		this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource)
				.setControllerAdvice(exceptionTranslator).build();
	}

	@Test
	public void testNonAuthenticatedUserAllow() throws Exception {
		restUserMockMvc.perform(get("/api/allow")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
	}

	@Test
	public void testNonAuthenticatedUserDeny() throws Exception {
		restMvcUnauthenticated
				.perform(get("/api/deny")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaTypes.PROBLEM))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testRegisterValid() throws Exception {

		UserVM validUser = UserVM.builder()
				.login("pam")
				.password("1234567")
				.email("pam@email.com")
				.imageUrl("http://www.logo.com")
				.langKey("en")
				.name("PM Pam")
				.accountId("8a8081966337e912016337e93d850001")
				.build();

		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(validUser);

		assertThat(userRepository.findOneByLogin("pam").isPresent()).isFalse();

		restMvcAuthenticated
					.perform(post("/api/register")
					.contentType(TestUtil.APPLICATION_JSON_UTF8)
					.content(TestUtil.convertObjectToJsonBytes(validUser)))
					.andDo(print())
					.andExpect(status()
					.isCreated());

		assertThat(userRepository.findOneWithAccountsByLogin("pam").isPresent()).isTrue();
		User user = userRepository.findOneWithAccountsByLogin("pam").get();
		assertThat(user.getAccounts().size() > 0);

	}

	@Test
	@Transactional
	public void testRegisterInvalidLogin() throws Exception {
		UserVM invalidUser = UserVM.builder().login("funky-log!n")// <-- invalid
				.password("password").name("Funky One").email("funky@example.com").activated(true)
				.imageUrl("http://placehold.it/50x50").langKey(Constants.DEFAULT_LANGUAGE).build();

		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(invalidUser);

		restMvcUnauthenticated.perform(post("/api/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(invalidUser))).andExpect(status().isBadRequest());

		Optional<User> user = userRepository.findOneByEmailIgnoreCase("funky@example.com");
		assertThat(user.isPresent()).isFalse();
	}
}
