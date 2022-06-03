# Demo of ApacheBeam CassandraIO bug
This bug was introduced with [BEAM-9008](https://issues.apache.org/jira/browse/BEAM-9008) in [this commit](https://github.com/apache/beam/commit/e12fc33e55e23db9f2aee330039d16dace34f9aa).

## Bug explanation
When using `CassandraIO`, a list of token ranges is requested to C* nodes in order to create splits in those ranges.  
A split will be represented as a RingRange resulting in a request to C* in the form of   
`TOKEN(partition_key) >= range_start AND TOKEN(partition_key) < range_end`

The token ring goes from Long.MIN_VALUE to Long.MAX_VALUE (so -2xxx to 2xxx), a range may contains the "join point" and be represented by [2xx, -2xxx].

In this case (Aka TokenRange isWrapping), old implementation used to send 2 different requests:
- `TOKEN(partition_key) >= range_start` (To get result up to the end of the ring, i.e. Long.MAX_VALUE)
- `TOKEN(partition_key) < range_end` (To get result from the beginning end of the ring, i.e. Long.MIN_VALUE)

Now, this behavior is not implemented anymore and token ranges are all called the same way, even in the wrapping case.  
It results in a request like :  
`TOKEN(partition_key) >= 2XXX AND TOKEN(partition_key) < -2xxx`  
This gives 0 results, and some data is never retrieved.

## WorkArounds
### Revert to 2.33.0
Simply revert to an implementation before the `readAll` and [BEAM-9008](https://issues.apache.org/jira/browse/BEAM-9008) merge.

### Use the readAll method
Define your own TokenRanges and call the readAll. An example is available in tests.  
This method is also simple and acceptable if you don't have lots of data and can simply provide a unique token range going from Long.MIN_VALUE to Long.MAX_VALUE (See tests for an example)

Pros:
- Faster than the `CassandraIO` split generation (flow starts faster)

Cons:
- Creating TokenRanges by yourself is not as efficient for C* nodes
- Only a workaround
