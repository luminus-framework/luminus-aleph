(ns luminus.http-server
  (:require [clojure.tools.logging :as log]
            [aleph.http :as http]))

(defn start [handler init port]
  (if @http-server
    (log/error "HTTP server is already running!")
    (try
      (init)
      (http/start-server
        handler
        {:port port})
      (log/info "server started on port:" port)
      (catch Throwable t
        (log/error t (str "server failed to start on port: " port))))))

(defn stop [destroy]
  (when @http-server
    (destroy)
    (log/info "HTTP server stopped")))
