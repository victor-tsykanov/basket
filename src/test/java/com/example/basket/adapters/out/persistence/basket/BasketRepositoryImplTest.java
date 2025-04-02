package com.example.basket.adapters.out.persistence.basket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import com.example.basket.adapters.out.persistence.PersistenceTestsConfiguration;
import com.example.basket.adapters.out.persistence.basket.entities.ItemEntity;
import com.example.basket.adapters.out.persistence.basket.mappers.BasketMapper;
import com.example.basket.adapters.out.persistence.basket.repositories.SpringDataBasketRepository;
import com.example.basket.adapters.out.persistence.basket.repositories.SpringDataItemRepository;
import com.example.basket.common.exceptions.EntityExistsException;
import com.example.basket.common.exceptions.EntityNotFoundException;
import com.example.basket.core.domain.kernel.Discount;
import com.example.basket.core.domain.model.basket.DeliveryPeriod;
import com.example.basket.core.domain.model.good.Good;
import com.example.basket.core.ports.out.BasketRepository;
import com.example.basket.core.ports.out.GoodRepository;
import com.example.basket.fakers.AddressFaker;
import com.example.basket.fakers.BasketFaker;
import com.example.basket.fakers.GoodFaker;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(PersistenceTestsConfiguration.class)
class BasketRepositoryImplTest {
    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private GoodRepository goodRepository;

    @Autowired
    private SpringDataBasketRepository springDataBasketRepository;

    @Autowired
    private SpringDataItemRepository springDataItemRepository;

    @Autowired
    private BasketMapper basketMapper;

    @Autowired
    private JdbcClient jdbcClient;

    private static final BasketFaker basketFaker = new BasketFaker();
    private static final AddressFaker addressFaker = new AddressFaker();

    @BeforeEach
    void createGoods() {
        Goods.ALL.forEach(goodRepository::create);
    }

    @AfterEach
    void cleanUpBaskets() {
        Stream.of("baskets", "items", "goods").forEach(tableName -> {
            jdbcClient
                    .sql("truncate table %s cascade".formatted(tableName))
                    .update();
        });
    }

    @Test
    void get_should_returnBasket_when_basketExists() {
        // Arrange
        var basket = basketFaker.makeCreated();
        basket.change(Goods.CARROT, 1);
        basket.change(Goods.ORANGE, 2);
        basket.addAddress(addressFaker.make());
        basket.addDeliveryPeriod(DeliveryPeriod.DAY);

        var basketEntity = basketMapper.toJpaEntity(basket);
        springDataBasketRepository.save(basketEntity);

        // Act
        var basketFromRepository = basketRepository.get(basket.getId());

        // Assert
        assertThat(basketFromRepository).usingRecursiveComparison().isEqualTo(basket);
    }

    @Test
    void get_should_throwsException_when_basketDoesNotExist() {
        // Arrange
        var id = UUID.randomUUID();

        // Act
        assertThrows(
                EntityNotFoundException.class,
                () -> basketRepository.get(id)
        );
    }

    @Test
    void create_should_addBasket_when_basketDoesNotExist() {
        // Arrange
        var basket = basketFaker.makeCreated();
        basket.change(Goods.APPLE, 1);
        basket.change(Goods.ORANGE, 2);

        // Act
        basketRepository.create(basket);

        // Assert
        var optionalJpaEntity = springDataBasketRepository.findById(basket.getId());
        assertThat(optionalJpaEntity).isNotEmpty();

        var jpaEntity = optionalJpaEntity.get();
        assertThat(jpaEntity.getId()).isEqualTo(basket.getId());
        assertThat(jpaEntity.getBuyerId()).isEqualTo(basket.getBuyerId());
        assertThat(jpaEntity.getAddress()).isNull();
        assertThat(jpaEntity.getDeliveryPeriod()).isNull();
        assertThat(jpaEntity.getStatus()).isEqualTo(basket.getStatus().name());
        assertThat(jpaEntity.getTotal()).isEqualTo(basket.getTotal(), offset(0.001));
        assertThat(jpaEntity.getItems()).hasSize(2);

        assertThat(jpaEntity.getItems()).satisfiesExactly(
                goodWithQuantity(Goods.APPLE, 1),
                goodWithQuantity(Goods.ORANGE, 2)
        );
    }

