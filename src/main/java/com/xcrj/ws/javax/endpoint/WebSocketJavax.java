package com.xcrj.ws.javax.endpoint;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/ws/javax/{uid}")
public class WebSocketJavax {
    private static final Logger log = LoggerFactory.getLogger(WebSocketJavax.class);

    /**
     * 存储session
     * 线程安全集合
     */
    private static final CopyOnWriteArraySet<Session> SESSIONS = new CopyOnWriteArraySet<>();

    /**
     * 存储在线连接数
     * uid,session
     */
    private static final Map<String, Session> SESSION_POOL = new HashMap<>();

    /**
     * 连接成功打开触发
     * 
     * @param session
     * @param uid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "uid") String uid) {
        try {
            SESSIONS.add(session);
            SESSION_POOL.put(uid, session);
            log.info("【WebSocket消息】有新的连接，总数为：" + SESSIONS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息触发
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("【WebSocket消息】收到客户端消息：" + message);
    }

    /**
     * 连接关闭触发
     * 
     * @param session
     * @param uid
     */
    @OnClose
    public void onClose(Session session, @PathParam("uid") String uid) {
        try {
            SESSIONS.remove(session);
            log.info("【WebSocket消息】连接断开，总数为：" + SESSIONS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发生错误时触发
     * 
     * @param session
     * @param uid
     * @param throwable
     */
    @OnError
    public void onError(Session session, @PathParam("uid") String uid, Throwable throwable) {
        log.info("[WebSocketServer] Connection Exception : uid = " + uid + " , throwable = "
                + throwable.getMessage());
    }

    /**
     * 广播
     *
     * @param message 消息
     */
    public void sendAllMessage(String message) {
        log.info("【WebSocket消息】广播消息：" + message);
        for (Session session : SESSIONS) {
            try {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单点
     *
     * @param uid  用户编号
     * @param message 消息
     */
    public void sendOneMessage(String uid, String message) {
        Session session = SESSION_POOL.get(uid);
        if (session != null && session.isOpen()) {
            try {
                synchronized (session) {
                    log.info("【WebSocket消息】单点消息：" + message);
                    session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 组播
     *
     * @param uids 用户编号列表
     * @param message 消息
     */
    public void sendMoreMessage(String[] uids, String message) {
        for (String uid : uids) {
            Session session = SESSION_POOL.get(uid);
            if (session != null && session.isOpen()) {
                try {
                    log.info("【WebSocket消息】单点消息：" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
