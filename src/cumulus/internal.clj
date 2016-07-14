(ns cumulus.internal
  (:require
    [clojure.string :as str]))


(defn reqd
  "Get required parameter k from map m. Throw exception if not found."
  [m k]
  (if (contains? m k)
    (get m k)
    (throw (IllegalArgumentException.
             (format "Key %s not found in %s" (pr-str k) (pr-str m))))))


(defn expected
  "Throw illegal input exception citing `expectation` and what was `found` did not match. Optionally accept a predicate
  fn to test `found` before throwing the exception."
  ([expectation found]
    (throw (IllegalArgumentException.
             (format "Expected %s, but found (%s) %s" expectation (class found) (pr-str found)))))
  ([pred expectation found]
    (when-not (pred found)
      (expected expectation found))))


(defn as-str
  "Convert `x` to string and return it"
  [x]
  (if (or (keyword? x) (symbol? x))
    (name x)
    (str x)))


(defn type-check-int
  [db-params key]
  (if (not (integer? (get db-params key)))
    ((throw (IllegalArgumentException. (format "Expected Integer but found %s" (pr-str (get db-params key))))))))


(defn type-check-string
  [db-params key]
  (if (not (or (string? (get db-params key)) (keyword? (get db-params key)) (symbol? (get db-params key))))
    ((throw (IllegalArgumentException. (format "Expected String but found %s" (pr-str (get db-params key))))))))


(defn parse-fn
  [db-params key]
  (try
    (Integer/parseInt (get db-params key))
    (catch NumberFormatException _
      (throw (IllegalArgumentException.(format "Expected Integer string but found %s" (get db-params key)))))))


;; ===== Helper Functions =====


(defn raw-params
  [driver-classname
   jdbc-url
   test-query]
  {:classname  driver-classname
   :jdbc-url   jdbc-url
   :test-query test-query})


(defn R
  [m k]
  (reqd m k))


(defn P
  ([m k]
    (get m k))
  ([m k d]
    (get m k d)))


(defn Q
  ([m k]
    (if-let [v (P m k)]
      (str ":" v)
      ""))
  ([m k d]
    (str ":" (P m k d))))


(defn assert-as
  "Assert the validity of x using predicate f, returning x on success or throw IllegalArgumentException on failure."
  [f description x]
  (expected f description x)
  x)


(defn assert-maybe
  "Assert the validity of x only if it is truthy, returning x on success or throw IllegalArgumentException on failure."
  [f description x]
  (when x
    (assert-as f description x)))


(defn jdbc
  [m]
  (merge m (raw-params
             (assert-as string? ":classname as string" (R m :classname))
             (assert-as string? ":classname as string" (R m :jdbc-url))
             (assert-maybe string? ":test-query to be string or nil" (get m :test-query)))))


(defn subprotocol
  [m]
  (merge m (raw-params
             (assert-as string? ":classname as string" (R m :classname))
             (format "jdbc:%s:%s" (as-str (R m :subprotocol)) (as-str (R m :subname)))
             (assert-maybe string? ":test-query to be string or nil" (get m :test-query)))))


(defn odbc
  [m]
  (merge m (raw-params
             "sun.jdbc.odbc.JdbcOdbcDriver"
             (format "jdbc:odbc:%s" (as-str (R m :dsn)))
             (assert-maybe string? ":test-query to be string or nil" (get m :test-query)))))


(defn odbc-lite
  [m]
  {:classname "sun.jdbc.odbc.JdbcOdbcDriver"
   :jdbc-url  (format "jdbc:odbc:%s" (as-str (R m :dsn)))
   :lite?     true})


(defn axiondb
  [m]
  (merge m (raw-params
             "org.axiondb.jdbc.AxionDriver"
             (let [target (:target m)]
               (case target
                 :memory (format "jdbc:axiondb:%s"     (as-str (R m :database)))
                 :filesys (format "jdbc:axiondb:%s:%s" (as-str (R m :database))  (as-str (R m :db-path)))))
             "SELECT 1")))


