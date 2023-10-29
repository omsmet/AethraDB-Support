select c_city, s_city, d_year, sum(lo_revenue) as revenue
from customer, lineorder, supplier, datedef
where c_nation = 'UNITED STATES  '
and c_custkey = lo_custkey
and lo_suppkey = s_suppkey
and s_nation = 'UNITED STATES  '
and lo_orderdate = d_datekey
and d_year >= 1992 and d_year <= 1997
group by c_city, s_city, d_year
-- order by d_year asc, revenue desc;