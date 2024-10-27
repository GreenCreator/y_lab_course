package io.service;

import io.annotations.Loggable;
import io.domain.enums.HealthStatus;
@Loggable
public class HealthCheckService {
    public HealthStatus getHealthStatus() {
        try {
            Thread.sleep(100);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return HealthStatus.UP;
    }
}
