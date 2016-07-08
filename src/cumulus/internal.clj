(ns cumulus.internal)

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

(defn type-check
  [db-params key]
  (if (not (integer? (get db-params key)))
    ((throw (IllegalArgumentException. (format "Expected Integer but found %s" (pr-str (get db-params key))))))))

(defn type-check-string
  [db-params key]
  (if (not (string? (get db-params key)))
    ((throw (IllegalArgumentException. (format "Expected String but found %s" (pr-str (get db-params key))))))))

(defn parse-fn
  [db-params key]
  (try 
    (Integer/parseInt (get db-params key))
    (catch NumberFormatException _
      (throw (IllegalArgumentException.(format "Expected Integer string but found %s" (get db-params key)))))))

;;;;;;;Helper Functions
(defn raw-params
  [driver-classname
   jdbc-url
   test-query]
  {:classname  driver-classname
   :jdbc-url   jdbc-url
   :test-query test-query})

(def ^:dynamic *db-params* nil)
(defn R 
  [k]
  (reqd *db-params* k))

(defn P 
  ([k]
    (get *db-params* k))
  ([k d]
    (get *db-params* k d)))

(defn Q 
  ([k]
    (if-let [v (P k)] 
      (str ":" v)
      ""))
  ([k d]
    (str ":" (P k d))))

(defn odbc
  [*db-params*]
  (raw-params
    "sun.jdbc.odbc.JdbcOdbcDriver"
    (format "jdbc:odbc:%s"  (R :dsn))
    nil))

(defn axiondb
  [*db-params*]
  (raw-params 
    "org.axiondb.jdbc.AxionDriver"
    (let [target (:target *db-params*)]
      (case target
        :memory (format "jdbc:axiondb:%s"  (R :database))
        :filesys (format "jdbc:axiondb:%s:%s" (R :database) (R :db-path))))
    "SELECT 1"))

(defn derby
  [*db-params*]
  (raw-params 
    "org.apache.derby.jdbc.EmbeddedDriver"
    (let [target (:target *db-params*)]
      (case target
        :memory   (format "jdbc:derby:memory:%s;create=true;"     (R :database))
        :filesys   (format "jdbc:derby:directory:%s;create=true;" (R :database))
        :classpath (format "jdbc:derby:classpath:%s"              (R :database))
        :jar       (format "jdbc:derby:jar:(%s)%s"                (R :jar-path) (R :database))
        :network   (format "jdbc:derby://%s%s/%s;create=true;"    (R :host) (Q :port) (R :database))
        (expected ":target to be :memory, :filesys, :classpath, :jar or :network" target)))
    "values(1)"))

(defn h2
  [*db-params*]
  (raw-params
    "org.h2.Driver"
    (let [target (:target *db-params*)]
      (case target 
        :memory  (format "jdbc:h2:mem:%s"             (R :database))
        :filesys (format "jdbc:h2:file:%s"            (R :database))
        :network (format "jdbc:h2:tcp:%s%s/%s"        (R :host) (Q :port) (R :database))
        (expected ":target to be :memory, :filesys or :network" target)))
    "SELECT 1"))

(defn hsqldb
  [*db-params*]
  (raw-params 
    "org.hsqldb.jdbcDriver"
    (let [target (:target *db-params*)]
      (case target
        :memory  (format "jdbc:hsqldb:mem:%s"         (R :database))
        :filesys (format "jdbc:hsqldb:file:%s"        (R :database))
        :network (format "jdbc:hsqldb:hsql://%s%s/%s" (R :host) (Q :port) (R :database))
        (expected ":target to be :memory, :filesys or :network" target)))
    "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"))

(defn mckoi
  [*db-params*]
  (raw-params 
    "com.mckoi.JDBCDriver"
    (format "jdbc:mckoi:local://%s"                   (R :database))
    "SELECT 1"))

(defn sqlite
  [*db-params*]
  (raw-params 
    "org.sqlite.JDBC"
    (let [target (:target *db-params*)]
      (case target 
        :memory  (format "jdbc:sqlite::memory:")
        :filesys (format "jdbc:sqlite:%s"             (R :database))
        (expected ":target to be :memory or :filesys" target)))
    "SELECT 1"))

