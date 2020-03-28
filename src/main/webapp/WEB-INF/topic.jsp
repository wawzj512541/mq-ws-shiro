<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8" />
    <title>WebScoket广播式</title>
    <script src="/sockjs.js"></script>
    <script src="/stomp.js"></script>
    <script src="/jquery.js"></script>
</head>
<body onload="disconnect()">
<noscript><h2 style="color: #ff0000">貌似你的浏览器不支持websocket</h2></noscript>
<div>
    <div>
        <button id="connect" onclick="connect();">连接</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect();">断开连接</button>
    </div>
    <div id="conversationDiv">
        <label>输入要发送的消息</label><input type="text" id="name" />
        <button id="sendName" onclick="sendName();">发送</button>
        <p id="response"></p>
    </div>
</div>

<script type="text/javascript">
    var stompClient = null;

    function setConnected(connected) {
        document.getElementById('connect').disabled = connected;
        document.getElementById('disconnect').disabled = !connected;
        document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
        $('#response').html();
    }

    function connect() {
        var socket = new SockJS('/websocket'); //链接SockJS 的endpoint 名称为"/endpointWisely"
        stompClient = Stomp.over(socket);//使用stomp子协议的WebSocket 客户端
        stompClient.connect({}, function(frame) {//链接Web Socket的服务端。
            setConnected(true);

            stompClient.subscribe('/topic', function(respnose){ //订阅/topic/getResponse 目标发送的消息。这个是在控制器的@SendTo中定义的。
                showResponse(JSON.parse(respnose.body).message);
            });

            stompClient.subscribe('/test', function(respnose){
                showResponse(JSON.parse(respnose.body).message);
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

    function sendName() {
        var name = $('#name').val();
        //通过stompClient.send 向/welcome 目标 发送消息,这个是在控制器的@messageMapping 中定义的。
        stompClient.send("/app/test", {}, JSON.stringify({ 'message': name }));
    }

    function showResponse(message) {
        var response = $("#response");
        response.html(message);
    }
</script>
</body>
</html>