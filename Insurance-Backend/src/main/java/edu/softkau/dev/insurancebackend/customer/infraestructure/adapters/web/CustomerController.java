package edu.softkau.dev.insurancebackend.customer.infraestructure.adapters.web;

import edu.softkau.dev.insurancebackend.customer.application.usecase.CreateCustomerUseCase;
import edu.softkau.dev.insurancebackend.customer.application.usecase.GetCustomerUseCase;
import edu.softkau.dev.insurancebackend.customer.application.usecase.ListCustomersUseCase;
import edu.softkau.dev.insurancebackend.customer.domain.model.Customer;
import edu.softkau.dev.insurancebackend.customer.infraestructure.adapters.web.dto.CreateCustomerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST que expone los endpoints para interactuar con el módulo de clientes (Customer).
 */
@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Endpoints para la gestión y consulta de clientes")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerUseCase getCustomerUseCase;
    private final ListCustomersUseCase listCustomersUseCase;

    public CustomerController(CreateCustomerUseCase createCustomerUseCase, 
                              GetCustomerUseCase getCustomerUseCase,
                              ListCustomersUseCase listCustomersUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.getCustomerUseCase = getCustomerUseCase;
        this.listCustomersUseCase = listCustomersUseCase;
    }

    /**
     * POST /api/customers
     * Crea un nuevo cliente en el sistema.
     */
    @PostMapping
    @Operation(summary = "Crear un nuevo cliente", description = "Registra un nuevo cliente validando que su correo electrónico sea único y tenga un formato válido.")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CreateCustomerDto dto) {
        Customer customer = createCustomerUseCase.execute(dto.getName(), dto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    /**
     * GET /api/customers/{id}
     * Obtiene el detalle de un cliente por su ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un cliente por ID", description = "Recupera la información detallada de un cliente existente a partir de su identificador único UUID.")
    public ResponseEntity<Customer> getCustomerById(@PathVariable UUID id) {
        Customer customer = getCustomerUseCase.execute(id);
        return ResponseEntity.ok(customer);
    }

    /**
     * GET /api/customers
     * Obtiene todos los clientes registrados.
     */
    @GetMapping
    @Operation(summary = "Listar todos los clientes", description = "Retorna la lista completa de todos los clientes registrados.")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(listCustomersUseCase.execute());
    }
}
