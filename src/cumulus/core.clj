(ns cumulus.core
  (:require
    [clojure.string :as str]
    [cumulus.internal :as i]))


(defn jdbc-params
  "Given database-specific parameters, return a map of fundamental JDBC params consisting of
  the following mandatory keys (all of them having string values):
  :classname (fully qualified class name)
  :jdbc-url
  :test-query"
  ([db-params]
    (as-> db-params $
      (update-in $ [:adapter] (fn [adapter]
                                (if (nil? adapter)
                                  (cond
                                    (and (contains? $ :classname)
                                      (contains? $ :jdbc-url))    :jdbc
                                    (contains? $ :subprotocol)    :subprotocol
                                    :otherwise                    nil)
                                  adapter)))
      (jdbc-params (:adapter $) $)))
  ([db-type db-params]
    (when-let [key (get db-params :port)]
      (i/type-check-int db-params :port))
    (when-let [key (get db-params :database)]
      (i/type-check-string db-params :database))
    (when-let [key (get db-params :host)]
      (i/type-check-string db-params :host))
    (case db-type
      ;;embedded
      nil              (i/jdbc db-params)
      :jdbc            (i/jdbc db-params)
      :subprotocol     (i/subprotocol db-params)
      :odbc            (i/odbc db-params)
      :odbc-lite       (i/odbc-lite db-params)
      :axiondb         (i/axiondb db-params)
      :derby           (i/derby db-params)
      :h2              (i/h2 db-params)
      :hsqldb          (i/hsqldb db-params)
      :mckoi           (i/mckoi db-params)
      :sqlite          (i/sqlite db-params)
      ;; network OSS
      :cubrid          (i/cubrid db-params)
      :firebird        (i/firebird db-params)
      :jtds-sqlserver  (i/jtds-sqlserver db-params)
      :jtds-sybase     (i/jtds-sybase db-params)
      :monetdb         (i/monetdb db-params)
      :mysql           (i/mysql db-params)
      :postgresql      (i/postgresql db-params)
      :pgsql           (i/pgsql db-params)
      ;; network proprietary
      :db2             (i/db2 db-params)
      :oracle          (i/oracle db-params)
      :sapdb           (i/sapdb db-params)
      :sqlserver       (i/sqlserver db-params)
      :sybase          (i/sybase db-params)
      (throw (IllegalArgumentException.
               (format "Database/adapter type %s is not supported" db-type))))))
