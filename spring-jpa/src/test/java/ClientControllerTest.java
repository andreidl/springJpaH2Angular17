import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.duarte.spring.jpa.h2.SpringBootJpaH2Application;
import com.duarte.spring.jpa.h2.controller.ClientController;
import com.duarte.spring.jpa.h2.model.Address;
import com.duarte.spring.jpa.h2.model.Client;
import com.duarte.spring.jpa.h2.repository.ClientRepository;
import com.duarte.spring.jpa.h2.service.AddressService;
import com.duarte.spring.jpa.h2.service.ClientService;

@WebMvcTest(ClientController.class)
@ContextConfiguration(classes = SpringBootJpaH2Application.class)
public class ClientControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ClientService clientService;

	@MockBean
	private ClientRepository clientRepository;

	@MockBean
	private AddressService addressService;

	private Client client;
	private Address address;

	@BeforeEach
	public void setUp() {
		address = new Address();
		address.setPostalCode("12345-678");
		address.setNumber("100");
		address.setCity("São Paulo");
		address.setState("SP");
		address.setCountry("Brasil");

		client = new Client();
		client.setFirstName("Andrei");
		client.setLastName("Lima");
		client.setEmail("andrei@email.com");
		client.setPostalCode("12345-678");
		client.setNumber("100");
		client.setAddresses(List.of(address));
	}

	@Test
	void shouldGetAllClients() throws Exception {
		when(clientService.findAll()).thenReturn(List.of(client));

		mockMvc.perform(get("/api/clients")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Andrei"));

		verify(clientService, times(1)).findAll();
	}

	@Test
	void shouldGetClientById() throws Exception {
		when(clientService.findById(1L)).thenReturn(Optional.of(client));

		mockMvc.perform(get("/api/clients/1")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Andrei"));

		verify(clientService, times(1)).findById(1L);
	}

	@Test
	void shouldDeleteClient() throws Exception {
		doNothing().when(clientService).deleteById(1L);

		mockMvc.perform(delete("/api/clients/1")).andExpect(MockMvcResultMatchers.status().isNoContent());

		verify(clientService, times(1)).deleteById(1L);
	}
}