SELECT *
FROM increasing_filter_count_table
WHERE col0 < 214748365 -- Integer.MAX_VALUE / 10 --> Should give 10% of records = 20M