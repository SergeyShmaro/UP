var id = 0;
var countNickUsed=0;
var username = "Spaceman";

function run(){
    var appContainer = document.getElementsByClassName('main')[0];

    appContainer.addEventListener('click', delegateMessage);
    appContainer.addEventListener('keyboard', delegateMessage);
}

function delegateMessage(evtObj) {
    if(evtObj.type === 'click' && evtObj.target.classList.contains('changenick')) {
        changeName(evtObj);
    }
    if((evtObj.type === 'click' && evtObj.target.classList.contains('send')) || evtObj.keyCode == 13) {
        sendMessage(evtObj);
    }
}

function changeName() {
    var input = document.getElementById('nick');
    var newnick=document.getElementById('nickOnHistory0');
    var i=0;
    while(i < countNickUsed){
        if(newnick.className === 'myRedactedMessage')
            newnick.innerHTML = newnick.innerHTML.replace(username, input.value);
        else
            newnick.innerHTML = input.value;
        i++;
        newnick=document.getElementById('nickOnHistory'+i);
    }
    username = input.value;
}

function sendMessage() {
    var message = document.getElementById('inputmessage');
    var divNick = document.createElement('li');
    var divMyMessage = document.createElement('li');
    var time = document.createElement('div');
    var butDelete = document.createElement('button');
    var butRedact = document.createElement('button');
    var divTextMessage=document.createElement('div');

    butDelete.classList.add('delete');
    butRedact.classList.add('redact');
    time.classList.add('time');
    divMyMessage.classList.add('myMessage');
    divNick.classList.add('user0');
    divTextMessage.classList.add('text');

    divMyMessage.setAttribute('id', 'user'+id);
    divTextMessage.setAttribute('id', 'textuser'+id);
    divNick.setAttribute('id', 'nickOnHistory'+countNickUsed);
    countNickUsed++;

    divNick.innerHTML=username;
    divTextMessage.innerHTML=message.value;
    time.textContent = new Date().toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");

    var s = ""+id;

    butDelete.addEventListener('click', function(){
        deleteMessage(s);
    });

    butRedact.addEventListener('click', function(){
        changeMessage(s);
    });

    divMyMessage.appendChild(butDelete);
    divMyMessage.appendChild(butRedact);
    divMyMessage.appendChild(divTextMessage);
    divMyMessage.appendChild(time);

    if(message.value != "")
    {
        document.getElementById('list').appendChild(divNick);
        document.getElementById('list').appendChild(divMyMessage);
    }
    message.value = "";
    id++;
}


function deleteMessage(s) {
    var time =document.createElement('div');
    time.textContent = new Date().toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");
    time.classList.add('time');
    var toDelete = document.getElementById('user' + s);
    toDelete.innerHTML = "Message deleted.";
    toDelete.classList.remove('myMessage');
    toDelete.classList.add('myDeletedMessage');
    toDelete.appendChild(time);
}

function changeMessage(s) {
    var textmessage=document.getElementById('textuser'+s);
    var newmessage=prompt("Redact your message", textmessage.innerHTML);
    var check= document.getElementById('nickOnHistory0');
    var i=0;
    var find=false;
    var idRedactedMesage='nickOnHistory'+countNickUsed;
    var time =document.createElement('div');
    time.textContent = new Date().toTimeString().replace(/.*(\d{2}:\d{2}:\d{2}).*/, "$1");
    time.classList.add('time');
    while(i<countNickUsed){
        if(check.className === 'myRedactedMessage') {
            find=true;
            break;
        }
        i++;
        check=document.getElementById('nickOnHistory'+i);
        console.log('find!');
    }
    if(find){
        idRedactedMesage=check.getAttribute('id');
        document.getElementById('list').removeChild(check);
        console.log('delete');
    }
    if (newmessage!=null && newmessage!=""){
        console.log('add');
        var divRedact= document.createElement('div');
        divRedact.classList.add('myRedactedMessage');
        divRedact.setAttribute('id', idRedactedMesage);
        if(!find)
            countNickUsed++;
        divRedact.innerHTML=username+' redacted upper message: ' + textmessage.innerHTML + ' to ' + newmessage;
        divRedact.appendChild(time);
        textmessage.innerHTML=newmessage;
        document.getElementById('list').appendChild(divRedact);
    }
}
