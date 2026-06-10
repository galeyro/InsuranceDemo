package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.web;

import edu.softkau.dev.insurancebackend.policy.application.usecase.ChangePolicyStatusUseCase;
import edu.softkau.dev.insurancebackend.policy.application.usecase.CreatePolicyUseCase;
import edu.softkau.dev.insurancebackend.policy.application.usecase.GetPolicyUseCase;
import edu.softkau.dev.insurancebackend.policy.application.usecase.ListPoliciesByCustomerUseCase;
import edu.softkau.dev.insurancebackend.policy.domain.model.Policy;
import edu.softkau.dev.insurancebackend.policy.domain.model.RiskProfile;
import edu.softkau.dev.insurancebackend.policy.domain.ports.PolicyRepository;
import edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.web.dto.CreatePolicyDto;
import edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.web.dto.UpdatePolicyStatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST que expone los endpoints para interactuar con el módulo de pólizas (Policy).
 */
@RestController
@RequestMapping("/api/policies")
@Tag(name = "Policies", description = "Endpoints para la cotización, ciclo de vida y consulta de pólizas de seguros")
public class PolicyController {

    private final CreatePolicyUseCase createPolicyUseCase;
    private final GetPolicyUseCase getPolicyUseCase;
    private final ListPoliciesByCustomerUseCase listPoliciesByCustomerUseCase;
    private final ChangePolicyStatusUseCase changePolicyStatusUseCase;
    private final PolicyRepository policyRepository;

    public PolicyController(CreatePolicyUseCase createPolicyUseCase,
                            GetPolicyUseCase getPolicyUseCase,
                            ListPoliciesByCustomerUseCase listPoliciesByCustomerUseCase,
                            ChangePolicyStatusUseCase changePolicyStatusUseCase,
                            PolicyRepository policyRepository) {
        this.createPolicyUseCase = createPolicyUseCase;
        this.getPolicyUseCase = getPolicyUseCase;
        this.listPoliciesByCustomerUseCase = listPoliciesByCustomerUseCase;
        this.changePolicyStatusUseCase = changePolicyStatusUseCase;
        this.policyRepository = policyRepository;
    }

    /**
     * POST /api/policies
     * Cotiza y crea una póliza en estado inicial QUOTED.
     */
    @PostMapping
    @Operation(summary = "Cotizar y crear una nueva póliza", description = "Genera una cotización inicial basada en el ramo y la estrategia de tarificación. Retorna la póliza en estado QUOTED.")
    public ResponseEntity<Policy> createPolicy(@Valid @RequestBody CreatePolicyDto dto) {
        RiskProfile riskProfile = new RiskProfile(
                dto.getRiskProfile().getRiskScore(),
                dto.getRiskProfile().getCustomerSinceYear()
        );

        Policy policy = createPolicyUseCase.execute(
                dto.getCustomerId(),
                dto.getBranch(),
                dto.getRatingStrategy(),
                riskProfile
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(policy);
    }

    /**
     * GET /api/policies/{id}
     * Obtiene el detalle de una póliza por su ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una póliza por ID", description = "Recupera la información detallada de una póliza a partir de su UUID.")
    public ResponseEntity<Policy> getPolicyById(@PathVariable UUID id) {
        Policy policy = getPolicyUseCase.execute(id);
        return ResponseEntity.ok(policy);
    }

    /**
     * GET /api/policies/customer/{id}
     * Obtiene todas las pólizas de un cliente.
     */
    @GetMapping("/customer/{id}")
    @Operation(summary = "Obtener todas las pólizas de un cliente", description = "Lista todas las pólizas contratadas por un cliente a partir del UUID de este.")
    public ResponseEntity<List<Policy>> getPoliciesByCustomerId(@PathVariable UUID id) {
        List<Policy> policies = listPoliciesByCustomerUseCase.execute(id);
        return ResponseEntity.ok(policies);
    }

    /**
     * PATCH /api/policies/{id}/status
     * Transiciona el estado de una póliza e integra la publicación del evento.
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Cambiar el estado de una póliza", description = "Realiza la transición de estado basada en la máquina de estados. Si es exitosa, guarda los cambios y publica el evento en Kafka.")
    public ResponseEntity<Policy> updatePolicyStatus(@PathVariable UUID id, @Valid @RequestBody UpdatePolicyStatusDto dto) {
        Policy policy = getPolicyUseCase.execute(id);
        changePolicyStatusUseCase.execute(policy, dto.getTargetStatus());
        policyRepository.save(policy);
        return ResponseEntity.ok(policy);
    }
}
