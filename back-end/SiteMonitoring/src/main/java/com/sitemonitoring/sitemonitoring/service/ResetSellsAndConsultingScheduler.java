package com.sitemonitoring.sitemonitoring.service;

import com.sitemonitoring.sitemonitoring.entity.Product;
import com.sitemonitoring.sitemonitoring.repository.SalesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ResetSellsAndConsultingScheduler {

    private static final long ONE_MONTH = 864_000 * 3; // 10 days * 3

    private static final Logger log = LoggerFactory.getLogger(ResetSellsAndConsultingScheduler.class.getName());

    @Autowired
    private SalesRepository salesRepository;

    @Scheduled(fixedDelay = ONE_MONTH)
    public void reset() throws InterruptedException {

        log.info("One month has been passed resetting values ...");
        for (Product p : salesRepository.findAll()) {
            p.setBought(0);
            p.setVisits(0);
            salesRepository.save(p);
        }

    }
}