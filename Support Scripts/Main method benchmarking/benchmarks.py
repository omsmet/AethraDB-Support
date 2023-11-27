# Definitions of benchmarks in terms of
#   - The datasets to execute over
#   - The scale factors to execute
#   - The query file to use
#   - The paradigms to benchmark
#   - Whether to summarise the result as a count of the number of rows in the result

benchmarks = {
    # 'filter_query': {
    #     'datasets': [
    #         'filter_query_int_rest_1',
    #         'filter_query_int_rest_2',
    #         'filter_query_int_rest_3',
    #     ],
    #     'scale_factors': [
    #         1,
    #         10,
    #         20,
    #     ],
    #     'query_file': 'filter_query.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised",
    #     ],
    #     'summarise': False,
    # },

    # 'aggregation_query': {
    #     'datasets': [
    #         'aggregation_query_int_keys_2',
    #         'aggregation_query_int_keys_16',
    #         'aggregation_query_int_keys_8192',
    #         'aggregation_query_int_keys_262144',
    #         'aggregation_query_int_keys_524288',
    #     ],
    #     'scale_factors': [
    #         1,
    #         10,
    #         20,
    #     ],
    #     'query_file': 'aggregation_query.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised",
    #     ],
    #     'summarise': True,
    # },

    # 'join_query': {
    #     'datasets': [
    #         'join_query_int_B_0.6_C_0.8',
    #     ],
    #     'scale_factors': [
    #         1,
    #         10,
    #         #20,
    #     ],
    #     'query_file': 'join_query.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised",
    #     ],
    #     'summarise': True,
    # },

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

    'ssb_q1_1': {
        'datasets': [
            'SSB',
        ],
        'scale_factors': [
            1,
            10,
            100,
        ],
        'query_file': 'ssb_q1_1.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': False,
    },

    'ssb_q1_2': {
        'datasets': [
            'SSB',
        ],
        'scale_factors': [
            1,
            10,
            100,
        ],
        'query_file': 'ssb_q1_2.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': False,
    },

    'ssb_q1_3': {
        'datasets': [
            'SSB',
        ],
        'scale_factors': [
            1,
            10,
            100,
        ],
        'query_file': 'ssb_q1_3.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': False,
    },

    'ssb_q2_1': {
        'datasets': [
            'SSB',
        ],
        'scale_factors': [
            1,
            10,
            100,
        ],
        'query_file': 'ssb_q2_1_no_sort.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': True,
    },

    'ssb_q2_3': {
        'datasets': [
            'SSB',
        ],
        'scale_factors': [
            1,
            10,
            100,
        ],
        'query_file': 'ssb_q2_3_no_sort.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': True,
    },

    'ssb_q3_1': {
        'datasets': [
            'SSB',
        ],
        'scale_factors': [
            1,
            10,
            100,
        ],
        'query_file': 'ssb_q3_1_no_sort.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': True,
    },

    'ssb_q3_2': {
        'datasets': [
            'SSB',
        ],
        'scale_factors': [
            1,
            10,
            100,
        ],
        'query_file': 'ssb_q3_2_no_sort.sql',
        'paradigms': [
            "non-vectorised",
            "vectorised",
        ],
        'summarise': True,
    },

    # 'increasing_filter_count_1_filters': {
    #     'datasets': [
    #         'increasing_filter_count',
    #     ],
    #     'scale_factors': [
    #         75
    #     ],
    #     'query_file': 'increasing_filter_count_microbenchmark/1_filters.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised"
    #     ],
    #     'summarise': True,
    # },

    # 'increasing_filter_count_2_filters': {
    #     'datasets': [
    #         'increasing_filter_count',
    #     ],
    #     'scale_factors': [
    #         75
    #     ],
    #     'query_file': 'increasing_filter_count_microbenchmark/2_filters.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised"
    #     ],
    #     'summarise': True,
    # },

    # 'increasing_filter_count_3_filters': {
    #     'datasets': [
    #         'increasing_filter_count',
    #     ],
    #     'scale_factors': [
    #         75
    #     ],
    #     'query_file': 'increasing_filter_count_microbenchmark/3_filters.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised"
    #     ],
    #     'summarise': True,
    # },

    # 'increasing_filter_count_4_filters': {
    #     'datasets': [
    #         'increasing_filter_count',
    #     ],
    #     'scale_factors': [
    #         75
    #     ],
    #     'query_file': 'increasing_filter_count_microbenchmark/4_filters.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised"
    #     ],
    #     'summarise': True,
    # },

    # 'increasing_filter_count_5_filters': {
    #     'datasets': [
    #         'increasing_filter_count',
    #     ],
    #     'scale_factors': [
    #         75
    #     ],
    #     'query_file': 'increasing_filter_count_microbenchmark/5_filters.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised"
    #     ],
    #     'summarise': True,
    # },

    # 'increasing_arithmetic_1_ops': {
    #     'datasets': [
    #         'arithmetic_table',
    #     ],
    #     'scale_factors': [
    #         75
    #     ],
    #     'query_file': 'increasing_arithmetic_microbenchmark/1_ops.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised"
    #     ],
    #     'summarise': False,
    # },

    # 'increasing_arithmetic_2_ops': {
    #     'datasets': [
    #         'arithmetic_table',
    #     ],
    #     'scale_factors': [
    #         75
    #     ],
    #     'query_file': 'increasing_arithmetic_microbenchmark/2_ops.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised"
    #     ],
    #     'summarise': False,
    # },

    # 'increasing_arithmetic_3_ops': {
    #     'datasets': [
    #         'arithmetic_table',
    #     ],
    #     'scale_factors': [
    #         75
    #     ],
    #     'query_file': 'increasing_arithmetic_microbenchmark/3_ops.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised"
    #     ],
    #     'summarise': False,
    # },

    # 'increasing_arithmetic_4_ops': {
    #     'datasets': [
    #         'arithmetic_table',
    #     ],
    #     'scale_factors': [
    #         75
    #     ],
    #     'query_file': 'increasing_arithmetic_microbenchmark/4_ops.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised"
    #     ],
    #     'summarise': False,
    # },

    # 'increasing_filter_count_100_cols_1_filters': {
    #     'datasets': [
    #         'increasing_filter_count_100_cols',
    #     ],
    #     'scale_factors': [
    #         75
    #     ],
    #     'query_file': 'increasing_filter_count_microbenchmark_100_cols/1_filters.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised"
    #     ],
    #     'summarise': True,
    # },

    # 'increasing_filter_count_100_cols_100_filters': {
    #     'datasets': [
    #         'increasing_filter_count_100_cols',
    #     ],
    #     'scale_factors': [
    #         75
    #     ],
    #     'query_file': 'increasing_filter_count_microbenchmark_100_cols/100_filters.sql',
    #     'paradigms': [
    #         "non-vectorised",
    #         "vectorised"
    #     ],
    #     'summarise': True,
    # },

}