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

(defn odbc
  [m]
  (raw-params
    "sun.jdbc.odbc.JdbcOdbcDriver"
    (format "jdbc:odbc:%s"  (R m :dsn))
    nil))

(defn axiondb
  [m]
  (raw-params 
    "org.axiondb.jdbc.AxionDriver"
    (let [target (:target m)]
      (case target
        :memory (format "jdbc:axiondb:%s"  (R m :database))
        :filesys (format "jdbc:axiondb:%s:%s" (R m :database) (R m :db-path))))
    "SELECT 1"))

(defn derby
  [m]
  (raw-params 
    "org.apache.derby.jdbc.EmbeddedDriver"
    (let [target (:target m)]
      (case target
        :memory   (format "jdbc:derby:memory:%s;create=true;"     (R m :database))
        :filesys   (format "jdbc:derby:directory:%s;create=true;" (R m :database))
        :classpath (format "jdbc:derby:classpath:%s"              (R m :database))
        :jar       (format "jdbc:derby:jar:(%s)%s"                (R m :jar-path) (R m :database))
        :network   (format "jdbc:derby://%s%s/%s;create=true;"    (R m :host) (Q m :port) (R m :database))
        (expected ":target to be :memory, :filesys, :classpath, :jar or :network" target)))
    "values(1)"))

(defn h2
  [m]
  (raw-params
    "org.h2.Driver"
    (let [target (:target m)]
      (case target 
        :memory  (format "jdbc:h2:mem:%s"             (R m :database))
        :filesys (format "jdbc:h2:file:%s"            (R m :database))
        :network (format "jdbc:h2:tcp:%s%s/%s"        (R m :host) (Q m :port) (R m :database))
        (expected ":target to be :memory, :filesys or :network" target)))
    "SELECT 1"))

(defn hsqldb
  [m]
  (raw-params 
    "org.hsqldb.jdbcDriver"
    (let [target (:target m)]
      (case target
        :memory  (format "jdbc:hsqldb:mem:%s"         (R m :database))
        :filesys (format "jdbc:hsqldb:file:%s"        (R m :database))
        :network (format "jdbc:hsqldb:hsql://%s%s/%s" (R m :host) (Q m :port) (R m :database))
        (expected ":target to be :memory, :filesys or :network" target)))
    "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"))

(defn mckoi
  [m]
  (raw-params 
    "com.mckoi.JDBCDriver"
    (format "jdbc:mckoi:local://%s"                   (R m :database))
    "SELECT 1"))

(defn sqlite
  [m]
  (raw-params 
    "org.sqlite.JDBC"
    (let [target (:target m)]
      (case target 
        :memory  (format "jdbc:sqlite::memory:")
        :filesys (format "jdbc:sqlite:%s"             (R m :database))
        (expected ":target to be :memory or :filesys" target)))
    "SELECT 1"))

(defn cubrid
  [m]
  (raw-params 
    "cubrid.jdbc.driver.CUBRIDDriver"
    (format "jdbc:cubrid:%s%s:%s"                 (R m :host) (Q m :port) (R m :database))
    "SELECT 1;"))

(defn firebird
  [m]
  (raw-params
    "org.firebirdsql.jdbc.FBDriver"
    (format "jdbc:firebirdsql://%s%s/%s"          (R m :host) (Q m :port) (R m :database))
    "SELECT CAST(1 AS INTEGER) FROM rdb$database;"))

(defn jtds-sqlserver
  [m]
  (raw-params
    "net.sourceforge.jtds.jdbc.Driver"
    (format "jdbc:jtds:sqlserver://%s%s%s"        (R m :host) (Q m :port) (R m :database))
    "select 1;"))

(defn jtds-sybase
  [m]
  (raw-params 
    "net.sourceforge.jtds.jdbc.Driver"
    (format "jdbc:jtds:sybase://%s%s%s"           (R m :host) (Q m :port) (R m :database))
    "select 1;"))

(defn monetdb
  [m]
  (raw-params
    "nl.cwi.monetdb.jdbc.MonetDriver"
    (format "jdbc:monetdb://%s%s/%s"              (R m :host) (Q m :port) (R m :database))
    "SELECT 1;"))

(defn mysql
  [m]
  (raw-params 
    "com.mysql.jdbc.Driver"
    (format "jdbc:mysql://%s%s/%s"                (R m :host) (Q m :port) (R m :database))
    "SELECT 1;"))

(defn postgresql
  [m]
  (raw-params 
    "org.postgresql.Driver"
    (format "jdbc:postgresql://%s%s/%s"           (R m :host) (Q m :port) (R m :database))
    "SELECT version();"))

(defn db2
  [m]
  (raw-params 
    "com.ibm.db2.jcc.DB2Driver"
    (format "jdbc:db2://%s%s/%s"                  (R m :host) (Q m :port) (R m :database))
    "select * from sysibm.SYSDUMMY1;"))

(defn oracle 
  [m]
  (raw-params
    "oracle.jdbc.driver.OracleDriver"
    (let [style (:style m)]
      (case style
        :system-id    (format "jdbc:oracle:thin:@%s%s:%s"        (R m :host) (Q m :port) (R m :database))
        :service-name (format "jdbc:oracle:thin:@//%s%s/%s"      (R m :host) (Q m :port) (R m :database))
        :tns-name     (format "jdbc:oracle:thin:@%s"              (R m :database))
        :ldap         (format "jdbc:oracle:thin:@ldap://%s/%s:%s" (R m :host) (Q m :port) (R m :database))
        :oci          (format "jdbc:oracle:oci:@%s"               (R m :database))
        :oci8         (format "jdbc:oracle:oci8:@%s"              (R m :database))
        (expected ":target to be :system-id, :service-name, :tns-name, :ldap,oci or :oci8" style)))
                         "SELECT 1 FROM DUAL"))

(defn sapdb
  [m]
  (raw-params
    "com.sap.dbtech.jdbc.DriverSapDB"
    (format "jdbc:sapdb://%s%s/%s"                 (R m :host) (Q m :port) (R m :database))
    "SELECT 1 FROM DUAL"))

(defn sqlserver
  [m]
  (raw-params
    "com.microsoft.sqlserver.jdbc.SQLServerDriver"
    (format "jdbc:sqlserver://%s%s%s"            (Q m :host) (Q m :instance) (Q m :port))
    "SELECT 1"))

(defn sybase 
  [m]
  (raw-params
    "com.sybase.jdbc2.jdbc.SybDriver"
    (if (get m :database)
      (format "jdbc:sybase:Tds:%s%s?ServiceName=%s?"  (R m :host) (Q m :port) (Q m :database))
      (format "jdbc:sybase:Tds:%s%s" (R m :host) (Q m :port)))
    "SELECT 1"))
