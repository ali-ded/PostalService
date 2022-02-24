package ppryvarnikov.ps1.service;

import ppryvarnikov.ps1.dao.ConnectionPool;
import ppryvarnikov.ps1.dao.DeliveryDao;
import ppryvarnikov.ps1.dao.NotificationDao;
import ppryvarnikov.ps1.dto.Notification;
import ppryvarnikov.ps1.io.Constants;
import ppryvarnikov.ps1.io.Writer;
import ppryvarnikov.ps1.io.WriterTablesToFile;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationHandler extends Thread {
    private final Logger logger = Logger.getLogger(NotificationHandler.class.getName());

    @Override
    public void run() {
        int deliveriesNumberOfRows = new DeliveryDao().getNumberOfRows();
        int messagesSent = 0;
        Writer writer = new Writer();

        while (messagesSent < deliveriesNumberOfRows) {
            for(Notification notification : new NotificationDao().getAll()) {
                if (notification.getNotificationStatus().getId() == 1) {
                    notification.getNotificationStatus().setId((short) 2);
                    if(new NotificationDao().update(notification)) {
                        writer.writeMesasgeToFile(notification.getMessage());
                    }
                    messagesSent++;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "Thread interrupted while running.", e);
            }
        }

        new WriterTablesToFile().writeAllTablesToFile(Constants.OUTPUT_FILE, true);
        ConnectionPool.close();
        logger.log(Level.INFO, "Application completed successfully.");
    }
}