(defn cubrid
  [*db-params*]
  (raw-params 
    "cubrid.jdbc.driver.CUBRIDDriver"
    (format "jdbc:cubrid:%s%s:%s"                 (R :host) (Q :port) (R :database))
    "SELECT 1;"))

(defn firebird
  [*db-params*]
  (raw-params
    "org.firebirdsql.jdbc.FBDriver"
    (format "jdbc:firebirdsql://%s%s/%s"          (R :host) (Q :port) (R :database))
    "SELECT CAST(1 AS INTEGER) FROM rdb$database;"))

(defn jtds-sqlserver
  [*db-params*]
  (raw-params
    "net.sourceforge.jtds.jdbc.Driver"
    (format "jdbc:jtds:sqlserver://%s%s%s"        (R :host) (Q :port) (R :database))
    "select 1;"))

(defn jtds-sybase
  [*db-params*]
  (raw-params 
    "net.sourceforge.jtds.jdbc.Driver"
    (format "jdbc:jtds:sybase://%s%s%s"           (R :host) (Q :port) (R :database))
    "select 1;"))

(defn monetdb
  [*db-params*]
  (raw-params
    "nl.cwi.monetdb.jdbc.MonetDriver"
    (format "jdbc:monetdb://%s%s/%s"              (R :host) (Q :port) (R :database))
    "SELECT 1;"))

(defn mysql
  [*db-params*]
  (raw-params 
    "com.mysql.jdbc.Driver"
    (format "jdbc:mysql://%s%s/%s"                (R :host) (Q :port) (R :database))
    "SELECT 1;"))

(defn postgresql
  [*db-params*]
  (raw-params 
    "org.postgresql.Driver"
    (format "jdbc:postgresql://%s%s/%s"           (R :host) (Q :port) (R :database))
    "SELECT version();"))

(defn db2
  [*db-params*]
  (raw-params 
    "com.ibm.db2.jcc.DB2Driver"
    (format "jdbc:db2://%s%s/%s"                  (R :host) (Q :port) (R :database))
    "select * from sysibm.SYSDUMMY1;"))

(defn oracle 
  [*db-params*]
  (raw-params
    "oracle.jdbc.driver.OracleDriver"
    (let [style (:style *db-params*)]
      (case style
        :system-id    (format "jdbc:oracle:thin:@%s%s:%s"        (R :host) (Q :port) (R :database))
        :service-name (format "jdbc:oracle:thin:@//%s%s/%s"      (R :host) (Q :port) (R :database))
        :tns-name     (format "jdbc:oracle:thin:@%s"              (R :database))
        :ldap         (format "jdbc:oracle:thin:@ldap://%s/%s:%s" (R :host) (Q :port) (R :database))
        :oci          (format "jdbc:oracle:oci:@%s"               (R :database))
        :oci8         (format "jdbc:oracle:oci8:@%s"              (R :database))
        (expected ":target to be :system-id, :service-name, :tns-name, :ldap,oci or :oci8" style)))
                         "SELECT 1 FROM DUAL"))

(defn sapdb
  [*db-params*]
  (raw-params
    "com.sap.dbtech.jdbc.DriverSapDB"
    (format "jdbc:sapdb://%s%s/%s"                 (R :host) (Q :port) (R :database))
    "SELECT 1 FROM DUAL"))

(defn sqlserver
  [*db-params*]
  (raw-params
    "com.microsoft.sqlserver.jdbc.SQLServerDriver"
    (format "jdbc:sqlserver://%s%s%s"            (Q :host) (Q :instance) (Q :port))
    "SELECT 1"))

(defn sybase 
  [*db-params*]
  (raw-params
    "com.sybase.jdbc2.jdbc.SybDriver"
    (if (get *db-params* :database)
      (format "jdbc:sybase:Tds:%s%s?ServiceName=%s?"  (R :host) (Q :port) (Q :database))
      (format "jdbc:sybase:Tds:%s%s" (R :host) (Q :port)))
    "SELECT 1"))
