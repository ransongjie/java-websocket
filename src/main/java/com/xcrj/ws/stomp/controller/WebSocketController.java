package com.xcrj.ws.stomp.controller;

import com.xcrj.ws.stomp.domain.MyMsg;
import com.xcrj.ws.stomp.domain.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    /**
     * Client:
     * - Connect: client.subscribe('/user/queue/position-updates-user123',
     * - Send: stomp.send("/app/private", {}, payload);
     * Broker:
     * - Receive: @MessageMapping("/private")
     * - Send: @SendToUser("/queue/position-updates-user123")
     * @param req
     * @return
     */
    @MessageMapping("/private")
    @SendToUser("/queue/position-updates-user123")
    public String greating(@Payload String req) {
        System.out.println("From client: " + req);

        String res = "Private, from broker: received" + req;
        return res;
    }

    /**
     * Client:
     * - Connect: client.subscribe('/user/queue/position-updates-user123',
     * - Send: stomp.send("/app/private/json", {}, payload);
     * Broker:
     * - Receive: @MessageMapping("/private/json")
     * - Send: @SendToUser("/queue/position-updates-user123")
     * @param req
     * @return
     */
    @MessageMapping("/private/json")
    @SendToUser("/queue/position-updates-user123")
    public Teacher greating(Teacher req) {
        System.out.println("From client: " + req.toString());
        return req;
    }

    /**
     * Client:
     * - Connect: client.subscribe("/queue/notifications",
     * - Send: stomp.send("/app/broadcast", {}, payload);
     * Broker:
     * - Receive: @MessageMapping("/broadcast")
     * - Send: @SendTo("/queue/notifications")
     * @param req
     * @return
     */
    @MessageMapping("/broadcast")
    @SendTo("/queue/notifications")
    public MyMsg userShout(MyMsg req) {
        System.out.println("From client: " + req.getMessage());

        MyMsg res=new MyMsg();
        res.setMessage("Broadcast, from broker: received" + req.getMessage());
        return res;
    }
}
