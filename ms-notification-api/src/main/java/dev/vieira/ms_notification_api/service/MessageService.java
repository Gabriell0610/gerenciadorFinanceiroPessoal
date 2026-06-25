package dev.vieira.ms_notification_api.service;

public interface MessageService {

   void sendMessage(String destination, Object message);
}
