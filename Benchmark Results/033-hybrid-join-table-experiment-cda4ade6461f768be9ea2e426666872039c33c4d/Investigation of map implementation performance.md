Preliminary conclusions from the "Influence of Hash-Table Altererations":
- Record classes in the aggregation table never yield performance degradation over any TPC-H query, and hence, are not the subject of performance profiling.
- The record classes in join tables (with the new default initialisation, but without overhead reduction, 031) yield an average improvement of 15% on Q1, Q3 and Q10 combined
    - However, for Q3 SF-10 we see performance degradations of roughly 10% (in a reproducible fashion)
- By futher reducing the overhead in these maps, we can bring down the running times on Q3 SF-1 and Q10 SF-10, but at slight performance cost in Q1 NV
    - It might thus be worth while to revert this change for the aggregation map
    - However, might be that other queries do benefit like TPC-H Q10 (even though the benefit might be in the join maps reduced overhead)
- If we use the hybrid join map (so separate key, next, count arrays - but a combined value records array), we get similar overall performance, but no degradations
    - Additionally, scalability seems improved (TPC-H Hash Table Alterations Effects - Renewed)

Investigation of Q3 performance using the new init strategy across the board: (test package of commit c068f7792a3dd4d7665447e25c7ccfc499f2479c)
- Original Join Maps:
    - 22.13% of query time spent in getIndex of customer map, which effectively is find()
        - Expensive line: initialIndex == -1 (66% of find)
    - 27.51% of query time spent in getIndex of other map, which effectively is find()
        - Expensive line: initialIndex == -1 (73% of find)

- Record Join Maps:
    - 19.82% of query time spent in getIndex of customer map, which effectively is find()
        - Expensive lines: initialIndex == -1 (34% of find), currentKeyRecord.key != key (41% of find)
    - 40.73% of query time spent in getIndex of other map, which effectively is find()
        - Expensive lines: initialIndex == -1 (44% of find), currentKeyRecord.key != key (36% of find)

- Hybrid Join Maps:
    - 14.94% of query time spent in getIndex of customer map, which effectively is find()
        - Expensive line: initialIndex == -1 (52% of find)
    - 31.6% of query time spent in getIndex of other map, which effectively is find()
        - Expensive line: this.keys[currentIndex] != key (67% of find)

Experiment to investigate if "reduced overhead" makes sense in in aggregation maps.
It is currently unclear whether the "reduced overhead" of using references instead of array indices in the aggregation tables is actually faster.
We run a separate experiment to investigate this for the TPC-H queries. (commit 64b54f23eb89c2eebaf5c6fe3c0263c7a605f421)
From this experiment, we do not see any improvement and hence we revert the change.