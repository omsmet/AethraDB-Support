# Dataset paths based on dataset name, scale factor -> containing folder
datasets = {
    'filter_query_int_rest_1': {
        1: '/nvtmp/AethraTestData/filter_query_int/arrow_col1_002_col2_098_col3_098',
        10: '/nvtmp/AethraTestData/filter_query_int_sf10/arrow_col1_002_col2_098_col3_098',
        20: '/nvtmp/AethraTestData/filter_query_int_sf20/arrow_col1_002_col2_098_col3_098',
    },

    'filter_query_int_rest_2': {
        1: '/nvtmp/AethraTestData/filter_query_int/arrow_col1_098_col2_002_col3_098',
        10: '/nvtmp/AethraTestData/filter_query_int_sf10/arrow_col1_098_col2_002_col3_098',
        20: '/nvtmp/AethraTestData/filter_query_int_sf20/arrow_col1_098_col2_002_col3_098',
    },

    'filter_query_int_rest_3': {
        1: '/nvtmp/AethraTestData/filter_query_int/arrow_col1_098_col2_098_col3_002',
        10: '/nvtmp/AethraTestData/filter_query_int_sf10/arrow_col1_098_col2_098_col3_002',
        20: '/nvtmp/AethraTestData/filter_query_int_sf20/arrow_col1_098_col2_098_col3_002',
    },

    'aggregation_query_int_keys_2': {
        1: '/nvtmp/AethraTestData/aggregation_query_int/arrow_size_31457280_keys_2',
        10: '/nvtmp/AethraTestData/aggregation_query_int_sf10/arrow_size_314572800_keys_2',
        20: '/nvtmp/AethraTestData/aggregation_query_int_sf20/arrow_size_629145600_keys_2',
    },

    'aggregation_query_int_keys_16': {
        1: '/nvtmp/AethraTestData/aggregation_query_int/arrow_size_31457280_keys_16',
        10: '/nvtmp/AethraTestData/aggregation_query_int_sf10/arrow_size_314572800_keys_16',
        20: '/nvtmp/AethraTestData/aggregation_query_int_sf20/arrow_size_629145600_keys_16',
    },

    'aggregation_query_int_keys_8192': {
        1: '/nvtmp/AethraTestData/aggregation_query_int/arrow_size_31457280_keys_8192',
        10: '/nvtmp/AethraTestData/aggregation_query_int_sf10/arrow_size_314572800_keys_8192',
        20: '/nvtmp/AethraTestData/aggregation_query_int_sf20/arrow_size_629145600_keys_8192',
    },

    'aggregation_query_int_keys_262144': {
        1: '/nvtmp/AethraTestData/aggregation_query_int/arrow_size_31457280_keys_262144',
        10: '/nvtmp/AethraTestData/aggregation_query_int_sf10/arrow_size_314572800_keys_262144',
        20: '/nvtmp/AethraTestData/aggregation_query_int_sf20/arrow_size_629145600_keys_262144',
    },

    'aggregation_query_int_keys_524288': {
        1: '/nvtmp/AethraTestData/aggregation_query_int/arrow_size_31457280_keys_524288',
        10: '/nvtmp/AethraTestData/aggregation_query_int_sf10/arrow_size_314572800_keys_524288',
        20: '/nvtmp/AethraTestData/aggregation_query_int_sf20/arrow_size_629145600_keys_524288',
    },

    'join_query_int_B_0.6_C_0.8': {
        1: '/nvtmp/AethraTestData/join_query_int/A_B_0.6_C_0.8',
        10: '/nvtmp/AethraTestData/join_query_int_sf10/A_B_0.6_C_0.8',
        20: '/nvtmp/AethraTestData/join_query_int_sf20/A_B_0.6_C_0.8',
    },

    'tpch': {
        1: '/nvtmp/AethraTestData/tpch/sf-1',
        10: '/nvtmp/AethraTestData/tpch/sf-10',
        100: '/nvtmp/AethraTestData/tpch/sf-100',
    },

    'SSB': {
        1: '/nvtmp/AethraTestData/SSB/sf-1',
        10: '/nvtmp/AethraTestData/SSB/sf-10',
        100: '/nvtmp/AethraTestData/SSB/sf-100',
    },

    'increasing_filter_count': {
        75: '/nvtmp/AethraTestData/increasing_filter_count_sf75',
    },

    'arithmetic_table': {
        75: '/nvtmp/AethraTestData/arithmetic_table_sf75',
    },
}