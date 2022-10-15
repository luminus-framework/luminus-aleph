(ns luminus.http-server
  (:require [clojure.tools.logging :as log]
            [aleph.http :as http]
            [aleph.netty :as netty]
            [manifold.deferred :as d]))

;; from https://www.booleanknot.com/blog/2016/07/15/asynchronous-ring.html
(defn async-ring->aleph [handler]
  (fn [request]
    (let [response (d/deferred)]
      (handler request #(d/success! response %) #(d/error! response %))
      response)))


(defn start [{:keys [handler port async?] :as opts}]
  (try
    (log/info "starting HTTP server on port" port)
    (let
      [server (http/start-server
                (if async? 
                  (async-ring->aleph handler)
                  handler)
                (dissoc opts :handler))]
      (future (netty/wait-for-close server))
      server)
    (catch Throwable t
      (log/error t (str "server failed to start on port " port))
      (throw t))))

(defn stop [server]
  (.close server)
  (log/info "HTTP server stopped"))