(defn derby
  [m]
  (merge m (raw-params
             "org.apache.derby.jdbc.EmbeddedDriver"
             (let [target (:target m)]
               (case target
                 :memory   (format "jdbc:derby:memory:%s;create=true;"     (as-str (R m :database)))
                 :filesys   (format "jdbc:derby:directory:%s;create=true;" (as-str (R m :database)))
                 :classpath (format "jdbc:derby:classpath:%s"              (as-str (R m :database)))
                 :jar       (format "jdbc:derby:jar:(%s)%s"                (as-str (R m :jar-path)) (as-str (R m :database)))
                 :network   (format "jdbc:derby://%s%s/%s;create=true;"    (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
                 (expected ":target to be :memory, :filesys, :classpath, :jar or :network" target)))
             "values(1)")))


(defn h2
  [m]
  (merge m (raw-params
             "org.h2.Driver"
             (let [target (:target m)]
               (case target
                 :memory  (format "jdbc:h2:mem:%s"             (as-str (R m :database)))
                 :filesys (format "jdbc:h2:file:%s"            (as-str (R m :database)))
                 :network (format "jdbc:h2:tcp:%s%s/%s"        (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
                 (expected ":target to be :memory, :filesys or :network" target)))
             "SELECT 1")))


(defn hsqldb
  [m]
  (merge m (raw-params
             "org.hsqldb.jdbcDriver"
             (let [target (:target m)]
               (case target
                 :memory  (format "jdbc:hsqldb:mem:%s"         (as-str (R m :database)))
                 :filesys (format "jdbc:hsqldb:file:%s"        (as-str (R m :database)))
                 :network (format "jdbc:hsqldb:hsql://%s%s/%s" (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
                 (expected ":target to be :memory, :filesys or :network" target)))
             "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS")))


(defn mckoi
  [m]
  (merge m (raw-params
             "com.mckoi.JDBCDriver"
             (format "jdbc:mckoi:local://%s"                   (as-str (R m :database)))
             "SELECT 1")))


(defn sqlite
  [m]
  (merge m (raw-params
             "org.sqlite.JDBC"
             (let [target (:target m)]
               (case target
                 :memory  (format "jdbc:sqlite::memory:")
                 :filesys (format "jdbc:sqlite:%s"             (as-str (R m :database)))
                 (expected ":target to be :memory or :filesys" target)))
             "SELECT 1")))


(defn cubrid
  [m]
  (merge m (raw-params
             "cubrid.jdbc.driver.CUBRIDDriver"
             (format "jdbc:cubrid:%s%s:%s"                     (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
             "SELECT 1;")))


(defn firebird
  [m]
  (merge m (raw-params
             "org.firebirdsql.jdbc.FBDriver"
             (format "jdbc:firebirdsql://%s%s/%s"              (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
             "SELECT CAST(1 AS INTEGER) FROM rdb$database;")))


(defn jtds-sqlserver
  [m]
  (merge m (raw-params
             "net.sourceforge.jtds.jdbc.Driver"
             (format "jdbc:jtds:sqlserver://%s%s%s"            (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
             "select 1;")))


(defn jtds-sybase
  [m]
  (merge m (raw-params
             "net.sourceforge.jtds.jdbc.Driver"
             (format "jdbc:jtds:sybase://%s%s%s"               (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
             "select 1;")))


(defn monetdb
  [m]
  (merge m (raw-params
             "nl.cwi.monetdb.jdbc.MonetDriver"
             (format "jdbc:monetdb://%s%s/%s"                  (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
             "SELECT 1;")))


(defn mysql
  [m]
  (merge m (raw-params
             "com.mysql.jdbc.Driver"
             (format "jdbc:mysql://%s%s/%s"                    (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
             "SELECT 1;")))


(defn postgresql
  [m]
  (merge m (raw-params
             "org.postgresql.Driver"
             (format "jdbc:postgresql://%s%s/%s"               (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
             "SELECT version();")))


(defn pgsql
  [m]
  (merge m (raw-params
             "com.impossibl.postgres.jdbc.PGDriver"
             (format "jdbc:pgsql://%s%s/%s"                    (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
             "SELECT version();")))


(defn db2
  [m]
  (merge m (raw-params
             "com.ibm.db2.jcc.DB2Driver"
             (format "jdbc:db2://%s%s/%s"                      (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
             "select * from sysibm.SYSDUMMY1;")))


(defn oracle
  [m]
  (merge m (raw-params
             "oracle.jdbc.driver.OracleDriver"
             (let [style (:style m)]
               (case style
                 :system-id    (format "jdbc:oracle:thin:@%s%s:%s"         (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
                 :service-name (format "jdbc:oracle:thin:@//%s%s/%s"       (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
                 :tns-name     (format "jdbc:oracle:thin:@%s"              (as-str (R m :database)))
                 :ldap         (format "jdbc:oracle:thin:@ldap://%s/%s:%s" (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
                 :oci          (format "jdbc:oracle:oci:@%s"               (as-str (R m :database)))
                 :oci8         (format "jdbc:oracle:oci8:@%s"              (as-str (R m :database)))
                 (expected ":target to be :system-id, :service-name, :tns-name, :ldap,oci or :oci8" style)))
             "SELECT 1 FROM DUAL")))


(defn sapdb
  [m]
  (merge m (raw-params
             "com.sap.dbtech.jdbc.DriverSapDB"
             (format "jdbc:sapdb://%s%s/%s"                    (as-str (R m :host)) (Q m :port) (as-str (R m :database)))
             "SELECT 1 FROM DUAL")))


(defn sqlserver
  [m]
  (merge m (raw-params
             "com.microsoft.sqlserver.jdbc.SQLServerDriver"
             (format "jdbc:sqlserver://%s%s%s"                (as-str (Q m :host)) (as-str (Q m :instance)) (Q m :port))
             "SELECT 1")))


(defn sybase
  [m]
  (merge m (raw-params
             "com.sybase.jdbc2.jdbc.SybDriver"
             (if (get m :database)
               (format "jdbc:sybase:Tds:%s%s?ServiceName=%s?" (as-str (R m :host)) (Q m :port) (as-str (Q m :database)))
               (format "jdbc:sybase:Tds:%s%s"                 (as-str (R m :host)) (Q m :port)))
             "SELECT 1")))
