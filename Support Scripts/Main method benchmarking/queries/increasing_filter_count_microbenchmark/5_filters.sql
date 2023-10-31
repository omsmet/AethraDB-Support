SELECT *
FROM increasing_filter_count_table
WHERE col1 < 1556452476 -- Integer.MAX_VALUE * 0.2 ^ (1/5) --> Should give 20% of records = 0.8B
AND col2 < 1556452476
AND col3 < 1556452476
AND col4 < 1556452476
AND col5 < 1556452476