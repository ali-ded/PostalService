package ppryvarnikov.ps1.service;

import ppryvarnikov.ps1.dao.DeliveryDao;
import ppryvarnikov.ps1.dao.NotificationDao;
import ppryvarnikov.ps1.dto.Delivery;
import ppryvarnikov.ps1.dto.Notification;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Scheduler extends Thread {
    private final Logger logger = Logger.getLogger(Scheduler.class.getName());
    private final Delivery delivery;

    public Scheduler(Delivery delivery) {
        this.delivery = delivery;
    }

    @Override
    public void run() {
        Random random = new Random();
        String notificationForRecipient = "";
        boolean probablyParcelTaken;
        for (int i = 0; i < 5; i++) {
            probablyParcelTaken = random.nextInt(5) == 0;
            if (probablyParcelTaken) {
                delivery.getParcelStatus().setId((short) 2);
                notificationForRecipient = "Dear " + delivery.getFirstNameRecipient() +
                        ", your package from " + delivery.getSender().getFirstName() +
                        " has been successfully received.";
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Thread interrupted while running.", e);
            }
        }

        if (delivery.getParcelStatus().getId() == 1) {
            delivery.getParcelStatus().setId((short) 3);
            notificationForRecipient = "Dear " + delivery.getFirstNameRecipient() +
                    ", your parcel from " + delivery.getSender().getFirstName() + " is overdue.";
        }

        delivery.setDateTimeStatusChange(Timestamp.valueOf(LocalDateTime.now()));
        new DeliveryDao().update(delivery);
        new NotificationDao().insert(new Notification(notificationForRecipient, null));
    }
}
