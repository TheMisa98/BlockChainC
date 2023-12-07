var transactionHistory = [];
var currentColor = getRandomColor(); // Inicializar con un color aleatorio

function submitTransaction(sender,receiver,amount) {
    // var sender = document.getElementById('sender').value;
    // var receiver = document.getElementById('receiver').value;
    // var amount = document.getElementById('amount').value;

    if (sender === '' || receiver === '' || amount === '') {
        alert('Por favor, complete todos los campos del formulario.');
        return;
    }

    var transaction = {
        sender: sender,
        receiver: receiver,
        amount: parseFloat(amount),
        color: currentColor // Asignar el color actual a la transacción
    };

    simulateTransactionSubmission(transaction);
}

function simulateTransactionSubmission(transaction) {
    setTimeout(function() {
        transactionHistory.push(transaction);
        updateTransactionHistoryContainer();
        document.getElementById('transactionForm').reset();
        currentColor = getRandomColor(); // Generar un nuevo color para la siguiente transacción
    }, 1000);
}

function updateTransactionHistoryContainer() {
    var historyContainer = document.getElementById('transactionHistoryContainer');
    historyContainer.innerHTML = '';

    transactionHistory.forEach(function(transaction, index) {
        var transactionItem = document.createElement('div');
        transactionItem.className = 'transactionItem';
        transactionItem.innerHTML = '<p><strong>Sender:</strong> ' + transaction.sender + '</p>' +
                                    '<p><strong>Receiver:</strong> ' + transaction.receiver + '</p>' +
                                    '<p><strong>Amount:</strong> ' + transaction.amount + '</p>';
        
        // Aplicar el color de la transacción actual al bloque
        transactionItem.style.backgroundColor = transaction.color;

        // Agregar flecha solo si no es el primer bloque
        if (index > 0) {
            transactionItem.style.marginLeft = '20px';
        }

        // Aplicar animación a nuevos bloques
        transactionItem.style.opacity = '1';
        transactionItem.style.transform = 'scale(1)';

        historyContainer.appendChild(transactionItem);
    });
}


function getRandomColor() {
    var colors = ['#3498db', '#f39c12', '#9b59b6'];
    var randomIndex = Math.floor(Math.random() * colors.length);
    return colors[randomIndex];
}