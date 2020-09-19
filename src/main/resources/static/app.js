'use strict';
document.querySelector('#welcomeForm').addEventListener('submit', connect, true)
document.querySelector('#dialogueForm').addEventListener('submit', sendMessage, true)
var stompClient = null;
var name = null;
var fname = null;
var topic=null;
function connect(event) {
	name = document.querySelector('#name').value.trim();
	fname = document.querySelector('#fname').value.trim();
	if (name) {
		document.querySelector('#welcome-page').classList.add('hidden');
		document.querySelector('#dialogue-page').classList.remove('hidden');
		var socket = new SockJS('/websocket-chat');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, connectionSuccess);
	}
	event.preventDefault();
}
function connectionSuccess() {
	topic = "group";
	if(fname){
		topic = name < fname ? (fname+"-"+name) : (name+"-"+fname);
	}
	stompClient.subscribe('/topic/'+topic, onMessageReceived);
	stompClient.send("/app/chat/add/"+name, {},{});
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
    	if (this.readyState == 4 && this.status == 200) {
        	var json = JSON.parse(xhttp.responseText);
        	console.log("ok---"+json);
        	for(var i = 0; i < json.length; i++) {
    			var message = json[i];
    			console.log("ok---"+message);
    			if(message.type === 'DELIVER'){
    				var messageElement = document.createElement('li');
    				messageElement.classList.add('message-data');
					var element = document.createElement('i');
					var text = document.createTextNode(message.userId);
					element.appendChild(text);
					messageElement.appendChild(element);
					var usernameElement = document.createElement('span');
					var usernameText = document.createTextNode(message.userId);
					usernameElement.appendChild(usernameText);
					messageElement.appendChild(usernameElement);
					var textElement = document.createElement('p');
					var messageText = document.createTextNode(message.content);
					textElement.appendChild(messageText);
					messageElement.appendChild(textElement);
					document.querySelector('#messageList').appendChild(messageElement);
					document.querySelector('#messageList').scrollTop = document.querySelector('#messageList').scrollHeight;
					stompClient.send("/app/chat/"+topic+"/msg", {}, JSON.stringify({
						userId : name,
						type : 'READ'
					}));
    			}
			}
    	}
	};
	xhttp.open("GET", '/app/'+topic+'/msgs', true);
	xhttp.send();
}
function sendMessage(event) {
	var messageContent = document.querySelector('#chatMessage').value.trim();
	if (messageContent && stompClient) {
		var chatMessage = {
			userId : name,
			content : document.querySelector('#chatMessage').value,
			type : 'DELIVER'
		};
		stompClient.send("/app/chat/"+topic+"/msg", {}, JSON
				.stringify(chatMessage));
		document.querySelector('#chatMessage').value = '';
	}
	event.preventDefault();
}

function preMessagesLoad(message) {
	var messageElement = document.createElement('li');
	if (message.type === 'SENT') {
		messageElement.classList.add('event-data');
		message.content = 'Message was sent';
	} else if (message.type === 'READ') {
		messageElement.classList.add('event-data');
		message.content = name != message.userId ? 'Message was read' : 'New Message';
	} else {
		messageElement.classList.add('message-data');
		var element = document.createElement('i');
		var text = document.createTextNode(message.userId);
		element.appendChild(text);
		messageElement.appendChild(element);
		var usernameElement = document.createElement('span');
		var usernameText = document.createTextNode(message.userId);
		usernameElement.appendChild(usernameText);
		messageElement.appendChild(usernameElement);
		if(name != message.userId){
			stompClient.send("/app/chat/"+topic+"/msg", {}, JSON.stringify({
				userId : name,
				type : 'READ'
			}));
		}
	}
	var textElement = document.createElement('p');
	var messageText = document.createTextNode(message.content);
	textElement.appendChild(messageText);
	messageElement.appendChild(textElement);
	document.querySelector('#messageList').appendChild(messageElement);
	document.querySelector('#messageList').scrollTop = document
			.querySelector('#messageList').scrollHeight;
}

function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);
	preMessagesLoad(message);
}