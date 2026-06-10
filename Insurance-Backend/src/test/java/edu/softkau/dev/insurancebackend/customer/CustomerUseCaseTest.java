package edu.softkau.dev.insurancebackend.customer;

import edu.softkau.dev.insurancebackend.customer.application.usecase.CreateCustomerUseCase;
import edu.softkau.dev.insurancebackend.customer.application.usecase.GetCustomerUseCase;
import edu.softkau.dev.insurancebackend.customer.domain.exception.CustomerNotFoundException;
import edu.softkau.dev.insurancebackend.customer.domain.exception.EmailAlreadyExistsException;
import edu.softkau.dev.insurancebackend.customer.domain.model.Customer;
import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.customer.domain.model.Email;
import edu.softkau.dev.insurancebackend.customer.domain.ports.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerUseCaseTest {

    private CustomerRepository customerRepository;
    private CreateCustomerUseCase createCustomerUseCase;
    private GetCustomerUseCase getCustomerUseCase;

    @BeforeEach
    public void setUp() {
        customerRepository = mock(CustomerRepository.class);
        createCustomerUseCase = new CreateCustomerUseCase(customerRepository);
        getCustomerUseCase = new GetCustomerUseCase(customerRepository);
    }

    @Test
    public void testCreateCustomerSuccess() {
        when(customerRepository.existsByEmail(any(Email.class))).thenReturn(false);

        Customer result = createCustomerUseCase.execute("Jane Doe", "jane.doe@example.com");

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane.doe@example.com", result.getEmail().getValue());
        verify(customerRepository, times(1)).save(result);
    }

    @Test
    public void testCreateCustomerThrowsIfEmailExists() {
        when(customerRepository.existsByEmail(any(Email.class))).thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            createCustomerUseCase.execute("Jane Doe", "jane.doe@example.com");
        });

        assertNotNull(exception.getMessage());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    public void testGetCustomerSuccess() {
        UUID uuid = UUID.randomUUID();
        CustomerId customerId = new CustomerId(uuid);
        Customer customer = Customer.create(customerId, "Jane Doe", new Email("jane.doe@example.com"));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        Customer result = getCustomerUseCase.execute(uuid);

        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals("Jane Doe", result.getName());
    }

    @Test
    public void testGetCustomerThrowsIfNotFound() {
        UUID uuid = UUID.randomUUID();
        CustomerId customerId = new CustomerId(uuid);

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            getCustomerUseCase.execute(uuid);
        });
    }
}
