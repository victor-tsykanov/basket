package com.example.basket.infrastructure.adapters.out.persistence.basket.queries;

import com.example.basket.common.exceptions.EntityNotFoundException;
import com.example.basket.core.ports.in.getbasket.GetBasketQuery;
import com.example.basket.core.ports.in.getbasket.GetBasketQueryHandler;
import com.example.basket.core.ports.in.getbasket.GetBasketQueryResponse;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetBasketQueryHandlerImpl implements GetBasketQueryHandler {
    private final JdbcClient jdbcClient;

    @Override
    public GetBasketQueryResponse handle(GetBasketQuery query) {
        var items = getItems(query.getId());

        var basket = jdbcClient
                .sql("""
                        select
                            b.id,
                            b.status,
                            b.address_country,
                            b.address_city,
                            b.address_street,
                            b.address_house,
                            b.address_apartment,
                            d.name as "delivery_period_name"
                        from baskets b
                        left join delivery_periods as d on b.delivery_period_id = d.id
                        where b.id = ?
                        """)
                .param(query.getId())
                .query(
                        (rs, rowNum) -> {
                            return new GetBasketQueryResponse(
                                    (UUID) rs.getObject("id"),
                                    rs.getString("status"),
                                    rs.getString("delivery_period_name"),
                                    new GetBasketQueryResponse.Address(
                                            rs.getString("address_country"),
                                            rs.getString("address_city"),
                                            rs.getString("address_street"),
                                            rs.getString("address_house"),
                                            rs.getString("address_apartment")
                                    ),
                                    items
                            );
                        }
                )
                .optional();

        return basket.orElseThrow(
                () -> new EntityNotFoundException("Basket with id=%s is not found".formatted(query.getId()))
        );
    }

    private List<GetBasketQueryResponse.Item> getItems(UUID basketId) {
        return jdbcClient
                .sql("""
                        select id, good_id, quantity
                        from items
                        where basket_id = ?
                        order by position
                        """)
                .param(basketId)
                .query(
                        (rs, rowNum) -> new GetBasketQueryResponse.Item(
                                (UUID) rs.getObject("id"),
                                (UUID) rs.getObject("good_id"),
                                rs.getInt("quantity")
                        )
                )
                .list();
    }
}
