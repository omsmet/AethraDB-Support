select sum(lo_revenue), d_year, p_brand1
from part, lineorder, supplier, datedef
where p_category = 'MFGR#12'
and p_partkey = lo_partkey
and s_region = 'AMERICA     '
and lo_suppkey = s_suppkey
and lo_orderdate = d_datekey
group by d_year, p_brand1
-- order by d_year, p_brand1