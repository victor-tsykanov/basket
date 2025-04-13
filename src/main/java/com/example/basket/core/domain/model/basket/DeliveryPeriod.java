package com.example.basket.core.domain.model.basket;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class DeliveryPeriod {
    private int id;
    private String name;
    private int from;
    private int to;

    public static final DeliveryPeriod MORNING = new DeliveryPeriod(1, "morning", 6, 12);
    public static final DeliveryPeriod MIDDAY = new DeliveryPeriod(2, "midday", 12, 17);
    public static final DeliveryPeriod EVENING = new DeliveryPeriod(3, "evening", 17, 24);
    public static final DeliveryPeriod NIGHT = new DeliveryPeriod(4, "night", 0, 6);
    public static final List<DeliveryPeriod> LIST = List.of(MORNING, MIDDAY, EVENING, NIGHT);

    public static DeliveryPeriod getById(int id) {
        return LIST
                .stream()
                .filter(p -> p.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Delivery period with id=" + id + " does not exist"));
    }

    public static DeliveryPeriod getByName(String name) {
        return LIST
                .stream()
                .filter(p -> p.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Delivery period with name=" + name + " does not exist"));
    }

    public static DeliveryPeriod restore(Integer id, String name, int from, int to) {
        return new DeliveryPeriod(id, name, from, to);
    }
}
