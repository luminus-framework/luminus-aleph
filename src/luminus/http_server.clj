(ns luminus.http-server
  (:require [clojure.tools.logging :as log]
            [aleph.http :as http]))

(defn start [{:keys [handler init port] :as opts}]
  (try
      (init)
      (log/info "HTTP server is starting on port " port)
      (http/start-server
       handler
       (dissoc opts :handler :init))
      (log/info "server started on port:" port)
      (catch Throwable t
        (log/error t (str "server failed to start on port: " port)))))

(defn stop [destroy]
  (destroy))
