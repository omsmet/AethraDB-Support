# Notes on the results of all query runs when compared against DuckDB performance
In this document, I make a few observations regarding the results of the Filter, Aggregation and Join query runs which are present in this folder.

## Notes on the Filter Query
Both the query plan, as well as the generated code are "as you would expect", so no surprises here.
When looking at the result graphs, we see that all our implementations are always faster than the DuckDB implementation.

Observations: 
- The fastest overall implementation is the non-simded vectorised implementation when the first column is restrictive
    - This is in contrast to our old (less efficient) vectorised implementation based on hand-written classes, where Non-Vectorised + SIMD was fastest
    - But: it seems all "generated" implementations are always faster than their hand-written counterparts
- For the non-vectorised implementations, the SIMD-ed version is always faster than the scalar non-vectorised implementation
    - This is not always the case for the vectorised implementations
- SIMD enhances predictability of performance, but need not be faster than the scalar implementation
    - But: we are not making full use of SIMD performance for the non-vectorised SIMD implementation: SIMD vector length is such that all vectors are always of the same length and thus determined by the largest data type. (In this case, we only use half of the optimal length for the integer experiment)

## Notes on the Aggregation Query
Both the query plan, as well as the generated code are "as you would expect", again no surprises.
For the implementation, we rely on a "key" iterator over the used aggregation map. This might be inefficient and it might be more effective to simply iterate over the key array itself. We can check the profiling information discussed below.

Observations on the result (Varying number of keys):
- There are non-negligible performance differences between the "no-verification" and "with-verification" runs
    - Surprisingly, the "with verification" runs seem to be faster in these cases
    - Differences maily occur in the non-vectorised with SIMD implementations, in other cases, results seem similar
    - Difference might lie in the fact that the "with-verification" runs create result arrays which are each consumed by a single function call, while the "without-verification" runs have each result value (as in each number in the result) consumed by a single function call.
    - So: for the remaining discussion we look at the "With Verification" run results
- We see that for the aggregation query, most implementations perform quite similarly
    - But: for keys <= 65536 we see that the vectorised SIMD implementation is usually slowest
    - While for keys >= 131072 the non-vectorised, non-SIMD implementation is usually slowest
- SIMD only becomes beneficial for keys >= 131072, especially for the non-vectorised implementation
    - Here, the optimal number of SIMD elements is actually used, since we need to convert the integer SIMD vectors to long SIMD vectors anyhow
    - But: the conversion allows to specify "parts", which we currently do not use, which might make the computation faster still
    - Alternatively: SIMD is not beneficial, but for keys >= 131072 the running-time of non-vec, non-simd explodes
- For the number of keys <= 262144, we see that most implementations are within a factor 2 of the DuckDB performance (and usually quite a lot closer)

Observations on the result (Varying the key skew):
- Keys are skewed using a zipfian distribution. Higher skew parameter means more key skew. Skew parameter of 0 means "uniform distribution".
- As key skew increases, our performance becomes closer to the performance of duckdb
- As key skew increases, the non-vectorised non-simd performance becomes closer to the other implementations, which all perform about equally
    - Perhaps this means that especially the non-vectorised, non-simd implementation suffers from the memory access patterns? (Idea that more skew decreases random access to hash-table/makes the hash-table access pattern more predictable)
- Once more: significant performance differences between the non-vectorised but SIMDed query implementation between the "verifying" and "non-verifying" runs.

Room for SIMD optimisation potential is still left:
- The fromArray and toArray SIMD calls support index maps, so we might be able to accellerate the `addToKeyOrPutIfNotExist` calls in a SIMD fashion by performing parallel memory loads, additions and memory stores.
- But: currently the hash-map seems to be the inefficient part

### XPS Performance vs R440 Performance
Incidentally, I made the observation that the performance of the aggregation query implementations is much much higher on my local Dell XPS, compared to the Dell R440 benchmark machine, even though both machines seem to run the query software on approximately the same CPU frequency. 
Not only are the running-times much lower on the Dell XPS, they are also closer to DuckDB in a relative sense on that same machine. (See `Aggregation Query Performance/`, preliminary result images, which result from the `Aethra vs Duck Local` analysis in the previous benchmark run)

