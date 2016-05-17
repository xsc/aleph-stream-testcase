# aleph-stream-testcase

Start up aleph using one of:

```
$ lein run -m aleph-stream-testcase.core
$ lein run -m aleph-stream-testcase.core fixed-thread
```

Thre are three endpoints provided:

- http://localhost:9877/stream uses an `InputStream` as `:body`,
- http://localhost:9877/bytes converts the `InputStream` to a byte array first,
- http://localhost:9877/proxy uses the aleph HTTP client to fetch the body of
  http://google.com.

## Issues

This repository is used to demonstrate some issues discovered in aleph.

### Keep-Alive Behaviour [closed]

https://github.com/ztellman/aleph/issues/248

This might just be a quirk in ApacheBench when using the `-k` option, as tests
with other tools did not expose the same problems.

### Fixed-Size Thread Pool vs. Aleph HTTP Client

https://github.com/ztellman/aleph/issues/249
