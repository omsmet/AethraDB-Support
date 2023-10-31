SELECT sum(col1), sum(col2), sum(col3), sum(col4), sum(col5), sum(((col1 + col2) - col3) * col4 - col5)
FROM arithmetic_table