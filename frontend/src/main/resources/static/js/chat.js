let stompClient = null;

function connect() {
    const chatUser = sessionStorage.getItem("chatUser");
    if (!chatUser) {
        console.error("ERROR: No hay usuario en sessionStorage.");
        return;
    }

    const protocol = location.protocol === "https:" ? "wss://" : "ws://";
    const socketUrl = `${protocol}${location.host}/ws?user=${encodeURIComponent(chatUser)}`;

    console.log("DEBUG: Intentando conexión a:", socketUrl);

    const socket = new WebSocket(socketUrl);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log("DEBUG: Conectado como: " + chatUser);

        // Suscripción Lista Usuarios
        stompClient.subscribe('/topic/connected-users', function (message) {
            updateConnectedUsers(JSON.parse(message.body));
        });

        // Suscripción Broadcast
        stompClient.subscribe('/topic/broadcast', function (message) {
            const msg = JSON.parse(message.body);
            addMessage(`[Público] ${msg.senderUsername}: ${msg.subject} - ${msg.body}`, "broadcast");
        });

        // Suscripción Privados
        stompClient.subscribe('/user/queue/messages', function (message) {
            const msg = JSON.parse(message.body);
            addMessage(`[Privado] ${msg.senderUsername}: ${msg.subject} - ${msg.body}`, "private");
            showNotification("Nuevo mensaje de " + msg.senderUsername, msg.subject);
        });

    }, function (error) {
        console.error("DEBUG: Error STOMP:", error);
    });
} // <--- AQUÍ SE CIERRA LA FUNCIÓN CONNECT

// --- FUNCIONES DE ENVÍO (FUERA DE CONNECT) ---

function sendBroadcastFromForm() {
    const subject = document.getElementById('broadcast-subject').value;
    const body = document.getElementById('broadcast-body').value;

    if (stompClient?.connected) { // Optional Chaining para Sonar
        stompClient.send("/app/broadcast", {}, JSON.stringify({ subject, body }));
        document.getElementById('broadcast-subject').value = '';
        document.getElementById('broadcast-body').value = '';
    }
}

function sendPrivateFromForm() {
    const to = document.getElementById('private-to').value;
    const subject = document.getElementById('private-subject').value;
    const body = document.getElementById('private-body').value;

    if (stompClient?.connected) { // Optional Chaining para Sonar
        stompClient.send("/app/private", {}, JSON.stringify({ to, subject, body }));
        document.getElementById('private-subject').value = '';
        document.getElementById('private-body').value = '';
    }
}

function addMessage(text, type) {
    const messagesDiv = document.getElementById('messages');
    if (messagesDiv) {
        const div = document.createElement('div');
        div.className = "msg " + type;
        div.innerText = text;
        messagesDiv.appendChild(div);
        messagesDiv.scrollTop = messagesDiv.scrollHeight;
    }
}

function updateConnectedUsers(users) {
    const ul = document.getElementById('connected-users');
    if (ul) {
        ul.innerHTML = '';
        users.forEach(u => {
            const li = document.createElement('li');
            li.innerText = u;
            ul.appendChild(li);
        });
    }
}

function showNotification(title, body) {
    if (Notification.permission === "granted") {
        new Notification(title, { body });
    } else if (Notification.permission !== "denied") {
        Notification.requestPermission();
    }
}