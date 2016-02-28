(ns luminus.http-server
  (:require [clojure.tools.logging :as log]
            [aleph.http :as http]))

(defn start [{:keys [handler init port] :as opts}]
  (try
    (init)
    (let [server (http/start-server
                   handler
                   (dissoc opts :handler :init))]
      (log/info "server started on port" port)
      server)
    (catch Throwable t
      (log/error t (str "server failed to start on port " port)))))

(defn stop [server destroy]
  (destroy)
  (.close server)
  (log/info "HTTP server stopped"))
