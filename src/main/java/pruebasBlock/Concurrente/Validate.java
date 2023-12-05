package pruebasBlock.Concurrente;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;

public class Validate {
    private ExecutorService executorService;
    public Validate(){
        this.executorService = Executors.newCachedThreadPool();
    }
    public void validateConcurrentlyAndRemoveInvalidBlocks(List<Block> block, BlockChain blockChain ) {
        int numBlocks = block.size();

        // Crear tantos hilos como bloques en la cadena
        for (int i = 0; i < numBlocks; i++) {
            executorService.submit(() -> blockChain.validateAndRemoveBlock(numBlocks-1));
        }

        // Esperar a que todos los hilos completen
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
