package edu.softkau.dev.insurancebackend.policy;

import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyNumberSequencer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyNumberSequencerTest {

    private PolicyNumberSequencer sequencer;

    @BeforeEach
    public void setUp() {
        // Obtenemos el Singleton y reiniciamos el contador antes de cada prueba para mantenerlas aisladas
        sequencer = PolicyNumberSequencer.getInstance();
        sequencer.reset();
    }

    @Test
    public void shouldReturnSameInstance() {
        PolicyNumberSequencer first = PolicyNumberSequencer.getInstance();
        PolicyNumberSequencer second = PolicyNumberSequencer.getInstance();

        // Comprobamos que el Singleton devuelva siempre la misma referencia física en memoria
        assertSame(first, second);
    }

    @Test
    public void shouldGenerateSequentialNumbers() {
        int currentYear = LocalDate.now().getYear();

        // Validamos la generación consecutiva de los primeros números con el formato correcto
        assertEquals(String.format("POL-%d-000001", currentYear), sequencer.nextPolicyNumber());
        assertEquals(String.format("POL-%d-000002", currentYear), sequencer.nextPolicyNumber());
        assertEquals(String.format("POL-%d-000003", currentYear), sequencer.nextPolicyNumber());
    }

    @Test
    public void shouldGenerateUniqueNumbersConcurrently() throws InterruptedException {
        int totalThreads = 100;
        ExecutorService threadPool = Executors.newFixedThreadPool(totalThreads);
        
        // startingGun mantiene congelados a los hilos en la salida hasta que demos la señal
        CountDownLatch startingGun = new CountDownLatch(1);
        
        // finishLine espera a que los 100 hilos completen su tarea y lleguen a la meta
        CountDownLatch finishLine = new CountDownLatch(totalThreads);
        
        // ConcurrentHashMap.newKeySet crea un Set seguro para hilos que descarta elementos duplicados
        Set<String> generatedNumbers = ConcurrentHashMap.newKeySet();

        for (int i = 0; i < totalThreads; i++) {
            threadPool.submit(() -> {
                try {
                    startingGun.await(); // El hilo espera pacientemente la señal
                    generatedNumbers.add(sequencer.nextPolicyNumber());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLine.countDown(); // El hilo terminó e informa su llegada
                }
            });
        }

        startingGun.countDown(); // ¡Disparo de salida! Todos los hilos se ejecutan simultáneamente
        boolean allThreadsFinished = finishLine.await(5, TimeUnit.SECONDS); // Esperamos a que todos terminen
        threadPool.shutdown();

        assertTrue(allThreadsFinished);
        // Si no hay duplicados, el tamaño del Set debe coincidir exactamente con el total de hilos
        assertEquals(totalThreads, generatedNumbers.size());
    }
}
