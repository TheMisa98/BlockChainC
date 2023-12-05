package pruebasBlock.Concurrente;

import java.util.ArrayList;
import java.util.List;

public class pruebas {
    public static void main(String[] args) {
        // Crear una cadena de bloques
        BlockChain Chain = new BlockChain();

        // Crear algunas transacciones
        List<Transaction> transactions1 = new ArrayList<>();
        transactions1.add(new Transaction("Alice", "Bob", (double) 10));
        // transactions1.add(new Transaction("Bob", "Charlie", 5));

        // List<Transaction> transactions2 = new ArrayList<>();
        // transactions2.add(new Transaction("Charlie", "Alice", 3));

        // Agregar bloques a la cadena
        Chain.addBlock(transactions1);
        // Chain.addBlock(transactions2);

        // Imprimir la cadena de bloques antes de la validación concurrente
        System.out.println("Blockchain before concurrent validation:");
        Chain.getBlockchain().forEach(block -> System.out.println("Block #" + block.getIndex()));

        // Validar concurrentemente y eliminar bloques inválidos
        Validate validar = new Validate();
        validar.validateConcurrentlyAndRemoveInvalidBlocks(Chain.getBlockchain(), Chain);

        // Imprimir la cadena de bloques después de la validación concurrente
        System.out.println("\nBlockchain after concurrent validation:");
        Chain.getBlockchain().forEach(block -> System.out.println("Block #" + block.getIndex()));
    }
}
