select sum(lo_extendedprice * lo_discount) as revenue
from datedef, lineorder
where d_datekey = lo_orderdate
and d_yearmonthnum = 199401
and lo_discount between 4 and 6
and lo_quantity between 26 and 35