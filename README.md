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

Even plain `curl` shows a problem, at least on my machine:

```
$ curl -Iv http://localhost:9877/bytes
* Adding handle: conn: 0x7fd428803a00
* Adding handle: send: 0
* Adding handle: recv: 0
* Curl_addHandleToPipeline: length: 1
* - Conn 0 (0x7fd428803a00) send_pipe: 1, recv_pipe: 0
* About to connect() to localhost port 9877 (#0)
*   Trying ::1...
* Connected to localhost (::1) port 9877 (#0)
> HEAD /bytes HTTP/1.1
> User-Agent: curl/7.30.0
> Host: localhost:9877
> Accept: */*
>
< HTTP/1.1 200 OK
HTTP/1.1 200 OK
< Content-Type: image/png
Content-Type: image/png
* Server Aleph/0.4.1 is not blacklisted
< Server: Aleph/0.4.1
Server: Aleph/0.4.1
< Connection: Keep-Alive
Connection: Keep-Alive
< Date: Fri, 13 May 2016 14:10:43 GMT
Date: Fri, 13 May 2016 14:10:43 GMT
< content-length: 4345658
content-length: 4345658

<
* Excess found in a non pipelined read: excess = 16233 url = /bytes (zero-length body)
* Connection #0 to host localhost left intact
```
