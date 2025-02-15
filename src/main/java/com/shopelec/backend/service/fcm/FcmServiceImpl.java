package com.shopelec.backend.service.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FcmServiceImpl implements FcmService{

    @Override
    public void sendNotificationAllUser(String title, String body) throws ExecutionException, InterruptedException {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
        Message message = Message.builder()
                .setNotification(notification)
                .setTopic("all_users")
                .build();
        FirebaseMessaging.getInstance().sendAsync(message).get();
    }
    @Override
    public void sendNotification(String token, String title, String body) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
        Message message = Message.builder()
                .setNotification(notification)
                .setToken(token)
                .build();
        FirebaseMessaging.getInstance().send(message);
    }
}
