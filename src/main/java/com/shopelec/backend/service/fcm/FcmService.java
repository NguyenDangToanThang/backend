package com.shopelec.backend.service.fcm;

import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.concurrent.ExecutionException;

public interface FcmService {
    void sendNotificationAllUser(String title, String body) throws ExecutionException, InterruptedException;
    void sendNotification(String token, String title, String body) throws FirebaseMessagingException, ExecutionException, InterruptedException;
}
