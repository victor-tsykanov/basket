package com.example.basket.adapters.out.persistence.basket.queries;

import com.example.basket.common.exceptions.EntityNotFoundException;
import com.example.basket.core.ports.in.getbasket.GetBasketQuery;
import com.example.basket.core.ports.in.getbasket.GetBasketQueryHandler;
import com.example.basket.core.ports.in.getbasket.GetBasketQueryResponse;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GetBasketQueryHandlerImpl implements GetBasketQueryHandler {
    private final JdbcClient jdbcClient;

    @Override
    public GetBasketQueryResponse handle(GetBasketQuery query) {
        var basket = jdbcClient
                .sql("""
                        select id, status, address_country, address_city, address_street, address_house, address_apartment
                        from baskets
                        where id = ?
                        """)
                .param(query.getId())
                .query(
                        (rs, rowNum) -> {
                            return new GetBasketQueryResponse(
                                    (UUID) rs.getObject("id"),
                                    rs.getString("status"),
                                    new GetBasketQueryResponse.Address(
                                            rs.getString("address_country"),
                                            rs.getString("address_city"),
                                            rs.getString("address_street"),
                                            rs.getString("address_house"),
                                            rs.getString("address_apartment")
                                    )
                            );
                        }
                )
                .optional();

        return basket.orElseThrow(
                () -> new EntityNotFoundException("Basket with id=%s is not found".formatted(query.getId()))
        );
    }
}
