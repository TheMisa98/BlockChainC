package pruebasBlock.Concurrente;

import java.util.List;
import java.util.Random;

public class validateSec {
    private Random random;
    public validateSec(){
        this.random = new Random();
    }
    public void validateAndRemoveBlock(int blockIndex,List<Block> blockchain) {
        
        try{
            Thread.sleep(3);
            Block currentBlock = blockchain.get(blockIndex);
            Block previousBlock = blockchain.get(blockIndex - 1);

            if (random.nextDouble() < 0.1 ||
                !currentBlock.getPreviousHash().equals(previousBlock.getHash()) ||
                !currentBlock.getHash().equals(currentBlock.calculateHash())) {
                // Eliminar el bloque si la validación falla
                // blockchain.remove(blockIndex);
                System.out.println("Concurrent: Removing block #" + blockIndex);    
                }    
        }catch (Exception e) {
                // System.out.println("Bloque ya eliminado:");
        }
        
    }
}
