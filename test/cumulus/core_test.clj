(ns cumulus.core-test
  (:require [clojure.test :refer :all]
    [cumulus.core :refer :all]))

(deftest test-odbc
  (testing "ODBC"
    (is (= {:classname
            "sun.jdbc.odbc.JdbcOdbcDriver",
            :jdbc-url "jdbc:odbc:foo",
            :test-query nil}
          (jdbc-params :odbc {:dsn "foo"})))))

(deftest test-axiondb
  (testing "axiondb"
    (is (= {:classname "org.axiondb.jdbc.AxionDriver",
            :jdbc-url "jdbc:axiondb:foo",
            :test-query "SELECT 1"}
          (jdbc-params :axiondb {:database "foo"})))))

(deftest test-derby
  (testing "derby-memory"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby:memory:foo;create=true;",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :memory 
                               :database "foo"}))))
  (testing "derby-filesys"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby:directory:foo;create=true;",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :filesys 
                               :database "foo"}))))
  (testing "derby-classpath"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url "jdbc:derby:classpath:foo",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :classpath
                               :database "foo"}))))
  (testing "derby-jar"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby:jar:(c:/user)foo",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :jar
                               :database "foo"
                               :jar-path "c:/user"}))))  
  (testing "derby-network"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby://local3002/foo;create=true;",
            :test-query "values(1)"}
        (jdbc-params :derby {:target :network
                             :host "local"
                             :port "3002"
                             :database "foo"})))))

(deftest test-h2
  (testing "h2-memory"
    (is (= {:classname "org.h2.Driver",
            :jdbc-url "jdbc:h2:mem:foo",
            :test-query "SELECT 1"}
          (jdbc-params :h2 {:target :memory
                            :database "foo"}))))
  (testing "h2-filesys"
    (is (= {:classname "org.h2.Driver",
            :jdbc-url "jdbc:h2:file:foo",
            :test-query "SELECT 1"}
        (jdbc-params :h2 {:target :filesys 
                          :database "foo"}))))
  (testing "h2-network"
    (is (= {:classname "org.h2.Driver",
            :jdbc-url "jdbc:h2:tcp:local2030/foo",
            :test-query "SELECT 1"}
          (jdbc-params :h2 {:target :network 
                            :database "foo"
                            :host "local"
                       :port "2030"})))))

(deftest test-hsqldb
  (testing "hsqldb-memory"
    (is (= {:classname "org.hsqldb.jdbcDriver",
            :jdbc-url "jdbc:hsqldb:mem:foo",
            :test-query
            "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"}
          (jdbc-params :hsqldb {:target :memory 
                                :database "foo"}))))
  (testing "hsqldb-filesys"
    (is (= {:classname "org.hsqldb.jdbcDriver",
            :jdbc-url "jdbc:hsqldb:file:foo",
            :test-query
            "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"}
        (jdbc-params :hsqldb {:target :filesys
                              :database "foo"}))))
  (testing "hsqldb-network"
    (is (= {:classname "org.hsqldb.jdbcDriver",
            :jdbc-url
            "jdbc:hsqldb:hsql://local2938/foo",
            :test-query
            "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"}
          (jdbc-params :hsqldb {:target :network 
                                :host "local"
                                :port "2938"
                                :database "foo"})))))

(deftest test-mckoi
  (testing "mckoi"
    (is (= {:classname "com.mckoi.JDBCDriver",
            :jdbc-url "jdbc:mckoi:local://foo",
            :test-query "SELECT 1"}
          (jdbc-params :mckoi {:database "foo"})))))

(deftest test-sqlite
  (testing "sqlite-memory"
    (is (= {:classname "org.sqlite.JDBC",
            :jdbc-url "jdbc:sqlite::memory:",
            :test-query "SELECT 1"}
          (jdbc-params :sqlite {:target :memory}))))
  (testing "sqlite-filesys"
    (is (= {:classname "org.sqlite.JDBC",
            :jdbc-url "jdbc:sqlite:foo",
            :test-query "SELECT 1"}
          (jdbc-params :sqlite {:target :filesys
                                :database "foo"})))))

(deftest test-cubrid
  (testing "cubrid"
    (is (= {:classname
            "cubrid.jdbc.driver.CUBRIDDriver",
						:jdbc-url
      "jdbc:cubrid:localhost:3030:foo",
					  :test-query "SELECT 1;"}
          (jdbc-params :cubrid {:host "localhost"
                                :port "3030"
                                :database "foo"})))))

(deftest test-firebird
  (testing "firebird"
    (is (= {:classname
            "org.firebirdsql.jdbc.FBDriver",
            :jdbc-url
            "jdbc:firebirdsql://local:1234/foo",
            :test-query
            "SELECT CAST(1 AS INTEGER) FROM rdb$database;"}
          (jdbc-params :firebird {:host "local"
                                  :port "1234"
                                  :database "foo"})))))

