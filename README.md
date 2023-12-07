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

Con esto aplicamos la primera parte que fue la paralelización de hilos.

### Cliente Servidor

Una vez creado y desarrollado la sección paralela toca creer un cliente servidor el cual funcionará en una página html con java script, utilizando el servidor de **UNDERTOW** y **MAVEN** como gestor de paquetes java, más adelante veremos la instalación de este proyecto, por el momento seguiremos con su explicación del proyecto.

Para este servidor la única interacción creada entre el servidor y el cliente es un paso de mensaje con las transacciones generadas en la página, en java se recibe esta información se crea el bloque y se valida si fue correcto.

Veamos una sección del código.

```java 
// Obtenemos las partes del a guardar en la block chain
    String[] Parts = message.split("\\|");
    String Sender = Parts[0];
    String Receiver = Parts[1];
    String Amount = Parts[2];
                            
```
En esta sección se separa el mensaje de que nos manda la página en sus partes de la transacción como son quién lo envia, quién lo recibe y la cantidad.

```java
    //Creamos un objeto de transacción y la añadimos a la blockcchian
    List<Transaction> trasactions = new ArrayList<>();

    trasactions.add(new Transaction(Sender, Receiver, (double) Integer.parseInt(Amount)));

    chain.addBlock(trasactions);

    Block lastBlocBefore = chain.getBlockchain().get(chain.getBlockchain().size()-1);


```

En esta sección creamos el bloque nuevo de transacción y lo enviamos a la blockchain.

```java
    //Validamos que sea correcto de forma concurrente y guardamos si lo fue o no
    Validate validar = new Validate();
    validateSec validateSec = new validateSec();
    // validación de tiempos de respues
    long timeConcurrentInit = System.currentTimeMillis(); 
    validar.validateConcurrentlyAndRemoveInvalidBlocks(chain.getBlockchain(), chain);
    long timeConcurrentEnd = System.currentTimeMillis() - timeConcurrentInit; 

    long timeSecuenInit = System.currentTimeMillis();
    validateSec.validateAndRemoveBlock(chain.getBlockchain().size()-1, chain.getBlockchain());
    long timeSecuenEnd = System.currentTimeMillis()-timeSecuenInit;
```

Por ultimo instanciamos los validadores concurrentes y secuenciales para medir tiempos de espera.

### Paso de mensajes

Con esta información y ya validado el bloque podemos mandar la información a la página diciendo si fue correcto el bloque o por si de lo contrario no se agrego a la cadena.

```java
    if(lastBlocBefore == lastBloc){
        // caso contrario obtenemos los datos del ultimo bloque añadido y lo mandamos a consola
        Sender = lastBloc.getTransactions().get(0).getSender();
        Receiver = lastBloc.getTransactions().get(0).getReceiver();
        String Hash = lastBloc.getHash();
        String PreviousHash = lastBloc.getPreviousHash();
        Amount = String.valueOf(lastBloc.getTransactions().get(0).getAmount());
        exchange.getResponseSender().send("LastBlock => \nSender: "+Sender+" \nReceiver " + Receiver + " \nAmout: " +Amount+ " \nPrevious Hash: " + PreviousHash+" \nHash: "+Hash+"\n\nSize Blockchain: " +chain.getBlockchain().size()+" \n\nTiempo concurrente: "+String.valueOf(timeConcurrentEnd) +" Tiempo secuencial: " +String.valueOf(timeSecuenEnd));
    }else{
        exchange.getResponseSender().send("Bloque Incorrecto"+" Tiempo concurrente: "+(timeConcurrentEnd) +" Tiempo secuencial: " +String.valueOf(timeSecuenEnd));
    }
```
Con este if verificamos si el bloque fue eliminado o se agrego correctamente, junto con una cadena formateada con la información del bloque para mostrarlo en consola del navegador, al igual que los tiempos de ejecución.

Ahora veamos el js de la página para ver como se mandan los mensajes al servidor. 

```js
function Enviar(sender,receiver,amount){
    fetch( 'http://localhost:8080/?mensaje=' + sender +"|"+ receiver +"|"+ amount ) // URL reconocida por la aplicación java principal
    .then(response => {
        if (!response.ok) {
            throw new Error('La solicitud no pudo ser completada correctamente.');
        }
        return response.text();  // debe ser "text" en minúsculas
        //return response.json(); // Si se espera una respuesta JSON
    })
    .then(data => {
        if(data.startsWith("Bloque Incorrecto")){
            console.log(data)
            alert("Su bloque tuvo inconsistencias por favor vuelva a intentarlo")
        }else{
            console.log(data);
            submitTransaction(sender,receiver,amount);
        }                
        
    })
    .catch(error => {
        alert('Hubo un error al realizar la solicitud:');  // Muestra error en pantalla
        // console.error('Hubo un error al realizar la solicitud:', error);  // Manda error a la consola del navegador
    });

}
```
En la siguiente función se manda los dantos separdos por '|' al servidor, y este retorna un mensaje empezando por "Bloque incorrecto" lo cual sólo avisa al usuario de que su bloque tuvo incosistencias, y en consola se muestran los datos.

Caso contrario se manda la data formateada a la consola del navegador y se llama la función de **"submitTransaction"** para actualizaar la página con el nuevo bloque creado.
