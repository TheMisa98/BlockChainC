# Blockchain de utilizando el paradigma paralelo
## Universidad Nacional Autónoma de México

### Desarrollado por **Misael Ramos Navarrete** estudiante de la licenciatura en Ciencia de Datos  

## ¿Que es la blockchain?  
Esta consiste en bloques de transacciones registrados por millones de usuarios, trabaja como un tipo de lista ligada pues tenemos dos hash que actúan como punteros uno que es el hash del bloque de transacción actual y otro que apunta al bloque anterior estos hash se obtienen resolviendo problemas complicados, pero lo importante son las validaciones de estos bloques, estas son realizadas por millones de usuarios para asegurar que el bloque generado es correcto es decir que los bloques de hash coinciden.

![!\[Alt text\](Exponencial.png)](images/Exponencial.png)

Para efectos prácticos se decidió utilizar el tipo de hasheo con la normativa SHA-256 siendo eficaz rápida y fácil de implantar en java puesto que ya viene de forma nativa en el lenguaje 

## Aplicación

Es aquí en donde yo decidí actuar aplicando los temas vistos en clase como el paso de mensajes, cliente servidor, y paralelización de hilos

### Paralelización de Hilos

Lo que se decidió paralelizar fue la validación de los bloques, los hilos del procesador actuaran como otros usuarios para validar que el bloque con sus hash respectivos son correctos 

![!\[Alt text\](<Exponencial - Page 1.png>)](<images/Exponencial - Page 1.png>)

Utilizando java para hacer este proceso, se generan una función validar para asegurar que el bloque generado es correcto, aquí una porción del código

``` java
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
```

Esta es la función primordial para hacer la validación de los bloques como podemos notar no es mas complicada que crear varios hilos con una sola misión la de validar los bloques pero esto ¿Cómo funciona?:

``` java 
     public void validateAndRemoveBlock(int blockIndex) {
        Block currentBlock = blockchain.get(blockIndex);
        Block previousBlock = blockchain.get(blockIndex - 1);
        
        if (random.nextDouble() < 0.01 ||
            !currentBlock.getPreviousHash().equals(previousBlock.getHash()) ||
            !currentBlock.getHash().equals(currentBlock.calculateHash())) {
            // Eliminar el bloque si la validación falla
            try {
                blockchain.remove(blockIndex);
                System.out.println("Removing block #" + blockIndex);    
            } catch (Exception e) {
                System.out.println("Bloque ya eliminado anteriormente");
            }
        }
    }
```
Una vez creado el bloque este se obtiene y se valida que su hash del anterior bloque junto con el bloque actual sean iguales, pero al utilizar la SHA-256 esto siempre será correcto pues esta no tiene un indice de error real, por tanto agregué una probabilidad de fallar del $1$%, en caso de fallar este se decide eliminarlo de la cadena, así conseguimos una una simulación de blockchain.

Con esto aplicamos la primera parte que fue la paralelización de hilos

### Cliente Servidor