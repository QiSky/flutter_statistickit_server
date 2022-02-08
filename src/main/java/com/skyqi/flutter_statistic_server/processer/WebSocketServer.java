package com.skyqi.flutter_statistic_server.processer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@ServerEndpoint("/statistic/im")
@Component
public class WebSocketServer {

    public static final ConcurrentHashMap<String, Session> webSocketMap = new ConcurrentHashMap<>();

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject object = JSON.parseObject(message);
        if(object.containsKey("type") && !webSocketMap.containsKey(object.getString("identify"))) {
            webSocketMap.put(object.getString("identify"), session);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
        if(webSocketMap.containsValue(session)) {
           final AtomicReference<String> key = new AtomicReference<String>();
           webSocketMap.forEach((k,value) -> {
               if(value == session) {
                   key.set(k);
               }
           });
           if(key.get() != null) {
               try {
                   webSocketMap.get(key.get()).close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               webSocketMap.remove(key.get());
           }
        }
    }
}
