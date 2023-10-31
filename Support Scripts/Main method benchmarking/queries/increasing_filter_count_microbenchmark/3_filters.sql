SELECT *
FROM increasing_filter_count_table
WHERE col1 < 1255856056 -- Integer.MAX_VALUE * 0.2 ^ (1/3) --> Should give 20% of records = 0.8B
AND col2 < 1255856056
AND col3 < 1255856056