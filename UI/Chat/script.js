var id = 0;
var username = "Spaceman";
var historyMes = [];

function run(){
    var appContainer = document.getElementsByClassName('main')[0];
    appContainer.addEventListener('click', delegateMessage);
    appContainer.addEventListener('keyboard', delegateMessage);
    historyMes = loadMessages() || [newMessage('Hello world')];
    id = historyMes[historyMes.length - 1].id;
    username = loadUsername() || "Spaceman";
    document.getElementById("nick").value = username;
    updateHistory(historyMes);
}

function delegateMessage(evtObj) {
    if(evtObj.type === 'click' && evtObj.target.classList.contains('changenick')) {
        changeName(evtObj);
    }
    if(evtObj.type === 'click' && evtObj.target.classList.contains('send'))  {
        sendMessage(evtObj);
    }
}

function changeName() {
    var input = document.getElementById('nick');
    var nick=input.value;
    nick=deleteSpaces(nick);
    if(nick != "" && nick!=username)
        username = nick;
    input.value=nick;
    saveUsername(username);
    updateHistory(historyMes);
}

function sendMessage() {
    var inputMessage = document.getElementById('inputmessage');
    var text = inputMessage.value;
    text=deleteSpaces(text);
    inputMessage.value=text;
    if (text != "") {
        historyMes.push(newMessage(text));
        updateHistory(historyMes);
        inputMessage.value = "";
    }
}

function newMessage(text){
    id++;
    return {
        nick: username,
        id: id,
        messagetext: text,
        time: new Date().toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1"),
        isDeleted: false
    };

}

function deleteSpaces(text){
    return text.replace(/(^\s+|\s+$)/g,'');
}

function updateHistory(historyMes){
    document.getElementById("list").innerHTML = "";
    for (var i = 0; i < historyMes.length; i++)
        showUpdatedHistory(historyMes[i]);
    document.getElementById('list').scrollTop=9999;
    saveMessages(historyMes);
}

function showUpdatedHistory(message) {

    var divNick = document.createElement('li');
    var divMyMessage = document.createElement('li');
    var time = document.createElement('div');
    var divTextMessage=document.createElement('div');

    if(username === message.nick){
        var inputNewMessage=document.createElement('input');
        var butCancel=document.createElement('button');

        if(!message.isDeleted) {
            var butRedact = document.createElement('button');
            var butDelete = document.createElement('button');
            butDelete.classList.add('delete');
            divMyMessage.appendChild(butDelete);
            butDelete.addEventListener('click', function () {
                deleteMessage(message);
            });
            butRedact.classList.add('redact');
            divMyMessage.appendChild(butRedact);
            butRedact.addEventListener('click', function () {
                changeMessage(message);
            });
        }

        butCancel.classList.add("cancelOfRedact");
        butCancel.setAttribute('id', 'cancel'+message.id);
        butCancel.addEventListener('click', function () {
            stopChangeMessage(message);
        });
        butCancel.hidden=true;
        inputNewMessage.classList.add('redactInput');
        inputNewMessage.setAttribute('id','redactInput'+message.id);
        inputNewMessage.hidden=true;

        divMyMessage.appendChild(inputNewMessage);
        divMyMessage.appendChild(butCancel);
        divMyMessage.classList.add('myMessage');
        divNick.classList.add('user0');
    }
    else{
        divMyMessage.classList.add('userMessage');
        divNick.classList.add('user1');
    }

    time.classList.add('time');
    divTextMessage.classList.add('text');
    divMyMessage.setAttribute('id', 'user' + message.id);
    divTextMessage.setAttribute('id', 'textuser' + message.id);

    divNick.innerHTML = message.nick;
    time.textContent = message.time;
    divTextMessage.innerHTML = message.messagetext;

    divMyMessage.appendChild(divTextMessage);
    divMyMessage.appendChild(time);

    document.getElementById('list').appendChild(divNick);
    document.getElementById('list').appendChild(divMyMessage);
}


function deleteMessage(mes) {
    mes.messagetext='Message deleted';
    mes.time="Deleted on " + new Date().toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");
    mes.isDeleted=true;
    updateHistory(historyMes);
}

function stopChangeMessage(mes){
    var input = document.getElementById('redactInput'+mes.id);
    var butCancel= document.getElementById('cancel'+mes.id);
    var textOfMessage = document.getElementById("textuser"+mes.id);
    input.value=deleteSpaces(input.value);
    if( input.value!= "" &&
        input.value!= mes.messagetext){
            mes.messagetext=input.value;
            mes.time='Redacted on ' + new Date().toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");
    }
    input.hidden=true;
    butCancel.hidden=true;
    textOfMessage.hidden=false;
    updateHistory(historyMes);
}

function changeMessage(mes) {
    var input = document.getElementById('redactInput'+ mes.id);
    var butCancel= document.getElementById('cancel'+ mes.id);
    var textOfMessage = document.getElementById("textuser"+mes.id);
    input.hidden=false;
    input.value=mes.messagetext;
    butCancel.hidden=false;
    textOfMessage.hidden=true;
}

function saveMessages(listToSave) {
    if (!isStorage())
        return;

    localStorage.setItem("History", JSON.stringify(listToSave));
}

function saveUsername(name) {
    if (!isStorage())
        return;

    localStorage.setItem("Username", name);
}

function loadMessages() {
    if (!isStorage())
        return;
    var item = localStorage.getItem("History");

    return item && JSON.parse(item);
}

function loadUsername() {
    if (!isStorage())
        return;

    var item = localStorage.getItem("Username");
    console.log(item);
    return item;
}

function isStorage() {
    if (typeof(Storage) === "undefined") {
        alert('localStorage is not accessible');
        return false;
    }
    else
        return true;
}