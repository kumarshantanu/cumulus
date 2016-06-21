(ns cumulus.core
  (:require
    [clojure.string :as str]
    [cumulus.internal :as i]))



(defn jdbc-params
  "Given database-specific parameters, return a map of fundamental JDBC params consisting of
  the following mandatory keys (all of them having string values):
  :driver-class (fully qualified class name)
  :jdbc-url
  :test-query"
  [db-type db-params]
  (let [R (partial i/reqd db-params)]
    (case db-type
      ;;embedded
     :odbc {:classname "sun.jdbc.odbc.JdbcOdbcDriver"
           :jdbc-url  (format "jdbc:odbc:%s" (R :dsn))}
     
     :axiondb {:classname "org.axiondb.jdbc.AxionDriver"
               :jdbc-url (format "jdbc:axiondb:%s"  (R :database))
               :test-query "SELECT 1"}
     
     :derby {:classname "org.apache.derby.jdbc.EmbeddedDriver"
           :jdbc-url
                     {:memory   (format "jdbc:derby:memory:%s;create=true;"    (R :database))
                     :filesys   (format "jdbc:derby:directory:%s;create=true;" (R :database))
                     :classpath (format "jdbc:derby:classpath:%s"              (R :database))
                     :jar       (format "jdbc:derby:jar:(%s)%s"                (R :jar-path) (R :database))
                     :network   (format "jdbc:derby://%s%s/%s;create=true;"    (R :host) (R :port) (R :database))}
            :test-query "values(1)"}
     
     ;            :jdbc-url (format "jdbc:derby:%s" (R :database))

     :h2 {:classname "org.h2.Driver"
          :jdbc-url {:memory  (format "jdbc:h2:mem:%s"      (R :database))
                       :filesys (format "jdbc:h2:file:%s"    (R :database))
                       :network (format "jdbc:h2:tcp:%s%s/%s" (R :host) (R :port) (R :database))}
          :test-query "SELECT 1"}
     
     :hsqldb {:classname "org.hsqldb.jdbcDriver"
              :jdbc-url {:memory  (format "jdbc:hsqldb:mem:%s"        (R :database))
                       :filesys (format "jdbc:hsqldb:file:%s"        (R :database))
                       :network (format "jdbc:hsqldb:hsql://%s%s/%s" (R :host) (R :port)  (R :database))}
              :test-query "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"}
     
     :mckoi {:classname "com.mckoi.JDBCDriver"
             :jdbc-url (format "jdbc:mckoi:local://%s" (R :database))
             :test-query "SELECT 1"}
     
     :sqlite {:classname "org.sqlite.JDBC"
              :jdbc-url  {:memory  "jdbc:sqlite::memory:"
                       :filesys (format "jdbc:sqlite:%s" (R :database))}
              :test-query "SELECT 1"}
     
     ;; network OSS
     :cubrid {:classname "cubrid.jdbc.driver.CUBRIDDriver"
              :jdbc-url (format "jdbc:cubrid:%s:%s:%s"      (R :host) (R :port) (R :database))
              :test-query "SELECT 1;"}
     
     :firebird {:classname "org.firebirdsql.jdbc.FBDriver"
                :jdbc-url (format "jdbc:firebirdsql://%s:%s/%s"   (R :host) (R :port) (R :database))
                :test-query "SELECT CAST(1 AS INTEGER) FROM rdb$database;"}
     
     :jtds-sqlserver {:classname "net.sourceforge.jtds.jdbc.Driver"
                      :jdbc-url (format "jdbc:jtds:sqlserver://%s:%s%s" (R :host) (R :port) (R :database))
                      :test-query "select 1;"}
     
     :jtds-sybase {:classname "net.sourceforge.jtds.jdbc.Driver"
                   :jdbc-url (format "jdbc:jtds:sybase://%s:%s%s"    (R :host) (R :port) (R :database))
                   :test-query "select 1;"}
     
     :monetdb        {:classname "nl.cwi.monetdb.jdbc.MonetDriver"
                      :jdbc-url (format "jdbc:monetdb://%s:%s/%s"       (R :host) (R :port) (R :database))
                      :test-query "SELECT 1;"}
     
     :mysql {:classname "com.mysql.jdbc.Driver"
             :jdbc-url (format "jdbc:mysql://%s:%s/%s"         (R :host) (R :port) (R :database))
             :test-query "SELECT 1;"}
     
     :postgresql {:classname "org.postgresql.Driver"
                  :jdbc-url (format "jdbc:postgresql://%s:%s/%s"    (R :host) (R :port) (R :database))
                  :test-query "SELECT version();"}
     
     ;; network proprietary
     :db2 {:classname "com.ibm.db2.jcc.DB2Driver"
           :jdbc-url (format "jdbc:db2://%s:%s/%s"           (R :host) (R :port) (R :database))
           :test-query "select * from sysibm.SYSDUMMY1;"}
     
     :oracle         {:classname "oracle.jdbc.driver.OracleDriver"
                     :jdbc-url
                     
                             {:system-id    (format "jdbc:oracle:thin:@%s:%s:%s"
                                              (R :host) (R :port) (R :database))
                             :service-name (format "jdbc:oracle:thin:@//%s:%s/%s"
                                             (R :host) (R :port) (R :database))
                             :tns-name     (format "jdbc:oracle:thin:@%s"
                                            (R :database))
                             :ldap         (format "jdbc:oracle:thin:@ldap://%s%s/%s:%s"
                                             (R :host) (R :port) (R :database))
                             :oci          (format "jdbc:oracle:oci:@%s"
                                            (R :database))
                             :oci8         (format "jdbc:oracle:oci8:@%s"
                                             (R :database))}
                          :val-query "SELECT 1 FROM DUAL"}
     
     :sapdb {:classname "com.sap.dbtech.jdbc.DriverSapDB"
             :jdbc-url (format "jdbc:sapdb://%s:%s/%s"          (R :host) (R :port) (R :database))
             :test-query "SELECT 1 FROM DUAL"}
     
     :sqlserver {:classname "com.microsoft.sqlserver.jdbc.SQLServerDriver"
                :jdbc-url (format "jdbc:sqlserver://%s\\%s:%s"     (R :host) (R :instance) (R :port))
                :test-query "SELECT 1"}
     
     :sybase {:classname "com.sybase.jdbc2.jdbc.SybDriver"
             :jdbc-url (format "jdbc:sybase:Tds:%s:%s?ServiceName=%s"       (R :host) (R :port)  (R :database))
             :test-query "SELECT 1"}
     
     
    (throw (IllegalArgumentException.
             (format "Database/adapter type %s is not supported" db-type))))))
