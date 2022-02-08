package com.skyqi.flutter_statistic_server.controller;

import com.alibaba.fastjson.JSON;
import com.skyqi.flutter_statistic_server.model.DataModel;
import com.skyqi.flutter_statistic_server.processer.WebSocketServer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
public class MessageController {

    @PostMapping("/statistic/post")
    public HashMap<String,Object> getData(@RequestBody List<DataModel> data) {
        data.forEach(item -> {
            try {
                if(WebSocketServer.webSocketMap.get(item.getIdentify()) != null) {
                    WebSocketServer.webSocketMap.get(item.getIdentify()).getBasicRemote().sendText(JSON.toJSONString(item));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return null;
    }
}
