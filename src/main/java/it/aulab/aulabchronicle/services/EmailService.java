package it.aulab.aulabchronicle.services;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
}
