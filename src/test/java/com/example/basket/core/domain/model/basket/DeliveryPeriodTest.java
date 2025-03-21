package com.example.basket.core.domain.model.basket;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryPeriodTest {

    @Test
    void getById_should_returnDeliveryPeriodWithGivenId() {
        var period = DeliveryPeriod.getById(DeliveryPeriod.MORNING.getId());

        assertThat(period).isEqualTo(DeliveryPeriod.MORNING);
    }

    @Test
    void getByName_should_returnDeliveryPeriodWithGivenName() {
        var period = DeliveryPeriod.getByName(DeliveryPeriod.DAY.getName());

        assertThat(period).isEqualTo(DeliveryPeriod.DAY);
    }
}
