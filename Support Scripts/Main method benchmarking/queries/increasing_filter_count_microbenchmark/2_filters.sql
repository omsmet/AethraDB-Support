SELECT *
FROM increasing_filter_count_table
WHERE col1 < 959925191 -- Integer.MAX_VALUE * 0.2 ^ 0.5 --> Should give 20% of records = 0.8B
AND col2 < 959925191