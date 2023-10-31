SELECT *
FROM increasing_filter_count_table
WHERE col1 < 429496730 -- Integer.MAX_VALUE / 5 --> Should give 20% of records = 0.8B