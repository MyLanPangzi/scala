CREATE TABLE `user_visit_action`
(
    `date`               string,
    `user_id`            bigint,
    `session_id`         string,
    `page_id`            bigint,
    `action_time`        string,
    `search_keyword`     string,
    `click_category_id`  bigint,
    `click_product_id`   bigint,
    `order_category_ids` string,
    `order_product_ids`  string,
    `pay_category_ids`   string,
    `pay_product_ids`    string,
    `city_id`            bigint
)
    row format delimited fields terminated by '\t';
load data local inpath 'input/user_visit_action.txt' into table user_visit_action;

CREATE TABLE `product_info`
(
    `product_id`   bigint,
    `product_name` string,
    `extend_info`  string
)
    row format delimited fields terminated by '\t';
load data local inpath 'input/product_info.txt' into table product_info;

CREATE TABLE `city_info`
(
    `city_id`   bigint,
    `city_name` string,
    `area`      string
)
    row format delimited fields terminated by '\t';
load data local inpath 'input/city_info.txt' into table city_info;
select *
from user_visit_action;
select *
from product_info;
select *
from city_info
limit 10;
-- 需求：各区域热门商品 Top3
-- 地区	商品名称	点击次数	城市备注
select *
from (
         select area, product_name, clicks, rank() over (PARTITION BY area order by clicks desc ) rank
         from (
                  select area, product_name, count(*) clicks
                  from user_visit_action u
                           inner join city_info ci on u.city_id = ci.city_id
                           inner join product_info p on u.click_product_id = p.product_id
                  where click_product_id != -1
                  group by area, product_name
                  order by clicks
              ) t
     ) t
where t.rank <= 3;

select t1.area, t1.product_name, t1.city_name, t1.clicks, t2.clicks, t1.clicks / t2.clicks as percent
from (
         select area, city_name, product_name, count(*) clicks
         from user_visit_action u
                  inner join city_info ci on u.city_id = ci.city_id
                  inner join product_info p on u.click_product_id = p.product_id
         group by area, product_name, city_name
         order by clicks
     ) t1
         inner join (
    select *
    from (
             select area, product_name, clicks, rank() over (PARTITION BY area order by clicks desc ) rank
             from (
                      select area, product_name, city_name, count(*) clicks
                      from user_visit_action u
                               inner join city_info ci on u.city_id = ci.city_id
                               inner join product_info p on u.click_product_id = p.product_id
                      where click_product_id != -1
                      group by area, product_name, city_name
                      order by clicks
                  ) t
         ) t
    where t.rank <= 3
) t2 on t1.area = t2.area and t1.product_name = t2.product_name;
select t.area,t.product_name,sum(clicks),cityMark(t.city_name)
from (
         select area, product_name, count(*),cityMark(city_name)
         from user_visit_action u
                  inner join city_info ci on u.city_id = ci.city_id
                  inner join product_info p on u.click_product_id = p.product_id
         where click_product_id != -1
         group by area, product_name, city_name
     )t
group by t.area,t.product_name;