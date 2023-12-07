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
        // resultado.innerHTML = "<div>Hola " + sender + ", el servidor respondio:" + data+"</div>"; 
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