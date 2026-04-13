package org.project.sohwagi.infra.firebase;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FirebaseMessagingClient {

  @Async("notificationExecutor")
  public void sendMessage(String fcmToken, String title, String body) {
    Notification notification = Notification.builder()
      .setTitle(title)
      .setBody(body)
      .build();

    ApsAlert alert = ApsAlert.builder()
      .setTitle(title)
      .setBody(body)
      .build();

    Aps aps = Aps.builder()
      .setAlert(alert)
      .setSound("default")
      .build();

    ApnsConfig apnsConfig = ApnsConfig.builder()
      .setAps(aps)
      .build();

    Message message = Message.builder()
      .setNotification(notification)
      .setApnsConfig(apnsConfig)
      .setToken(fcmToken)
      .build();

    try {
      String response = FirebaseMessaging.getInstance().send(message);
      log.info(response);
    } catch (FirebaseMessagingException e) {
      log.error(e.getMessage());
    }
  }

}
