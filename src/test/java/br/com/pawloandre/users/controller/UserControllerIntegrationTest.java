package br.com.pawloandre.users.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pawloandre.users.config.SecurityConfig;
import br.com.pawloandre.users.model.User;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class UserControllerIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
	@Autowired
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@Test
	void testFindAllSuccess() throws Exception {
		mockMvc.perform(get("/users").with(user("user").password("password").roles("USER"))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void testFindAllUnauthorized() throws Exception {
		mockMvc.perform(get("/users")).andExpect(status().isUnauthorized());
	}

	@Test
	void testFindByIdUnauthorized() throws Exception {
		mockMvc.perform(get("/users/1")).andExpect(status().isUnauthorized());
	}

	@Test
	void testSaveWithAdminRoleSuccess() throws Exception {
		User user = new User();
		user.setUsername("testadmin");
		user.setName("Test Admin");
		user.setPassword("password");
		user.setRoles(Arrays.asList("ADMIN"));

		mockMvc.perform(post("/users").with(user("admin").password("password").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void testSaveWithAdminRoleUnauthorized() throws Exception {
		User user = new User();
		user.setUsername("testadmin");
		user.setName("Test Admin");
		user.setPassword("password");
		user.setRoles(Arrays.asList("ADMIN"));

		mockMvc.perform(
				post("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void testSaveWithUserRoleSuccess() throws Exception {
		User user = new User();
		user.setUsername("testuser");
		user.setName("Test User");
		user.setPassword("password");
		user.setRoles(Arrays.asList("USER"));

		mockMvc.perform(post("/users").with(user("admin").password("password").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void testSaveWithUserRoleUnauthorized() throws Exception {
		User user = new User();
		user.setUsername("testuser");
		user.setName("Test User");
		user.setPassword("password");
		user.setRoles(Arrays.asList("USER"));

		mockMvc.perform(
				post("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void testSaveWithUserAndAdminRolesSuccess() throws Exception {
		User user = new User();
		user.setUsername("testuseradmin");
		user.setName("Test User Admin");
		user.setPassword("password");
		user.setRoles(Arrays.asList("USER", "ADMIN"));

		mockMvc.perform(post("/users").with(user("admin").password("password").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void testSaveWithUserAndAdminRolesUnauthorized() throws Exception {
		User user = new User();
		user.setUsername("testuseradmin");
		user.setName("Test User Admin");
		user.setPassword("password");
		user.setRoles(Arrays.asList("USER", "ADMIN"));

		mockMvc.perform(
				post("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void testSaveWithNoRolesSuccess() throws Exception {
		User user = new User();
		user.setUsername("testnoroles");
		user.setName("Test No Roles");
		user.setPassword("password");
		user.setRoles(Collections.emptyList());

		mockMvc.perform(post("/users").with(user("admin").password("password").roles("ADMIN"))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void testSaveWithNoRolesUnauthorized() throws Exception {
		User user = new User();
		user.setUsername("testnoroles");
		user.setName("Test No Roles");
		user.setPassword("password");
		user.setRoles(Collections.emptyList());

		mockMvc.perform(
				post("/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void testUpdateConflict() throws Exception {
		User user = new User();
		user.setId(1);
		user.setUsername("updateduser");
		user.setName("Updated User");
		user.setPassword("password");
		user.setRoles(Arrays.asList("USER"));

		mockMvc.perform(put("/users/1").with(user("user").password("password").roles("USER"))
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isConflict()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void testUpdateUnauthorized() throws Exception {
		User user = new User();
		user.setId(1);
		user.setUsername("updateduser");
		user.setName("Updated User");
		user.setPassword("password");
		user.setRoles(Arrays.asList("USER"));

		mockMvc.perform(
				put("/users/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void testDeleteSuccess() throws Exception {
		mockMvc.perform(delete("/users/1").with(user("admin").password("password").roles("ADMIN")))
				.andExpect(status().isNoContent());
	}

	@Test
	void testDeleteUnauthorized() throws Exception {
		mockMvc.perform(delete("/users/1")).andExpect(status().isUnauthorized());
	}

	@Test
	void testPublicEndpoint() throws Exception {
		mockMvc.perform(get("/users/public")).andExpect(status().isOk())
				.andExpect(content().string("Este é um endpoint público!"));
	}

	@Test
	void testAdminEndpointSuccess() throws Exception {
		mockMvc.perform(get("/users/admin").with(user("admin").password("password").roles("ADMIN")))
				.andExpect(status().isOk()).andExpect(content().string("Este é um endpoint apenas para ADMIN!"));
	}

	@Test
	void testAdminEndpointUnauthorized() throws Exception {
		mockMvc.perform(get("/users/admin").with(user("user").password("password").roles("USER")))
				.andExpect(status().isForbidden());
	}

	@Test
	void testAdminEndpointForbidden() throws Exception {
		mockMvc.perform(get("/users/admin")).andExpect(status().isUnauthorized());
	}
}