    @Test
    void create_should_throwException_when_basketExists() {
        // Arrange
        var basket = basketFaker.makeCreated();
        basketRepository.create(basket);

        // Act
        assertThrows(
                EntityExistsException.class,
                () -> basketRepository.create(basket)
        );
    }

    @Test
    void update_should_saveBasket_when_basketExists() {
        // Arrange
        var basket = basketFaker.makeCreated();
        basket.change(Goods.APPLE, 1);
        basket.change(Goods.ORANGE, 2);
        basket.addDeliveryPeriod(DeliveryPeriod.EVENING);
        basket.addAddress(addressFaker.make());

        basketRepository.create(basket);

        basket.change(Goods.APPLE, 0);
        basket.change(Goods.ORANGE, 5);
        basket.change(Goods.CARROT, 1);
        basket.addDeliveryPeriod(DeliveryPeriod.DAY);
        basket.addAddress(addressFaker.make());
        basket.checkout(Discount.of(10));

        // Act
        basketRepository.update(basket);

        // Assert
        var optionalJpaEntity = springDataBasketRepository.findById(basket.getId());
        assertThat(optionalJpaEntity).isNotEmpty();

        var jpaEntity = optionalJpaEntity.get();
        assertThat(jpaEntity.getId()).isEqualTo(basket.getId());
        assertThat(jpaEntity.getBuyerId()).isEqualTo(basket.getBuyerId());

        var expectedStreet = Objects.requireNonNull(basket.getAddress()).getStreet();
        assertThat(jpaEntity.getAddress().getStreet()).isEqualTo(expectedStreet);

        var expectedPeriodName = Objects.requireNonNull(basket.getDeliveryPeriod()).getName();
        assertThat(jpaEntity.getDeliveryPeriod().getName()).isEqualTo(expectedPeriodName);

        assertThat(jpaEntity.getStatus()).isEqualTo(basket.getStatus().name());
        assertThat(jpaEntity.getTotal()).isEqualTo(basket.getTotal(), offset(0.001));
        assertThat(jpaEntity.getItems()).hasSize(2);

        assertThat(jpaEntity.getItems()).satisfiesExactly(
                goodWithQuantity(Goods.ORANGE, 5),
                goodWithQuantity(Goods.CARROT, 1)
        );

        assertThat(springDataItemRepository.count()).isEqualTo(2);
    }

    @Test
    void update_should_throwException_when_basketDoesNotExist() {
        // Arrange
        var basket = basketFaker.makeCreated();

        // Act
        assertThrows(
                EntityNotFoundException.class,
                () -> basketRepository.update(basket)
        );
    }

    static class Goods {
        static final GoodFaker goodFaker = new GoodFaker();

        static final Good APPLE = goodFaker.make();
        static final Good ORANGE = goodFaker.make();
        static final Good CARROT = goodFaker.make();

        static final List<Good> ALL = List.of(APPLE, ORANGE, CARROT);
    }

    static Consumer<ItemEntity> goodWithQuantity(Good good, int quantity) {
        return itemEntity -> {
            assertThat(itemEntity.getGoodId()).isEqualTo(good.getId());
            assertThat(itemEntity.getTitle()).isEqualTo(good.getTitle());
            assertThat(itemEntity.getDescription()).isEqualTo(good.getDescription());
            assertThat(itemEntity.getPrice()).isEqualTo(good.getPrice());
            assertThat(itemEntity.getQuantity()).isEqualTo(quantity);
        };
    }
}
