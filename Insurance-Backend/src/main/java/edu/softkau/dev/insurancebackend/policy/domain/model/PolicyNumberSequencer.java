package edu.softkau.dev.insurancebackend.policy.domain.model;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Secuenciador único y seguro para la generación de números de póliza.
 * Implementa el patrón Singleton usando la clase interna de Bill Pugh.
 */
public class PolicyNumberSequencer {

    // AtomicInteger garantiza incrementos seguros frente a hilos sin usar 'synchronized'
    private final AtomicInteger counter;

    // El constructor privado restringe la instanciación externa directa
    private PolicyNumberSequencer() {
        this.counter = new AtomicInteger(0);
    }

    // Clase interna que la JVM carga bajo demanda, garantizando seguridad nativa frente a hilos
    private static class SingletonHolder {
        private static final PolicyNumberSequencer INSTANCE = new PolicyNumberSequencer();
    }

    // Retorna la única instancia del secuenciador
    public static PolicyNumberSequencer getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Incrementa la secuencia y formatea el número de póliza final.
     * Formato: POL-[AÑO]-[SECUENCIA] (ej. POL-2026-000001)
     */
    public String nextPolicyNumber() {
        int currentYear = LocalDate.now().getYear();
        int sequenceValue = counter.incrementAndGet();
        return String.format("POL-%d-%06d", currentYear, sequenceValue);
    }

    // Permite reiniciar el contador para asegurar el aislamiento entre pruebas unitarias
    public void reset() {
        this.counter.set(0);
    }

    // Permite establecer el valor del contador al inicializar la aplicación
    public void setCounterValue(int value) {
        this.counter.set(value);
    }
}