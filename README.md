# aleph-stream-testcase

Start up aleph using:

```
$ lein run -m aleph-stream-testcase.core
```

Thre are two endpoints provided:

- http://localhost:9877/stream uses an `InputStream` as `:body`,
- http://localhost:9877/bytes converts the `InputStream` to a byte array first.

We'll use Apache Bench to test our endpoints.

## No Keep-Alive

The following should behave as expected, producing no errors or non-200 status
codes.

```
ab -n 500 -c 4 http://localhost:9877/stream
ab -n 500 -c 4 http://localhost:9877/bytes
```

## With Keep-Alive

The following, however, throws for the `/stream` endpoint and just cuts off the
stream for the `/bytes` version.

```
ab -n 500 -c 4 -k http://localhost:9877/stream
ab -n 500 -c 4 -k http://localhost:9877/bytes
```