(deftest test-jtds-sqlserver
  (testing "jtds-sqlserver"
    (is (= {:classname
            "net.sourceforge.jtds.jdbc.Driver",
            :jdbc-url
            "jdbc:jtds:sqlserver://local:2020foo",
            :test-query "select 1;"}
          (jdbc-params :jtds-sqlserver {:host "local"
                                        :port "2020"
                                        :database "foo"})))))

(deftest test-jtds-sybase
  (testing "jtds-sybase"
    (is (= {:classname
            "net.sourceforge.jtds.jdbc.Driver",
            :jdbc-url
            "jdbc:jtds:sybase://local:1223foo",
            :test-query "select 1;"}
          (jdbc-params :jtds-sybase {:host "local"
                                     :port "1223"
                                     :database "foo"})))))

(deftest test-monetdb
  (testing "monetdb"
    (is (= {:classname
            "nl.cwi.monetdb.jdbc.MonetDriver",
            :jdbc-url
            "jdbc:monetdb://localhost:1234/foo",
            :test-query "SELECT 1;"}
          (jdbc-params :monetdb {:host "localhost"
                                 :port "1234"
                                 :database "foo"})))))

(deftest test-mysql
  (testing "mysql"
    (is (= {:classname "com.mysql.jdbc.Driver",
            :jdbc-url
            "jdbc:mysql://localhost :1332/foo",
           :test-query "SELECT 1;"}
          (jdbc-params :mysql {:host "localhost "
                               :port "1332"
                               :database "foo"})))))

(deftest test-postgresql
  (testing "postgresql"
    (is (= {:classname "org.postgresql.Driver",
            :jdbc-url
            "jdbc:postgresql://localhost:1345/foo",
            :test-query "SELECT version();"}
          (jdbc-params :postgresql {:host "localhost"
                                    :port "1345"
                                    :database "foo"})))))

(deftest test-db2
  (testing "db2"
    (is (= {:classname
            "com.ibm.db2.jcc.DB2Driver",
            :jdbc-url "jdbc:db2://local:1234/foo",
            :test-query
            "select * from sysibm.SYSDUMMY1;"}
          (jdbc-params :db2 {:host "local"
                             :port "1234"
                             :database "foo"})))))

(deftest test-oracle
  (testing "oracle-system-id"
    (is (= {:classname
            "oracle.jdbc.driver.OracleDriver",
            :jdbc-url
            "jdbc:oracle:thin:@local:2937:foo",
            :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:target :system-id
                                :database "foo"
                                :host "local"
                                :port "2937"}))))
  (testing "oracle-service-name"
    (is (= {:classname
            "oracle.jdbc.driver.OracleDriver",
            :jdbc-url
            "jdbc:oracle:thin:@//local:2937/foo",
            :test-query "SELECT 1 FROM DUAL"}
        (jdbc-params :oracle 
          {:target :service-name
           :database "foo"
           :host "local"
           :port "2937"}))))
  (testing "oracle-tns-name"
    (is (= {:classname
            "oracle.jdbc.driver.OracleDriver",
            :jdbc-url "jdbc:oracle:thin:@foo",
            :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:target :tns-name
                                :database "foo"}))))
  (testing "oracle-ldap"
    (is (= {:classname
            "oracle.jdbc.driver.OracleDriver",
            :jdbc-url
            "jdbc:oracle:thin:@ldap://local/2937:foo",
            :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:target :ldap
                                :database "foo"
                                :host "local"
                                :port "2937"}))))
  (testing "oracle-oci"
    (is (= {:classname
            "oracle.jdbc.driver.OracleDriver",
            :jdbc-url "jdbc:oracle:oci:@foo",
            :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:target :oci
                                :database "foo"}))))
  (testing "oracle-oci8"
    (is (= {:classname
            "oracle.jdbc.driver.OracleDriver",
            :jdbc-url "jdbc:oracle:oci8:@foo",
            :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:target :oci8
                                :database "foo"})))))

(deftest test-sapdb
  (testing "sapdb"
    (is (= {:classname
            "com.sap.dbtech.jdbc.DriverSapDB",
            :jdbc-url
            "jdbc:sapdb://local:123/foo",
            :test-query "SELECT 1 FROM DUAL"}
    (jdbc-params :sapdb {:host "local"
                         :port "123"
                         :database "foo"})))))

(deftest test-sqlserver
  (testing "sqlserver"
    (is (= {:classname
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            :jdbc-url
            "jdbc:sqlserver://local\\foo:1423",
            :test-query "SELECT 1"}
          (jdbc-params :sqlserver {:host "local"
                       :port "1423"
                       :instance "foo"})))))

(deftest test-sybase
  (testing "sybase"
    (is (= {:classname
            "com.sybase.jdbc2.jdbc.SybDriver",
            :jdbc-url
            "jdbc:sybase:Tds:local:1234?ServiceName=foo",
            :test-query "SELECT 1"}
          (jdbc-params :sybase {:host "local"
                                :port "1234"
                       :database "foo"})))))