After some performanc einvestigations, we notice that:
- The number of page-faults on the benchmark machine is much higher: about 3.3x higher than on my local machine
- The number of instructions per cycle is much lower on the benchmark machine: about 3.3x lower than on the local machine
- I thus wonder if the CPU of my benchmark machine is actually stalled by waiting for memory accesses to complete
- Additonally, the relative number of cache misses is also about twice as high on the benchmark machine compared to the local machine (34.450% of all cache references, compared to 17.029% of all cache references)

Especially the latter is a bit surprising since both machines should have similar cache sizes.
The difference might, however, lie in the memory speed difference between the machines:
- The XPS has 64 GB of Synchronous DDR4 (3200MHz, 0.3ns) split over 2 modules
- The R440 has 64GB of Buffered Registered Synchronous DDR4 (2400MHz, 0.4ns) split over 4 modules

The file `Aggregation Query Performance/R440-vs-XPS/Comparison.ods` shows a performance comparison of the two machines over different benchmarks:
- For CPU heavy benchmarks, we see that the performance difference between the two machines is not that big
    - Exception: in the Non-vectorised, SIMDed filter query, the R440 significantly outperforms the XPS
    - This makes sense: the R440 has AVX512, compared to the AVX2 of the XPS (which is very much under-utilised in this case, due to the fact that vector length is capped to always fit long vectors too: AVX2 effectively becomes "AVX1", while AVX512 degrades to "AVX2")
- But, for the aggregation query we see that the XPS is usually more than 2x faster than the R440

### Investigating bottlenecks of the Aggregation Query
We can investigate the flamegraph provided in the `Aggregation Query Performance/Aggregation_query...` folder (created with the JFR profile option of JMH, can load this file in IntelliJ).
We make the following observations:
- Most of the time is spent in `getSimpleIntLongMap()` of the `BufferPoolAllocationManager`, which results in a `Simple_Int_Long_Map.<init>` call.
    - This is effectively a constructor invocation, which should NOT occur since the buffer pool allocation manager has a set of pre-allocated maps.
    - Even though the branch containing the allocation should not run, if we comment out the replenishing code that allocates more maps if we run out, the init call disappears from the flame graph, but the running-time remains exactly the same.
    - However, adding a print statement to the "replenishing branch" allows us to verify the branch is never run.
    - It seems something suspicious is going on here, or that the profiler is unable to provide accurate results.

## Notes on the Join Query
The first note on the join query is that Calcite generates a very stupid plan for the query:
- The query only needs to access columns A.col1, A.col2, B.col1 and C.col1, and could thus project out these columns during the scan
- Instead, the query plan does not project out these columns, but instead instantiates the full join result [A.col1, A.col2, A.col3, B.col1, B.col2, B.col3, C.col1, C.col2, C.col3].
- This means that a lot more data is "moved around" than strictly necessary
- In contrast, the DuckDB profiling output shows that DuckDB is clever enought to perform this projection during the table scan.
- In my opinion it thus makes sense to investigate how much performance we can save by doing the projection during the scan.

Now, regarding the results:
- We can see that the performance of all our engines is approximately the same
    - Not a surprise: all implementations are very similar to the row-based implementation since we need to create a row-based hash-table
        - Differences in vectorised implementation
            - Pre-hashing in a vectorised fashion, instead of on a per-entry basis
            - Result is exposed as vectors instead of scalar variables
        - SIMD implementation differences:
            - Only the pre-hash computation is done via SIMD accelleration
            - Note the SIMD integer to long vector conversion mentioned in the aggregation query
- Interestingly enough, the non-SIMDed vectorised implementation seems to be fastest in most cases
- SIMD is always slower than the non-SIMD counterpart
- Our implementation suffers a lot more from increasing number of records in the join compared to duckdb, which has an almost flat running-time curve

### Profiling the join query
Different profile results are provided in the folder `Join Query Run NonVecNonSimd A_B_0.6_C_0.8` directory.
If we look at the JFR profile provided in this folder (or the async profiler for that matter), we observe the following:
- About 1/3 of query time is spent in the `Int_Multi_Object_Map` `find()` method
- About 1/3 of query time is spent in the `Int_Multi_Object_Map` constructor (which we do expect to show up in this profile)

However, there is something suspicious things about this result: the constructor for the custom record type never shows up in the result, even though we know it is invoked often.

The perfasm profile marks the "execute" method of the query, and the "add" method of the hashmap as the hottest regions, where the listings for the execute method once more contains `<init>` calls to the `Int_Multi_Object_Map`, but not to the record constructors.