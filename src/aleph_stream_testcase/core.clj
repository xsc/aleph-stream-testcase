(ns aleph-stream-testcase.core
  (:require [aleph.http :as http]
            [aleph.flow :as flow]
            [byte-streams :as streams]
            [clojure.java.io :as io]))

(defn handler
  [{:keys [uri] :as request}]
  (locking *out*
    (println "[request]" (System/currentTimeMillis) request))
  (try
    (case uri
      "/stream"
      {:status 200
       :headers {"content-type" "image/png"}
       :body (-> "test.png" io/resource io/input-stream)}

      "/bytes"
      {:status 200
       :headers {"content-type" "image/png"}
       :body (with-open [in (-> "test.png" io/resource io/input-stream)]
               (streams/to-byte-array in))}

      "/proxy"
      (let [{:keys [body]} @(http/get "http://google.com"
                                      {:connection-timeout 1000
                                       :request-timeout    1000})]
        {:status 200
         :body (when body
                 (with-open [in body]
                   (slurp in)))})

      {:status 404})
    (catch Throwable t
      (locking *out*
        (println "[error]" uri " -> " (.getMessage t)))
      {:status 500})
    (finally
      (locking *out*
        (println "[request done]" (System/currentTimeMillis) request)))))

(defn -main
  [& [executor]]
  (with-open [server (http/start-server
                       #'handler
                       (merge
                         {:port 9877}
                         (when (= executor "fixed-thread")
                           {:executor (flow/fixed-thread-executor 4)})))]
    (println "server is running ...")
    @(promise)))
