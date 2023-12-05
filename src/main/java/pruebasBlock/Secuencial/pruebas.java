package pruebasBlock.Secuencial;

import java.util.ArrayList;
import java.util.List;

public class pruebas {
    public static void main(String[] args) {
        // Crear una cadena de bloques
        BlockchainExample blockchain = new BlockchainExample();

        // Crear algunas transacciones
        List<Transaction> transactions1 = new ArrayList<>();
        transactions1.add(new Transaction("Alice", "Bob", 10));
        transactions1.add(new Transaction("Bob", "Charlie", 5));
        // Agregar bloques a la cadena
        blockchain.addBlock(transactions1);

        // Imprimir la cadena de bloques
        for (Block block : blockchain.getBlockchain()) {
            System.out.println("Block #" + block.getIndex());
            System.out.println("Previous Hash: " + block.getPreviousHash());
            System.out.println("Hash: " + block.getHash());
            System.out.println("Transactions: " + block.getTransactions());
            System.out.println("Timestamp: " + block.getTimestamp());
            System.out.println();
        }
        int lastIndex = blockchain.getBlockchain().size();
        // Validar la cadena de bloques
        for (int i = 0; i < 10; i++) {
            blockchain.validateAndRemoveBlock(lastIndex-1);
        }
        System.out.println("\nDespues de eliminar\n");

        for (Block block : blockchain.getBlockchain()) {
            System.out.println("Block #" + block.getIndex());
            System.out.println("Previous Hash: " + block.getPreviousHash());
            System.out.println("Hash: " + block.getHash());
            System.out.println("Transactions: " + block.getTransactions());
            System.out.println("Timestamp: " + block.getTimestamp());
            System.out.println();
        }
    }
}
