create table if not exists delivery_periods
(
    id    integer not null primary key,
    name  text    not null,
    "from" integer not null,
    "to" integer not null
);

create table if not exists goods
(
    id          uuid    not null primary key,
    title       text    not null,
    description text    not null,
    price       numeric not null,
    weight      integer,
    quantity    integer not null
);

create table if not exists baskets
(
    id                 uuid not null primary key,
    buyer_id           uuid not null,
    address_country    text,
    address_city       text,
    address_street     text,
    address_house      text,
    address_apartment  text,
    delivery_period_id integer references public.delivery_periods,
    status             varchar(50) not null,
    total              numeric not null
);

create index if not exists idx_baskets_delivery_period_id on public.baskets (delivery_period_id);

create table if not exists items
(
    id          uuid    not null primary key,
    position    integer not null,
    good_id     uuid    not null,
    quantity    integer not null,
    title       text    not null,
    description text    not null,
    price       numeric not null,
    basket_id   uuid    not null references public.baskets on delete cascade
);

create index if not exists idx_items_basket_id on public.items (basket_id);
