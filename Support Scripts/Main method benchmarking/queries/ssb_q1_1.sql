select sum(lo_extendedprice * lo_discount) as revenue
from datedef, lineorder
where d_datekey = lo_orderdate
and d_year = 1993
and lo_discount between 1 and 3
and lo_quantity < 25