# Definitions of benchmarks in terms of
#   - The datasets to execute over
#   - The scale factors to execute
#   - The query file to use
#   - The paradigms to benchmark
#   - Whether to summarise the result as a count of the number of rows in the result

benchmarks = {
    'filter_query': {
        'datasets': [
            'filter_query_int_rest_1',
            'filter_query_int_rest_2',
            'filter_query_int_rest_3',
        ],
        'scale_factors': [
            1,
            10,
            20,
        ],
        'query_file': 'filter_query.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': False,
    },

    'aggregation_query': {
        'datasets': [
            'aggregation_query_int_keys_2',
            'aggregation_query_int_keys_16',
            'aggregation_query_int_keys_8192',
            'aggregation_query_int_keys_262144',
            'aggregation_query_int_keys_524288',
        ],
        'scale_factors': [
            1,
            10,
            20,
        ],
        'query_file': 'aggregation_query.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': True,
    },

    'join_query': {
        'datasets': [
            'join_query_int_B_0.6_C_0.8',
        ],
        'scale_factors': [
            1,
            10,
            #20,
        ],
        'query_file': 'join_query.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': True,
    },

    'tpch_q1': {
        'datasets': [
            'tpch',
        ],
        'scale_factors': [
            1,
            10,
            100,
        ],
        'query_file': 'tpch_q1_no_sort.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': True,
    },

    'tpch_q3': {
        'datasets': [
            'tpch',
        ],
        'scale_factors': [
            1,
            10,
            100,
        ],
        'query_file': 'tpch_q3_no_sort.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': True,
    },

    'tpch_q6': {
        'datasets': [
            'tpch',
        ],
        'scale_factors': [
            1,
            10,
            100,
        ],
        'query_file': 'tpch_q6.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': False,
    },

    'tpch_q10': {
        'datasets': [
            'tpch',
        ],
        'scale_factors': [
            1,
            10,
            100,
        ],
        'query_file': 'tpch_q10_no_sort.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': True,
    },
}