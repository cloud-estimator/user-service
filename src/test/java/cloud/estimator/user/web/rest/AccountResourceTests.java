package cloud.estimator.user.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import cloud.estimator.user.config.Constants;
import cloud.estimator.user.domain.User;
import cloud.estimator.user.repository.UserRepository;
import cloud.estimator.user.web.rest.vm.UserVM;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(secure = true, print = MockMvcPrint.SYSTEM_OUT)
public class AccountResourceTests {

	@Autowired
	private MockMvc restMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	protected ObjectMapper mapper;

	@Before
	public void init() {
		// create an account

	}

	@Test
	public void testRegisterValid() throws Exception {

		UserVM validUser = UserVM
				.builder()
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

		restMvc.perform(
				post("/api/register")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(validUser)))
				.andDo(print())
				.andExpect(status().isCreated());

		assertThat(userRepository.findOneWithAccountsByLogin("pam").isPresent()).isTrue();
		User user = userRepository.findOneWithAccountsByLogin("pam").get();
		assertThat(user.getAccounts().size() > 0);

	}

	@Test
	@Transactional
	public void testRegisterInvalidLogin() throws Exception {
		UserVM invalidUser = UserVM.builder()
				.login("funky-log!n")// <-- invalid
				.password("password")
				.name("Funky One")
				.email("funky@example.com")
				.activated(true)
				.imageUrl("http://placehold.it/50x50")
				.langKey(Constants.DEFAULT_LANGUAGE)
				.build();

		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(invalidUser);

		restMvc.perform(
				post("/api/register")
						.contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(invalidUser)))
				.andExpect(status().isBadRequest());

		Optional<User> user = userRepository.findOneByEmailIgnoreCase("funky@example.com");
		assertThat(user.isPresent()).isFalse();
	}
}
