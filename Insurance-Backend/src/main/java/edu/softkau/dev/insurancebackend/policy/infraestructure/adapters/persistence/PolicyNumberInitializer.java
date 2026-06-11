package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence;

import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyNumberSequencer;
import edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.persistence.repository.JpaPolicyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Inicializador de base de datos que se ejecuta al arrancar el Spring Boot.
 * Carga el número de póliza más alto almacenado y actualiza el contador en memoria del secuenciador
 * para evitar colisiones de claves duplicadas tras reinicios del servidor.
 */
@Component
public class PolicyNumberInitializer implements CommandLineRunner {

    private final JpaPolicyRepository jpaPolicyRepository;

    public PolicyNumberInitializer(JpaPolicyRepository jpaPolicyRepository) {
        this.jpaPolicyRepository = jpaPolicyRepository;
    }

    @Override
    public void run(String... args) {
        int currentYear = java.time.LocalDate.now().getYear();
        String pattern = "POL-" + currentYear + "-%";
        String maxPolicyNumber = jpaPolicyRepository.findMaxPolicyNumber(pattern);
        if (maxPolicyNumber != null && !maxPolicyNumber.isBlank()) {
            String[] parts = maxPolicyNumber.split("-");
            if (parts.length == 3) {
                try {
                    int maxSequence = Integer.parseInt(parts[2]);
                    PolicyNumberSequencer.getInstance().setCounterValue(maxSequence);
                    System.out.println("[PolicyNumberInitializer] Secuenciador de polizas inicializado en: " + maxSequence + " basado en " + maxPolicyNumber);
                } catch (NumberFormatException e) {
                    System.err.println("[PolicyNumberInitializer] Error parseando la secuencia del numero de poliza maximo: " + maxPolicyNumber);
                }
            }
        }
    }
}
