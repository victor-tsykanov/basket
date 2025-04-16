package com.example.basket.adapters.out.grpc.discount;

import com.example.basket.core.domain.kernel.Discount;
import com.example.basket.core.domain.model.basket.Basket;
import com.example.basket.core.ports.out.DiscountClient;
import com.example.discount.Contract;
import com.example.discount.Contract.GetDiscountRequest;
import com.example.discount.DiscountGrpc;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DiscountClientImpl implements DiscountClient {
    private final String discountServiceGrpcAddress;

    public DiscountClientImpl(@Value("${discount_service.grpc.address}") String discountServiceGrpcAddress) {
        this.discountServiceGrpcAddress = discountServiceGrpcAddress;
    }

    @Override
    public Discount getDiscount(Basket basket) {
        var channel = ManagedChannelBuilder
                .forTarget(discountServiceGrpcAddress)
                .usePlaintext()
                .build();

        var stub = DiscountGrpc.newBlockingStub(channel);

        var items = basket
                .getItems()
                .stream()
                .map(
                        item -> Contract.Item.newBuilder()
                                .setId(item.getId().toString())
                                .build()
                )
                .toList();
        var request = GetDiscountRequest
                .newBuilder()
                .addAllItems(items)
                .build();

        var response = stub.getDiscount(request);
        channel.shutdown();

        return Discount.of(response.getValue());
    }
}
