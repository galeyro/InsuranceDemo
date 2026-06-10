package edu.softkau.dev.insurancebackend.customer;

import edu.softkau.dev.insurancebackend.customer.domain.model.Customer;
import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;
import edu.softkau.dev.insurancebackend.customer.domain.model.Email;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerDomainTest {

    @Test
    public void testCustomerCreationSuccess() {
        CustomerId id = new CustomerId(UUID.randomUUID());
        Email email = new Email("test@example.com");
        Customer customer = Customer.create(id, "John Doe", email);

        assertNotNull(customer);
        assertEquals(id, customer.getId());
        assertEquals("John Doe", customer.getName());
        assertEquals(email, customer.getEmail());
        assertTrue(customer.isActive());
        assertNotNull(customer.getCreatedAt());
        assertNotNull(customer.getUpdatedAt());
    }

    @Test
    public void testCustomerValidationExceptions() {
        CustomerId id = new CustomerId(UUID.randomUUID());
        Email email = new Email("test@example.com");

        assertThrows(NullPointerException.class, () -> new Customer(null, "John Doe", email, true, Instant.now(), Instant.now()));
        assertThrows(NullPointerException.class, () -> new Customer(id, "John Doe", null, true, Instant.now(), Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new Customer(id, "", email, true, Instant.now(), Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new Customer(id, "   ", email, true, Instant.now(), Instant.now()));
    }

    @Test
    public void testCustomerUpdatesAndDeactivation() {
        CustomerId id = new CustomerId(UUID.randomUUID());
        Email email = new Email("test@example.com");
        Customer customer = Customer.create(id, "John Doe", email);

        customer.updateName("Jane Doe");
        assertEquals("Jane Doe", customer.getName());

        Email newEmail = new Email("jane@example.com");
        customer.changeEmail(newEmail);
        assertEquals(newEmail, customer.getEmail());

        customer.deactivate();
        assertFalse(customer.isActive());

        customer.activate();
        assertTrue(customer.isActive());

        assertThrows(NullPointerException.class, () -> customer.updateName(null));
        assertThrows(NullPointerException.class, () -> customer.changeEmail(null));
    }

    @Test
    public void testCustomerBuilder() {
        CustomerId id = new CustomerId(UUID.randomUUID());
        Email email = new Email("test@example.com");
        Instant now = Instant.now();

        Customer customer = Customer.builder()
                .id(id)
                .name("Alice")
                .email(email)
                .isActive(false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertNotNull(customer);
        assertEquals(id, customer.getId());
        assertEquals("Alice", customer.getName());
        assertEquals(email, customer.getEmail());
        assertFalse(customer.isActive());
        assertEquals(now, customer.getCreatedAt());
        assertEquals(now, customer.getUpdatedAt());
    }

    @Test
    public void testCustomerIdEquality() {
        UUID uuid = UUID.randomUUID();
        CustomerId id1 = new CustomerId(uuid);
        CustomerId id2 = new CustomerId(uuid);
        CustomerId id3 = new CustomerId(UUID.randomUUID());

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertEquals(uuid.toString(), id1.toString());
        assertThrows(NullPointerException.class, () -> new CustomerId(null));
    }

    @Test
    public void testEmailValidation() {
        assertThrows(NullPointerException.class, () -> new Email(null));
        assertThrows(IllegalArgumentException.class, () -> new Email(""));
        assertThrows(IllegalArgumentException.class, () -> new Email("   "));
        assertThrows(IllegalArgumentException.class, () -> new Email("invalid-email"));
        assertThrows(IllegalArgumentException.class, () -> new Email("test@"));
        assertThrows(IllegalArgumentException.class, () -> new Email("test@domain"));

        Email email1 = new Email("TEST@EXAMPLE.com");
        Email email2 = new Email("test@example.com");
        Email email3 = new Email("other@example.com");

        assertEquals("test@example.com", email1.getValue());
        assertEquals(email1, email2);
        assertNotEquals(email1, email3);
        assertEquals(email1.hashCode(), email2.hashCode());
        assertEquals("test@example.com", email1.toString());
    }
}
