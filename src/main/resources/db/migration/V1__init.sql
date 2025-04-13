create table delivery_periods
(
    id    integer not null primary key,
    name  text    not null,
    "from" integer not null,
    "to" integer not null
);

create table goods
(
    id          uuid    not null primary key,
    title       text    not null,
    description text    not null,
    price       numeric not null,
    weight      integer,
    quantity    integer not null
);

create table baskets
(
    id                 uuid not null primary key,
    buyer_id           uuid not null,
    address_country    text,
    address_city       text,
    address_street     text,
    address_house      text,
    address_apartment  text,
    delivery_period_id integer references delivery_periods,
    status             varchar(50) not null,
    total              numeric not null
);

create index idx_baskets_delivery_period_id on baskets (delivery_period_id);

create table items
(
    id          uuid    not null primary key,
    position    integer not null,
    good_id     uuid    not null,
    quantity    integer not null,
    title       text    not null,
    description text    not null,
    price       numeric not null,
    basket_id   uuid    not null references baskets on delete cascade
);

create index idx_items_basket_id on items (basket_id);
