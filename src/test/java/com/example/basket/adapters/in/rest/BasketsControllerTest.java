package com.example.basket.adapters.in.rest;

import com.example.basket.adapters.in.rest.dto.Address;
import com.example.basket.adapters.in.rest.dto.ChangeItemRequest;
import com.example.basket.adapters.in.rest.dto.Item;
import com.example.basket.adapters.in.rest.mappers.GetBasketResponseMapper;
import com.example.basket.common.exceptions.EntityExistsException;
import com.example.basket.common.exceptions.EntityNotFoundException;
import com.example.basket.core.ports.in.addaddress.AddAddressCommand;
import com.example.basket.core.ports.in.addaddress.AddAddressCommandHandler;
import com.example.basket.core.ports.in.adddeliveryperiod.AddDeliveryPeriodCommand;
import com.example.basket.core.ports.in.adddeliveryperiod.AddDeliveryPeriodCommandHandler;
import com.example.basket.core.ports.in.changeitems.ChangeItemsCommand;
import com.example.basket.core.ports.in.changeitems.ChangeItemsCommandHandler;
import com.example.basket.core.ports.in.chechout.CheckoutCommand;
import com.example.basket.core.ports.in.chechout.CheckoutCommandHandler;
import com.example.basket.core.ports.in.getbasket.GetBasketQuery;
import com.example.basket.core.ports.in.getbasket.GetBasketQueryHandler;
import com.example.basket.core.ports.in.getbasket.GetBasketQueryResponse;
import com.example.basket.core.ports.in.testcheckout.TestCheckoutCommand;
import com.example.basket.core.ports.in.testcheckout.TestCheckoutCommandHandler;
import com.example.basket.factories.AddressFactory;
import com.example.basket.factories.BasketFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@WebMvcTest(value = {
        BasketsController.class,
        GetBasketResponseMapper.class
})
@AutoConfigureMockMvc
class BasketsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddAddressCommandHandler addAddressCommandHandler;

    @MockitoBean
    private AddDeliveryPeriodCommandHandler addDeliveryPeriodCommandHandler;

    @MockitoBean
    private ChangeItemsCommandHandler changeItemsCommandHandler;

    @MockitoBean
    private CheckoutCommandHandler checkoutCommandHandler;

    @MockitoBean
    private TestCheckoutCommandHandler testCheckoutCommandHandler;

    @MockitoBean
    private GetBasketQueryHandler getBasketQueryHandler;

    @BeforeEach
    void initializeRestAssured() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @AfterEach
    void resetRestAssured() {
        RestAssuredMockMvc.reset();
    }

    @Test
    void addAddress_should_succeed_when_commandSucceeds() {
        var basket = BasketFactory.buildUnconfirmed();
        var address = AddressFactory.build();

        given()
                .body(
                        json(
                                new Address(
                                        address.getCountry(),
                                        address.getCity(),
                                        address.getStreet(),
                                        address.getHouse(),
                                        address.getApartment()
                                )
                        )
                )
                .when()
                .post("/api/v1/baskets/{basketId}/address/add", basket.getId())
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(addAddressCommandHandler).handle(
                AddAddressCommand.of(
                        basket.getId(),
                        address.getCountry(),
                        address.getCity(),
                        address.getStreet(),
                        address.getHouse(),
                        address.getApartment()
                )
        );
    }

    @Test
    void addAddress_should_returnNotFound_when_commandThrowsEntityNotFoundException() {
        var basketId = UUID.randomUUID();
        var address = AddressFactory.build();

        doThrow(new EntityNotFoundException("Basket not found"))
                .when(addAddressCommandHandler)
                .handle(any());

        given()
                .body(
                        json(
                                new Address(
                                        address.getCountry(),
                                        address.getCity(),
                                        address.getStreet(),
                                        address.getHouse(),
                                        address.getApartment()
                                )
                        )
                )
                .when()
                .post("/api/v1/baskets/{basketId}/address/add", basketId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(
                        "code", equalTo(404),
                        "message", equalTo("Basket not found")
                );
    }

    @Test
    void addAddress_should_returnConflict_when_commandThrowsEntityExistsException() {
        var basketId = UUID.randomUUID();
        var address = AddressFactory.build();

        doThrow(new EntityExistsException("Basket exists"))
                .when(addAddressCommandHandler)
                .handle(any());

        given()
                .body(
                        json(
                                new Address(
                                        address.getCountry(),
                                        address.getCity(),
                                        address.getStreet(),
                                        address.getHouse(),
                                        address.getApartment()
                                )
                        )
                )
                .when()
                .post("/api/v1/baskets/{basketId}/address/add", basketId)
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body(
                        "code", equalTo(409),
                        "message", equalTo("Basket exists")
                );
    }

    @Test
    void addAddress_should_returnUnprocessableEntity_when_commandThrowsIllegalArgumentException() {
        var basketId = UUID.randomUUID();
        var address = AddressFactory.build();

        doThrow(new IllegalArgumentException("Not valid"))
                .when(addAddressCommandHandler)
                .handle(any());

        given()
                .body(
                        json(
                                new Address(
                                        address.getCountry(),
                                        address.getCity(),
                                        address.getStreet(),
                                        address.getHouse(),
                                        address.getApartment()
                                )
                        )
                )
                .when()
                .post("/api/v1/baskets/{basketId}/address/add", basketId)
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(
                        "code", equalTo(422),
                        "message", equalTo("Not valid")
                );
    }

    @Test
    void addAddress_should_returnInternalServerError_when_commandThrowsException() {
        var basketId = UUID.randomUUID();
        var address = AddressFactory.build();

        doThrow(new RuntimeException("Boom!"))
                .when(addAddressCommandHandler)
                .handle(any());

        given()
                .body(
                        json(
                                new Address(
                                        address.getCountry(),
                                        address.getCity(),
                                        address.getStreet(),
                                        address.getHouse(),
                                        address.getApartment()
                                )
                        )
                )
                .when()
                .post("/api/v1/baskets/{basketId}/address/add", basketId)
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(
                        "code", equalTo(500),
                        "message", equalTo("Internal Server Error")
                );
    }

    @Test
    void addDeliveryPeriod_should_succeed_when_commandSucceeds() {
        var basketId = UUID.randomUUID();

        given()
                .body("morning")
                .when()
                .post("/api/v1/baskets/{basketId}/delivery-period/add", basketId)
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(addDeliveryPeriodCommandHandler).handle(
                AddDeliveryPeriodCommand.of(
                        basketId,
                        "morning"
                )
        );
    }

    @Test
    void changeItems_should_succeed_when_commandSucceeds() {
        var basketId = UUID.randomUUID();
        var buyerId = UUID.randomUUID();
        var goodId = UUID.randomUUID();
        var quantity = 5;

        given()
                .body(
                        json(
                                new ChangeItemRequest(
                                        buyerId,
                                        new Item(goodId, quantity)
                                )
                        )
                )
                .when()
                .post("/api/v1/baskets/{basketId}/items/change", basketId)
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(changeItemsCommandHandler).handle(
                ChangeItemsCommand.of(
                        basketId,
                        buyerId,
                        goodId,
                        quantity
                )
        );
    }

    @Test
    void checkout_should_succeed_when_commandSucceeds() {
        var basketId = UUID.randomUUID();

        given()
                .when()
                .post("/api/v1/baskets/{basketId}/checkout", basketId)
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(checkoutCommandHandler).handle(
                CheckoutCommand.of(basketId)
        );
    }

    @Test
    void getBasket_should_returnBasket_when_querySucceeds() {
        var basket = BasketFactory.buildReadyForCheckout();
        var address = Objects.requireNonNull(basket.getAddress());
        var deliveryPeriod = Objects.requireNonNull(basket.getDeliveryPeriod());
        var item = Objects.requireNonNull(basket.getItems().getFirst());
        var query = GetBasketQuery.of(basket.getId());

        when(getBasketQueryHandler.handle(query))
                .thenReturn(
                        new GetBasketQueryResponse(
                                basket.getId(),
                                basket.getStatus().name(),
                                deliveryPeriod.getName(),
                                new GetBasketQueryResponse.Address(
                                        address.getCountry(),
                                        address.getCity(),
                                        address.getStreet(),
                                        address.getHouse(),
                                        address.getApartment()
                                ),
                                List.of(
                                        new GetBasketQueryResponse.Item(
                                                item.getId(),
                                                item.getGoodId(),
                                                item.getQuantity()
                                        )
                                )
                        )
                );

        given()
                .when()
                .get("/api/v1/baskets/{basketId}", basket.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "id", equalTo(basket.getId().toString()),
                        "address.country", equalTo(address.getCountry()),
                        "address.city", equalTo(address.getCity()),
                        "address.street", equalTo(address.getStreet()),
                        "address.house", equalTo(address.getHouse()),
                        "address.apartment", equalTo(address.getApartment()),
                        "deliveryPeriod", equalTo(deliveryPeriod.getName()),
                        "status", equalTo(basket.getStatus().name().toLowerCase(Locale.ENGLISH)),
                        "items.size()", equalTo(basket.getItems().size()),
                        "items[0].Id", equalTo(item.getId().toString()),
                        "items[0].goodId", equalTo(item.getGoodId().toString()),
                        "items[0].quantity", equalTo(item.getQuantity())
                );

        verify(getBasketQueryHandler).handle(query);
    }

    @Test
    void testCheckout_should_succeed_when_commandSucceeds() {
        given()
                .when()
                .post("/api/v1/baskets/new/test-checkout")
                .then()
                .statusCode(HttpStatus.OK.value());

        verify(testCheckoutCommandHandler).handle(
                TestCheckoutCommand.of()
        );
    }

    private static String json(Object object) {
        var writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

        try {
            return writer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private MockMvcRequestSpecification given() {
        return RestAssuredMockMvc.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }
}
