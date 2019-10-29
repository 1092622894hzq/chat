<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Hello WebSocket</title>
    <script src="http://cdn.bootcss.com/sockjs-client/1.1.1/sockjs.min.js"></script>
    <script src="http://cdn.bootcss.com/stomp.js/2.3.3/stomp.js"></script>
    <script src="http://cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>
    <script type="text/javascript">
        var ws;
        window.onload = function subOpen() {
            var target = "ws://localhost:8080/chat_war/websocket";
            if ("WebSocket" in window) {
                ws = new WebSocket(target);
            } else if ("MozWebSocket" in Window) {
                ws = new MozWebSocket(target);
            } else {
                alert("WebSocket is not support");
            }
            //关闭连接时触发的事件
            ws.onclose = function (e) {
                console.log('websocket 断开: ' + e.code + ' ' + e.reason + ' ' + e.wasClean);
            };
            //连接发生错误的回调方法
            ws.onerror = function () {
                console.log("发生错误");
            };
        };

        window.onbeforeunload = function() {
            closeWebSocket();
        };

        //关闭WebSocket连接  
        function closeWebSocket() {
            console.log("close");
            ws.close();
        }

        //客户端发消息到服务端
        function subSend() {
            var obj = null;
                obj = {
                    message: "年后",
                    messageType: 1,
                    type : 0,
                    fromId : 3,
                    toIdOrGroupId : 4,
                    avatar : "http:8889f032rr.png",
                    name : "xioaxi"
            };
            var str = JSON.stringify(obj); //将对象转换为字符串
            ws.send(str);
            alert("发送成了");


        }
    </script>
</head>
<body>
<button onclick="subSend()">发送</button>
</body>
</html>
