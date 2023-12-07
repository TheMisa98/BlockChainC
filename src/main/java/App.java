import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import pruebasBlock.Concurrente.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    

    public static void main(final String[] args) {
        
        BlockChain chain = new BlockChain();
        

        Undertow server = Undertow.builder().addHttpListener(8080, "localhost")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        String message = exchange.getQueryParameters().get("mensaje") != null
                                ? exchange.getQueryParameters().get("mensaje").getFirst()
                                : null;

                        if (message != null) {
                            // Process the message and send a response
                            // Obtenemos las partes del a guardar en la block chain
                            String[] Parts = message.split("\\|");
                            String Sender = Parts[0];
                            String Receiver = Parts[1];
                            String Amount = Parts[2];
                            


                            //Creamos un objeto de transacción y la añadimos a la blockcchian
                            List<Transaction> trasactions = new ArrayList<>();

                            trasactions.add(new Transaction(Sender, Receiver, (double) Integer.parseInt(Amount)));
                            
                            chain.addBlock(trasactions);

                            Block lastBlocBefore = chain.getBlockchain().get(chain.getBlockchain().size()-1);

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

                            //
                            

                            // exchange.getResponseSender().send(responseMessage);
                            // Si el bloque fue incorrecto avisamos a la pagina
                            Block lastBloc = chain.getBlockchain().get(chain.getBlockchain().size()-1);

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
                            
                        } else {
                            // Serve the HTML page
                            String htmlContent = readHtmlFile("./html/prueba.html");
                            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
                            exchange.getResponseSender().send(htmlContent);
                        }
                    }
                }).build();
        server.start();
    }

    private static String readHtmlFile(String filePath) {
        try (InputStream inputStream = App.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream != null) {
                try (Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A")) {
                    return scanner.hasNext() ? scanner.next() : "";
                }
            } else {
                return "HTML file not found";
            }
        } catch (IOException e) {
            // Manejar la IOException o registrar la excepción
            e.printStackTrace();
            return "Error leyendo archivo HTML: debe estar en src\\resources";
        }
    }
}
