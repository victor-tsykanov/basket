package com.example.basket.infrastructure.adapters.in.rest;


import com.example.basket.infrastructure.adapters.in.rest.api.BasketApi;
import com.example.basket.infrastructure.adapters.in.rest.dto.Address;
import com.example.basket.infrastructure.adapters.in.rest.dto.Basket;
import com.example.basket.infrastructure.adapters.in.rest.dto.ChangeItemRequest;
import com.example.basket.infrastructure.adapters.in.rest.mappers.GetBasketResponseMapper;
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
import com.example.basket.core.ports.in.testcheckout.TestCheckoutCommand;
import com.example.basket.core.ports.in.testcheckout.TestCheckoutCommandHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class BasketsController implements BasketApi {
    private final AddAddressCommandHandler addAddressCommandHandler;
    private final AddDeliveryPeriodCommandHandler addDeliveryPeriodCommandHandler;
    private final ChangeItemsCommandHandler changeItemsCommandHandler;
    private final CheckoutCommandHandler checkoutCommandHandler;
    private final TestCheckoutCommandHandler testCheckoutCommandHandler;
    private final GetBasketQueryHandler getBasketQueryHandler;
    private final GetBasketResponseMapper getBasketResponseMapper;

    @Override
    public ResponseEntity<Void> addAddress(UUID basketId, Address address) {
        var command = AddAddressCommand.of(
                basketId,
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getHouse(),
                address.getApartment()
        );
        addAddressCommandHandler.handle(command);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> addDeliveryPeriod(UUID basketId, String body) {
        var command = AddDeliveryPeriodCommand.of(basketId, body);
        addDeliveryPeriodCommandHandler.handle(command);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> changeItems(UUID basketId, ChangeItemRequest request) {
        var command = ChangeItemsCommand.of(
                basketId,
                request.getBuyerId(),
                request.getItem().getGoodId(),
                request.getItem().getQuantity()
        );
        changeItemsCommandHandler.handle(command);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> checkout(UUID basketId) {
        var command = CheckoutCommand.of(basketId);
        checkoutCommandHandler.handle(command);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Basket> getBasket(UUID basketId) {
        var query = GetBasketQuery.of(basketId);
        var basket = getBasketQueryHandler.handle(query);
        var basketDto = getBasketResponseMapper.toDto(basket);

        return ResponseEntity.ok(basketDto);
    }

    @Override
    public ResponseEntity<Void> testCheckout() {
        var command = TestCheckoutCommand.of();
        testCheckoutCommandHandler.handle(command);

        return ResponseEntity.ok().build();
    }
}
