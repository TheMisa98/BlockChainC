package pruebasBlock.Secuencial;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;




// Clase para representar una cadena de bloques
class BlockchainExample {
    private List<Block> blockchain;
    private Random random;
    

    public BlockchainExample() {
        this.blockchain = new ArrayList<>();
        this.random = new Random();
        // Agregar el bloque génesis
        createGenesisBlock();
    }

    private void createGenesisBlock() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("Genesis", "Someone", 1000));
        Block genesisBlock = new Block(0, transactions, "0");
        blockchain.add(genesisBlock);
    }

    public void addBlock(List<Transaction> transactions) {
        int index = blockchain.size();
        String previousHash = blockchain.get(index - 1).getHash();
        Block newBlock = new Block(index, transactions, previousHash);
        blockchain.add(newBlock);
    }

    public boolean isValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }
        }
        return true;
    }
    public void validateAndRemoveBlock(int blockIndex) {
        try{
            Block currentBlock = blockchain.get(blockIndex);
            Block previousBlock = blockchain.get(blockIndex - 1);

            if (random.nextDouble() < 0.1 ||
                !currentBlock.getPreviousHash().equals(previousBlock.getHash()) ||
                !currentBlock.getHash().equals(currentBlock.calculateHash())) {
                // Eliminar el bloque si la validación falla
                blockchain.remove(blockIndex);
                System.out.println("Removing block #" + blockIndex);    
                }    
        }catch (Exception e) {
                // System.out.println("Bloque ya eliminado:");
        }
        
    }

    public List<Block> getBlockchain() {
        return blockchain;
    }
}

