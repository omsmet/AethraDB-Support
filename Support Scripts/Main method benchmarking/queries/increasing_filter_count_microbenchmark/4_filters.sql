SELECT *
FROM increasing_filter_count_table
WHERE col1 < 1436108870 -- Integer.MAX_VALUE * 0.2 ^ (1/4) --> Should give 20% of records = 0.8B
AND col2 < 1436108870
AND col3 < 1436108870
AND col4 < 1436108870