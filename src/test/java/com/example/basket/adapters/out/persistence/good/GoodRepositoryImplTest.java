package com.example.basket.adapters.out.persistence.good;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import com.example.basket.adapters.out.persistence.PersistenceTestsConfiguration;
import com.example.basket.adapters.out.persistence.good.mappers.GoodMapper;
import com.example.basket.adapters.out.persistence.good.repositories.SpringDataGoodRepository;
import com.example.basket.common.exceptions.EntityExistsException;
import com.example.basket.common.exceptions.EntityNotFoundException;
import com.example.basket.core.ports.out.GoodRepository;
import com.example.basket.fakers.GoodFaker;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(PersistenceTestsConfiguration.class)
class GoodRepositoryImplTest {
    @Autowired
    private GoodRepository goodRepository;

    @Autowired
    private SpringDataGoodRepository springDataGoodRepository;

    @Autowired
    private GoodMapper goodMapper;

    @Autowired
    private JdbcClient jdbcClient;

    private final GoodFaker goodFaker = new GoodFaker();

    @AfterEach
    void cleanUpDatabase() {
        jdbcClient.sql("truncate table goods").update();
    }

    @Test
    void get_should_returnGood_when_goodExists() {
        // Arrange
        var good = goodFaker.make();
        springDataGoodRepository.save(goodMapper.toJpaEntity(good));

        // Act
        var goodFromRepository = goodRepository.get(good.getId());

        // Assert
        assertThat(goodFromRepository).isEqualTo(good);
    }

    @Test
    void get_should_throwsException_when_goodDoesNotExist() {
        // Arrange
        var id = UUID.randomUUID();

        // Act
        assertThrows(
                EntityNotFoundException.class,
                () -> goodRepository.get(id)
        );
    }

    @Test
    void create_should_addGood_when_goodDoesNotExist() {
        // Arrange
        var good = goodFaker.make();

        // Act
        goodRepository.create(good);

        // Assert
        var optionalJpaEntity = springDataGoodRepository.findById(good.getId());
        assertThat(optionalJpaEntity).isNotEmpty();

        var jpaEntity = optionalJpaEntity.get();
        assertThat(jpaEntity.getId()).isEqualTo(good.getId());
        assertThat(jpaEntity.getTitle()).isEqualTo(good.getTitle());
        assertThat(jpaEntity.getDescription()).isEqualTo(good.getDescription());
        assertThat(jpaEntity.getPrice()).isEqualTo(good.getPrice());
        assertThat(jpaEntity.getWeight()).isEqualTo(good.getWeight().getValue());
        assertThat(jpaEntity.getQuantity()).isEqualTo(good.getQuantity());
    }


    @Test
    void create_should_throwException_when_goodExists() {
        // Arrange
        var good = goodFaker.make();
        goodRepository.create(good);

        // Act
        assertThrows(
                EntityExistsException.class,
                () -> goodRepository.create(good)
        );
    }

    @Test
    void update_should_saveGood_when_goodExists() {
        // Arrange
        var good = goodFaker.make();
        springDataGoodRepository.save(goodMapper.toJpaEntity(good));
        var newQuantity = good.getQuantity() + 1;
        good.changeStocks(newQuantity);

        // Act
        goodRepository.update(good);

        // Assert
        var jpaEntity = springDataGoodRepository.findById(good.getId()).get();
        assertThat(jpaEntity.getQuantity()).isEqualTo(newQuantity);
    }

    @Test
    void update_should_throwException_when_goodDoesNotExist() {
        // Arrange
        var good = goodFaker.make();

        // Act
        assertThrows(
                EntityNotFoundException.class,
                () -> goodRepository.update(good)
        );
    }
}
