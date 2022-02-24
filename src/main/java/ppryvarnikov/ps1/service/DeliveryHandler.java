package ppryvarnikov.ps1.service;

import ppryvarnikov.ps1.dao.DeliveryDao;
import ppryvarnikov.ps1.dto.Delivery;

import java.util.Set;

public class DeliveryHandler extends Thread{

    @Override
    public void run() {
        Set<Delivery> deliveries = new DeliveryDao().getAll();
        for (Delivery delivery : deliveries) {
            new Scheduler(delivery).start();
        }
    }
}
