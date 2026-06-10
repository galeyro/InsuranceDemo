package edu.softkau.dev.insurancebackend.policy.infraestructure.adapters.messaging.consumers;

import edu.softkau.dev.insurancebackend.policy.domain.events.PolicyStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Adaptador de entrada (Inbound Adapter).
 * Escucha los mensajes de Kafka y actúa en consecuencia (por ejemplo, notificando al cliente).
 */
@Component
public class PolicyNotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(PolicyNotificationConsumer.class);

    @KafkaListener(topics = "policy-status-changed", groupId = "insurance-notification-group")
    public void consume(PolicyStatusChangedEvent event) {
        log.info("🔔 [Consumer de Notificaciones] ¡Recibido evento de cambio de estado!");
        log.info("   -> Tipo de Evento: {}", event.eventType());
        log.info("   -> ID Póliza: {}", event.policyId());
        log.info("   -> Número Póliza: {}", event.policyNumber());
        log.info("   -> Cliente ID: {}", event.customerId());
        log.info("   -> Ramo: {}", event.branch());
        log.info("   -> Transición: {} ===> {}", event.oldStatus(), event.newStatus());
        log.info("   -> Fecha (Timestamp): {}", event.timestamp());
        log.info("   -> [ACCIÓN] Enviando correo electrónico al cliente para notificar el cambio...");
    }
}