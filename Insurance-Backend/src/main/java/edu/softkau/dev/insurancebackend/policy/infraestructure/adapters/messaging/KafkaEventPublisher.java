package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.messaging;

import edu.softkau.dev.insurancebackend.policy.domain.events.PolicyStatusChangedEvent;
import edu.softkau.dev.insurancebackend.policy.domain.ports.PolicyEventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida que implementa el puerto del dominio.
 * Utiliza Spring Kafka para enviar el evento real al clúster de Kafka.
 */
@Component
public class KafkaEventPublisher implements PolicyEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    // Nombre del tópico de Kafka
    private static final String TOPIC = "policy-status-changed";

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(PolicyStatusChangedEvent event) {
        log.info("Enviando evento a Kafka: Póliza #{} cambió a estado {}", 
                 event.policyNumber(), event.newStatus());

        try {
            // Enviamos el evento. Usamos event.policyId() como key para ordenar los mensajes.
            kafkaTemplate.send(TOPIC, event.policyId(), event);
            log.info("Evento enviado con éxito para la póliza #{}", event.policyNumber());
        } catch (Exception e) {
            log.error("Error al enviar el evento a Kafka para la póliza #{}", event.policyNumber(), e);
        }
    }
}
