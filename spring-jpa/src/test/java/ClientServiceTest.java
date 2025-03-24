import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.duarte.spring.jpa.h2.model.Client;
import com.duarte.spring.jpa.h2.repository.ClientRepository;
import com.duarte.spring.jpa.h2.service.ClientService;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

	@Mock
	private ClientRepository clientRepository;

	@InjectMocks
	private ClientService clientService;

	@Test
	void shouldCreateClient() {
		Client client = new Client(0, "Andrei", "Lima", "andrei@email.com", null, null, null);

		Client savedClient = new Client(1L, "Andrei", "Lima", "andrei@email.com", null, null, null);
		when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

		Client result = clientService.save(client);

		assertNotNull(result.getId(), "ID do cliente não deve ser nulo");
		assertEquals(1L, result.getId(), "ID do cliente não corresponde ao esperado");

		assertNotNull(result.getFirstName(), "first name do cliente não deve ser nulo");
		assertEquals("Andrei", result.getFirstName(), "Primeiro nome do cliente está errado");

		assertNotNull(result.getEmail(), "Email do cliente não deve ser nulo");
		assertEquals("andrei@email.com", result.getEmail(), "Email do cliente está errado");
	}

}
