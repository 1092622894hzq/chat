<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Hello WebSocket</title>
    <script src="http://cdn.bootcss.com/sockjs-client/1.1.1/sockjs.min.js"></script>
    <script src="http://cdn.bootcss.com/stomp.js/2.3.3/stomp.js"></script>
    <script src="http://cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            connect();
            //checkoutUserlist();
        });

        var stompClient = null;

        function setConnected(connected) {
            document.getElementById('connect').disabled = connected;
            document.getElementById('disconnect').disabled = !connected;
            document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
            document.getElementById('response').innerHTML = '';
        }

        //this line.
        function connect() {
            var userId = 3;
            var socket = new SockJS('http://'+window.location.host+'/Chat_war/hello');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {
                setConnected(true);
                console.log('Connected: ' + frame);
                stompClient.subscribe('/user/+'+userId+'/queue/result', function(msg){
                    alert(msg);
                    alert(JSON.parse(msg.body));
                    showGreeting(JSON.parse(msg.body));
                });
                stompClient.subscribe('/user/4/queue/result', function (msg) {
                    alert(msg);
                    alert(JSON.parse(msg.body));
                    showGreeting(JSON.parse(msg.body));
                });
                stompClient.subscribe("/topic/group/"+userId, function (msg) {
                    alert(msg);
                    alert(JSON.parse(msg.body));
                    showGreeting(JSON.parse(msg.body));
                });
                 stompClient.subscribe("/user/"+userId+"/errors", function (msg) {
                    alert(msg);
                    alert(JSON.parse(msg.body));
                    showGreeting(JSON.parse(msg.body));
                });



                stompClient.subscribe("/user/"+userId+"/queue/position-updates", function (msg) {
                    alert(msg+"---");
                    alert(JSON.parse(msg.body));
                    showGreeting(JSON.parse(msg.body));
                });
                // stompClient.subscribe("/topic/group", function (msg) {
                //     alert(msg);
                //     alert(JSON.parse(msg.body));
                //     showGreeting(JSON.parse(msg.body));
                // });

                stompClient.subscribe('/topic/groupTalk/1' ,function(msg){
                    alert(JSON.parse(msg.body));
                //    showGreeting(JSON.parse(msg.body));
                });
            });
        }

        function sendName() {
            var name = document.getElementById('name').value;
            // var payload = JSON.stringify(
            //     {
            //         'gmFromId': name ,
            //         'groupId' : name ,
            //         'gmType' : name ,
            //         'gmContent' : name
            //     }
            //     );
            var payload = JSON.stringify(
                {
                    'messageContent': name ,
                    'messageStatus' : name ,
                    'messageGroup' : name ,
                    'messageType' : name,
                    'messageFromId' : name,
                    'messageToId' : name,
                    'userId' : name
                }
                );
           // stompClient.send("/app/hello", {atytopic:"greetings"}, payload );
            stompClient.send('/app/friend',{atytopic:"greetings"}, payload );
            //stompClient.send('/user/message', {atytopic:"greetings"}, payload);
        }

        function connectAny() {
            var socket = new SockJS('http://'+window.location.host+'/Chat_war/hello');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {
                setConnected(true);
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/feed', function(greeting){
                    alert(JSON.parse(greeting.body).content);
                    showGreeting(JSON.parse(greeting.body).content);
                });
            });
        }

        function disconnect() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected");
        }


        function showGreeting(message) {
            var response = document.getElementById('response');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.appendChild(document.createTextNode(message));
            response.appendChild(p);
        }
    </script>
</head>
<body>
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being enabled. Please enable
    Javascript and reload this page!</h2></noscript>
<div>
    <div>
        <button id="connect" onclick="connect();">Connect</button>
        <button id="connectAny" onclick="connectAny();">ConnectAny</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
    </div>
    <div id="conversationDiv">
        <label>What is your name?</label><input type="text" id="name" />
        <button id="sendName" onclick="sendName();">Send</button>
        <p id="response"></p>
    </div>
</div>
</body>
</html>
