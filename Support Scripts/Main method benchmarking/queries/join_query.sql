SELECT A.col2
FROM table_A A
    INNER JOIN table_B B on B.col1 = A.col1 
    INNER JOIN table_C C on A.col2 = C.col1