(ns cumulus.core-test
  (:require [clojure.test :refer :all]
    [cumulus.core :refer :all]))

(deftest test-odbc
  (testing "ODBC-required"
    (is (= {:classname
            "sun.jdbc.odbc.JdbcOdbcDriver",
            :jdbc-url "jdbc:odbc:foo",
            :test-query nil}
          (jdbc-params :odbc {:dsn "foo"}))
      "All the required keys (dsn) are specified"))
  
  (testing "odbc-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :odbc {})))
    "The required keys are not specified"))


(deftest test-axiondb
  "axiondb-memory"
  (testing "axiondb-memory-reqd"
    (is (={:classname
           "org.axiondb.jdbc.AxionDriver",
           :jdbc-url "jdbc:axiondb:foo",
           :test-query "SELECT 1"}
          (jdbc-params :axiondb {:target :memory 
                                 :database "foo"}))
      "Axiondb-target-memory,the required keys are specified"))
  
  (testing "axiondb-memory-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :axiondb {:target :memory}))
      "Axiondb-target-memory,the required keys are not specified"))
  
  "axiondb-filesys"
  (testing "axiondb-filesys-reqd"
    (is (={:classname
           "org.axiondb.jdbc.AxionDriver",
           :jdbc-url "jdbc:axiondb:foo:c/u",
           :test-query "SELECT 1"}
          (jdbc-params :axiondb {:target :filesys
                                 :database "foo"
                                 :db-path "c/u"}))
      "Axiondb-target-filesys,the required keys are specified"))
  
  (testing "axiondb-filesys-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :axiondb {:target :filesys}))
      "Axiondb-target-filesys,the required keys are not specified"))
  
  (testing "axiondb-filesys-without-reqd-db"
    (is (thrown? IllegalArgumentException
          (jdbc-params :axiondb {:target :filesys
                                 :database "foo"}))
      "Axiondb-target-filesys, all the required keys are not specified (only database is specified)"))
  
  (testing "axiondb-without-reqd-filesys-db-path"
    (is (thrown? IllegalArgumentException
          (jdbc-params :axiondb {:target :filesys
                                 :db-path "c/u"})))
    "Axiondb-target-filesys, all the required keys are not specified (only db-path is specified)"))


(deftest test-derby
  "derby-memory"
  (testing "derby-memory-reqd"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby:memory:foo;create=true;",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :memory 
                               :database "foo"}))
      "Derby-target-memory,the required keys are specified"))
  
  (testing "derby-memory-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :memory}))
      "Derby-target-memory,the required keys are not specified"))
  
  "derby-filesys"
  (testing "derby-filesys-reqd"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby:directory:foo;create=true;",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :filesys
                               :database "foo"}))
      "Derby-target-filesys,the required keys are specified"))
  
  (testing "derby-filesys-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :filesys}))
      "Derby-target-filesys,the required keys are not specified"))
  
  "derby-classpath"
  (testing "derby-classpath-reqd"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url "jdbc:derby:classpath:foo",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :classpath 
                               :database "foo"}))
      "Derby-target-classpath,the required keys are specified"))
  
  (testing "derby-calsspath-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :classpath}))
      "Derby-target-classpath-the required keys are not specified"))
  
  "derby-jar"
  (testing "derby-jar-reqd"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url "jdbc:derby:jar:(c:/u)foo",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :jar 
                               :jar-path "c:/u"
                               :database "foo"}))
      "Derby-target-jar,the required keys are specified"))
  
  (testing "derby-jar-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :jar}))
      "Derby-jar-the required keys are not specified"))
  
  (testing "derby-jar-without-reqd-jar-path"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :jar 
                               :jar-path "c:/u"}))
      "Derby-target-jar,all the required keys are not specified (only jar-path is specified)"))
  
  (testing "derby-jar-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :jar 
                               :database "foo"}))
      "Derby-target-jar,all the required keys are not specified (only database is specified)"))
  
  "derby-network"
  (testing "derby-network-reqd-only"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby://localhost/foo;create=true;",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :network 
                               :host "localhost"
                               :database "foo"}))
      "Derby-target-network,the required keys are specified"))
  
  (testing "derby-network-reqd and opt"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby://local:3002/foo;create=true;",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :network
                               :host "local"
                               :port 3002
                               :database "foo"}))
      "Derby-target-network, all the required and optional keys are specified"))
  
  (testing "derby-network-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :network}))
      "Derby-target-network,the required keys are not specified"))
  
  (testing "derby-network-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :network 
                               :host "local"}))
      "Derby-target-network,all the required keys are not specified (only host is specified)"))
  
  (testing "derby-network-without-reqd-host-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :network 
                               :host "local"
                               :port 123}))
      "Derby-target-network,all the required keys are not specified (only host and port are specified)"))
  
  (testing "derby-network-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :network 
                               :database "foo"}))
      "Derby-target-network,all the required keys are not specified (only database is specified)"))
  
  (testing "derby-network-without-reqd-database-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :network 
                               :database "foo"
                               :port 123}))
      "Derby-target-network,all the required keys are not specified (only database and port are specified)"))
  
  (testing "derby-network-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :network
                               :port 2324})))
    "Derby-target-network,all the required keys are not specified (only port is specified)"))


(deftest test-h2
  "h2-memory"
  (testing "h2-memory-reqd"
    (is (={:classname "org.h2.Driver",
           :jdbc-url "jdbc:h2:mem:foo",
           :test-query "SELECT 1"}
          (jdbc-params :h2 {:target :memory
                            :database "foo"}))
      "h2-target-memory,the required keys are specified"))
  
  (testing "h2-memory-without-redq"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :memory}))
      "h2-target-memory,the required keys are not specified"))
  "h2-filesys"
  (testing "h2-filesys-reqd"
    (is (={:classname "org.h2.Driver",
           :jdbc-url "jdbc:h2:file:foo",
           :test-query "SELECT 1"}
          (jdbc-params :h2 {:target :filesys
                            :database "foo"}))
      "h2-target-filesys,the required keys are specified"))
  
  (testing "h2-filesys-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :filesys}))
      "h2-target-filesys,the required keys are not specified"))
  
  "h2-network"
  (testing "h2-network-reqd"
    (is (={:classname "org.h2.Driver",
           :jdbc-url "jdbc:h2:tcp:local/foo",
           :test-query "SELECT 1"}
          (jdbc-params :h2 {:target :network
                            :database "foo"
                            :host "local"}))
      "h2-target-network,the required keys are specified"))  
  
  (testing "h2-network-reqd and opt"
    (is (= {:classname "org.h2.Driver",
            :jdbc-url
            "jdbc:h2:tcp:local:2030/foo",
            :test-query "SELECT 1"}
          (jdbc-params :h2 {:target :network 
                            :database "foo"
                            :host "local"
                            :port 2030}))
      "h2-target-network,all the required and optional keys are specified"))
  
  (testing "h2-network-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :network}))
      "h2-target-network,the required keys are not specified"))
  
  (testing "h2-notwork-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :network 
                            :port 233}))
      "h2-target-network,all the required keys are not specified (only port is specified)"))
  
  (testing "h2-network-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :network
                            :database "foo"}))
      "h2-target-network,all the required keys are not specified (only database is specified)"))
  
  (testing "h2--network-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :network 
                            :host "local"}))
      "h2-target-network,all the required keys are not specified (only host is specified)"))
  
  (testing "h2-network-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :network 
                            :host "local"
                            :port 2342}))
      "h2-target-network,all the required keys are not specified (only host and port are specified)"))
  
  (testing "h2-network-without-reqd-database and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :network 
                            :database "foo"
                            :port 2334}))
      "h2-target-network,all the required keys are not specified (only database and port are specified)")))


(deftest test-hsqldb
  "hsqldb-memory"
  (testing "hsqldb-memory-reqd"
    (is (= {:classname "org.hsqldb.jdbcDriver",
            :jdbc-url "jdbc:hsqldb:mem:foo",
            :test-query
            "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"}
          (jdbc-params :hsqldb {:target :memory
                                :database "foo"}))
      "hsqldb-target-memory,the required keys are specified"))
  
  (testing "hsqldb-memory-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :memory}))
      "hsqldb-target-memory,the required keys are not specified"))
  
  "hsqldb-filesys"
  (testing "hsqldb-filesys-reqd"
    (is (= {:classname "org.hsqldb.jdbcDriver",
            :jdbc-url "jdbc:hsqldb:file:foo",
            :test-query
            "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"}
          (jdbc-params :hsqldb {:target :filesys 
                                :database "foo"}))
      "hsqldb-target-filesys,the required keys are specified"))
  
  (testing "hsqldb-filesys-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :filesys}))
      "hsqldb-target-filesys,the required keys are specified"))
  
  "hsql-network"
  (testing "hsqldb-network-reqd"
    (is (= {:classname "org.hsqldb.jdbcDriver",
            :jdbc-url
            "jdbc:hsqldb:hsql://local/foo",
            :test-query
            "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"}
          (jdbc-params :hsqldb {:target :network 
                                :database "foo"
                                :host "local"}))
      "hsqldb-target-network,the required parameters are specified"))
  
  (testing "hsqldb-network-reqd and opt"
    (is (= {:classname "org.hsqldb.jdbcDriver",
            :jdbc-url
            "jdbc:hsqldb:hsql://local:2938/foo",
            :test-query
            "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"}
          (jdbc-params :hsqldb {:target :network 
                                :host "local"
                                :port 2938
                                :database "foo"}))
      "hsqldb-target-network,all the required and optional keys are specified"))
  
  (testing "hsqldb-network-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :network}))
      "hsql-target-network,the required keys are not specified"))
  
  (testing "hsqldb-network-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :network 
                                :host "local"}))
      "hsqldb-target-network,all the required keys are not specified (only host is specified)"))
  
  (testing "hsqldb-network-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :network 
                                :host "local"
                                :port 2133}))
      "hsqldb-target-network,all the required keys are not specified (only host and port are specified)"))
  
  (testing "hsqldb-network-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :network
                                :database "foo"}))
      "hsqldb-target-network,all the required keys are not specified (only database is specified)"))
  
  (testing "hsqldb-network-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :network 
                                :port 2143}))
      "hsqldb-target-network,all the required keys are not specified (only port is specified)"))
  
  (testing "hsqldb-network-without-reqd-port and database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :network 
                                :database :foo
                                :port 23214})))
    "hsqldb-target-network,all the required keys are not specified (only port and database are specified)"))


(deftest test-mckoi
  (testing "mckoi"
    (is (= {:classname "com.mckoi.JDBCDriver",
            :jdbc-url "jdbc:mckoi:local://foo",
            :test-query "SELECT 1"}
          (jdbc-params :mckoi {:database "foo"}))
      "All the required and optional keys are specified"))
  
  (testing "mckoi-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mckoi {})))
    "The required parameters aren't specified"))


(deftest test-sqlite
  "sqlite-memory"
  (testing "sqlite-memory"
    (is (= {:classname "org.sqlite.JDBC",
            :jdbc-url "jdbc:sqlite::memory:",
            :test-query "SELECT 1"}
          (jdbc-params :sqlite {:target :memory}))
      "sqlite-target-memory,the required and optional keys are specified"))
  
  "sqlite-filesys"
  (testing "sqlite-filesys-reqd"
    (is (={:classname "org.sqlite.JDBC",
           :jdbc-url "jdbc:sqlite:foo",
           :test-query "SELECT 1"}
          (jdbc-params :sqlite {:target :filesys
                                :database "foo"}))
      "sqlite-target-filesys,the required keys are specified"))
  
  (testing "sqlite-filesys-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sqlite {:target :filesys}))
      "sqlite-target-filesys,the required keys are not specified")))


(deftest test-cubrid
  (testing "cubrid-reqd"
    (is (={:classname
           "cubrid.jdbc.driver.CUBRIDDriver",
           :jdbc-url "jdbc:cubrid:local:foo",
           :test-query "SELECT 1;"}
          (jdbc-params :cubrid {:host "local" :database "foo"}))
      "The required keys are specified"))
  
  (testing "cubrid-reqd and opts"
    (is (= {:classname
            "cubrid.jdbc.driver.CUBRIDDriver",
            :jdbc-url
            "jdbc:cubrid:localhost:3030:foo",
            :test-query "SELECT 1;"}
          (jdbc-params :cubrid {:host "localhost"
                                :port 3030
                                :database "foo"}))
      "All the required and optional keys are specifed"))
  
  (testing "cubrid-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :cubrid {}))
      "None of the required or optional keys are specified"))
  
  (testing "cubrid-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :cubrid {:port 1334}))
      "all the required keys are not specified (only port is specified)"))
  
  (testing "cubrid-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :cubrid {:host "local"}))
      "all the required keys are not specified (only host is specified)"))
  
  (testing "cubrid-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :cubrid {:database "foo"}))
      "all the required keys are not specified (only database is specified)"))
  
  (testing "cubrid-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :cubrid {:host "local"
                                :port 2132}))
      "all the required keys are not specified (only port and host are specified)"))
  (testing "cubrid-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :cubrid {:database "foo"
                                :port 23890}))
      "all the required keys are not specified (only port and database are specified)")))


(deftest test-firebird
  (testing "firebird-reqd-only"
    (is (={:classname
           "org.firebirdsql.jdbc.FBDriver",
           :jdbc-url
           "jdbc:firebirdsql://local/foo",
           :test-query
           "SELECT CAST(1 AS INTEGER) FROM rdb$database;"}
          (jdbc-params :firebird {:host "local" :database "foo"}))
      "The required keys are specified"))
  
  (testing "firebird-reqd and opts"
    (is (= {:classname
            "org.firebirdsql.jdbc.FBDriver",
            :jdbc-url
            "jdbc:firebirdsql://local:1234/foo",
            :test-query
            "SELECT CAST(1 AS INTEGER) FROM rdb$database;"}
          (jdbc-params :firebird {:host "local"
                                  :port 1234
                                  :database "foo"}))
      "All the required and optional keys are specifed"))
  
  (testing "firebird-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :firebird {}))
      "None of the required or optional keys are specified"))
  
  (testing "firebird-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :firebird {:port 1334}))
      "all the required keys are not specified (only port is specified)"))
  
  (testing "firebird-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :firebird {:host "local"}))
      "all the required keys are not specified (only host is specified)"))
  
  (testing "firebird-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :firebird {:database "foo"}))
      "all the required keys are not specified (only database is specified)"))
  (testing "firebird-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :firebird {:host "local"
                                  :port 2132}))
      "all the required keys are not specified (only port and host are specified)"))
  (testing "firebird-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :firebird {:database "foo"
                                  :port 23890}))
      "all the required keys are not specified (only port and database are specified)")))


(deftest test-jtds-sqlserver
  (testing "jtds-sqlserver-reqd-only"
    (is (={:classname
           "net.sourceforge.jtds.jdbc.Driver",
           :jdbc-url
           "jdbc:jtds:sqlserver://localfoo",
           :test-query "select 1;"}
          (jdbc-params :jtds-sqlserver {:host "local" :database "foo"}))
      "The required keys are specified"))
  
  (testing "jtds-sqlserver-reqd and opt"
    (is (= {:classname
            "net.sourceforge.jtds.jdbc.Driver",
            :jdbc-url
            "jdbc:jtds:sqlserver://local:2020foo",
            :test-query "select 1;"}
          (jdbc-params :jtds-sqlserver {:host "local"
                                        :port 2020
                                        :database "foo"}))
      "All the required and optional keys are specified"))
  
  (testing "jtds-sqlserver-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sqlserver {}))
      "None of the required or optional parameters are specified"))
  
  (testing "jtds-sqlserver-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sqlserver {:port 1334}))
      "all the required keys are not specified (only port is specified)"))
  
  (testing "jtds-sqlserver-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sqlserver {:host "local"}))
      "all the required keys are not specified (only host is specified)"))
  
  (testing "jtds-sqlserver-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sqlserver {:database "foo"}))
      "all the required keys are not specified (only database is specified)"))
  
  (testing "jtds-sqlserver-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sqlserver {:host "local"
                                        :port 2132}))
      "all the required keys are not specified (only port and host are specified)"))
  
  (testing "jtds-sqlserver-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sqlserver {:database "foo"
                                        :port 23890}))
      "all the required keys are not specified (only port and database are specified)")))


(deftest test-jtds-sybase
  (testing "jtds-sybase-reqd"
    (is (={:classname
           "net.sourceforge.jtds.jdbc.Driver",
           :jdbc-url
           "jdbc:jtds:sybase://localfoo",
           :test-query "select 1;"}
          (jdbc-params :jtds-sybase {:host "local" :database "foo"}))
      "The required keys are specified"))
  
  (testing "jtds-sybase"
    (is (= {:classname
            "net.sourceforge.jtds.jdbc.Driver",
            :jdbc-url
            "jdbc:jtds:sybase://local:1223foo",
            :test-query "select 1;"}
          (jdbc-params :jtds-sybase {:host "local"
                                     :port 1223
                                     :database "foo"}))
      "All the required and optional keys are specifed"))
  
  (testing "jtds-sybase-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sybase {}))
      "None of the required or optional parameters are specified"))
  
  (testing "jjtds-sybase-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sybase {:port 1334}))
      "all the required keys are not specified (only port is specified)"))
  
  (testing "jtds-sybase-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sybase {:host "local"}))
      "all the required keys are not specified (only host is specified)"))
  
  (testing "jtds-sybase-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sybase {:database "foo"}))
      "all the required keys are not specified (only database is specified)"))
  
  (testing "jtds-sybase-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sybase {:host "local"
                                     :port 2132}))
      "all the required keys are not specified (only port and host are specified)"))
  
  (testing "jtds-sybase-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sybase {:database "foo"
                                     :port 23890}))
      "all the required keys are not specified (only port and database are specified)")))


(deftest test-monetdb
  (testing "monetdb-reqd"
    (is (={:classname
           "nl.cwi.monetdb.jdbc.MonetDriver",
           :jdbc-url "jdbc:monetdb://local/foo",
           :test-query "SELECT 1;"}
          (jdbc-params :monetdb {:host "local" :database "foo"}))
      "The required parameters are specified"))
  
  (testing "monetdb-reqd and opt"
    (is (= {:classname
            "nl.cwi.monetdb.jdbc.MonetDriver",
            :jdbc-url
            "jdbc:monetdb://localhost:1234/foo",
            :test-query "SELECT 1;"}
          (jdbc-params :monetdb {:host "localhost"
                                 :port 1234
                                 :database "foo"}))
      "All the required and optional parameters are specifed"))
  
  (testing "monetdb-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :monetdb {}))
      "None of the required or optional parameters are specified"))
  
  (testing "monetdb-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :monetdb {:port 1334}))
      "all the required keys are not specified (only port is specified)"))
  
  (testing "monetdb-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :monetdb {:host "local"}))
      "all the required keys are not specified (only host is specified)"))
  
  (testing "monetdb-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :monetdb {:database "foo"}))
      "all the required keys are not specified (only database is specified)"))
  
  (testing "monetdb-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :monetdb {:host "local"
                                 :port 2132}))
      "all the required keys are not specified (only port and host are specified)"))
  
  (testing "monetdb-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :monetdb {:database "foo"
                                 :port 23890}))
      "all the required keys are not specified (only port and database are specified)")))


(deftest test-mysql
  (testing "mysql-reqd"
    (is (={:classname "com.mysql.jdbc.Driver",
           :jdbc-url "jdbc:mysql://local/foo",
           :test-query "SELECT 1;"}
          (jdbc-params :mysql {:host "local" :database "foo"}))
      "The required parameters are specified"))
  
  (testing "mysql-reqd and opt"
    (is (= {:classname "com.mysql.jdbc.Driver",
            :jdbc-url
            "jdbc:mysql://localhost :1332/foo",
            :test-query "SELECT 1;"}
          (jdbc-params :mysql {:host "localhost "
                               :port 1332
                               :database "foo"}))
      "All the required and optional parameters are specifed"))
  
  (testing "mysql-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mysql {}))
      "None of the required or optional parameters are specified"))
  
  (testing "mysql-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mysql {:port 1334}))
      "all the required keys are not specified (only port is specified)"))
  
  (testing "mysql-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mysql {:host "local"}))
      "all the required keys are not specified (only host is specified)"))
  
  (testing "mysql-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mysql {:database "foo"}))
      "all the required keys are not specified (only database is specified)"))
  
  (testing "mysql-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mysql {:host "local"
                               :port 2132}))
      "all the required keys are not specified (only port and host are specified)"))
  
  (testing "mysql-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mysql {:database "foo"
                               :port 23890}))
      "all the required keys are not specified (only port and database are specified)")))


(deftest test-postgresql
  (testing "postgresql-reqd"
    (is (={:classname "org.postgresql.Driver",
           :jdbc-url
           "jdbc:postgresql://local/foo",
           :test-query "SELECT version();"}
          (jdbc-params :postgresql {:host "local" :database "foo"}))
      "The required parameters are specified"))
  
  (testing "postgresql-reqd and opt"
    (is (= {:classname "org.postgresql.Driver",
            :jdbc-url
            "jdbc:postgresql://localhost:1345/foo",
            :test-query "SELECT version();"}
          (jdbc-params :postgresql {:host "localhost"
                                    :port 1345
                                    :database "foo"}))
      "All the required and optional parameters are specifed"))
  
  (testing "postgresql-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :postgresql {}))
      "None of the required or optional parameters are specified"))
  
  (testing "postgresql-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :postgresql {:port 1334}))
      "all the required keys are not specified (only port is specified)"))
  
  (testing "postgresql-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :postgresql {:host "local"}))
      "all the required keys are not specified (only host is specified)"))
  
  (testing "postgresql-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :postgresql {:database "foo"}))
      "all the required keys are not specified (only database is specified)"))
  
  (testing "postgresql-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :postgresql {:host "local"
                                    :port 2132}))
      "all the required keys are not specified (only port and host are specified)"))
  
  (testing "postgresql-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :postgresql {:database "foo"
                                    :port 23890}))
      "all the required keys are not specified (only port and database are specified)")))


(deftest test-oracle
  "system-id"
  (testing "oracle-system-id-reqd"
    (is (={:classname
           "oracle.jdbc.driver.OracleDriver",
           :jdbc-url
           "jdbc:oracle:thin:@local:foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :system-id
                                :host "local"
                                :database "foo"}))
      "oracle-system-id all the required keys are specified"))
  
  (testing "oracle-system-id-reqd and opt"
    (is (= {:classname
            "oracle.jdbc.driver.OracleDriver",
            :jdbc-url
            "jdbc:oracle:thin:@local:2937:foo",
            :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :system-id
                                :database "foo"
                                :host "local"
                                :port 2937}))
      "oracle-system-id, all the required and optional keys are specified"))
  
  (testing "oracle-system-id-without-reqd-host"
    (is (thrown? IllegalArgumentException 
          (jdbc-params :oracle {:style :system-id 
                                :host "local"}))
      "oracle-system-id, all the required keys are not specified (only host is specified)"))
  
  (testing "oracle-system-id-without-reqd-host & port"
    (is (thrown? IllegalArgumentException 
          (jdbc-params :oracle {:style :system-id
                                :host "local"
                                :port 324}))
      "oracle-system-id all the required keys are not specified (only host and port are specified)"))
  
  (testing "oracle-systemid-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :system-id 
                                :database "foo"}))
      "oracle-system-id all the required keys are not specified (only database is specified)"))
  
  (testing "oracle-system-id-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :system-id 
                                :database "foo"
                                :port 3242}))
      "oracle-system-id all the required keys are not specified (only port and database is specified)"))
  
  (testing "oracle-system-id-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :system-id}))
      "None of the parameters are specified"))
  "service-name"
  (testing "oracle-service-name-reqd"
    (is (={:classname
           "oracle.jdbc.driver.OracleDriver",
           :jdbc-url
           "jdbc:oracle:thin:@//local/foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :service-name
                                :host "local"
                                :database "foo"}))
      "oracle-service-name all the required keys are specified"))
  
  (testing "oracle-service-name-reqd and opt"
    (is (= {:classname
            "oracle.jdbc.driver.OracleDriver",
            :jdbc-url
            "jdbc:oracle:thin:@//local:2937/foo",
            :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle 
            {:style :service-name
             :database "foo"
             :host "local"
             :port 2937}))
      "oracle-service-name all the required and optional keys are specified"))
  
  (testing "oracle-service-name-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :service-name 
                                :host "local"}))
      "oracle - service-name, all the required keys are not specified (only host is specified)"))
  
  (testing "oracle-service-name-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :service-name
                                :host "local"
                                :port 324}))
      "oracle - service-name, all the required keys are not specified (only port and host are specified)"))
  
  (testing "oracle-service-name-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :service-name
                                :database "foo"}))
      "oracle - service-name, all the required keys are not specified (only database is specified)"))
  
  (testing "oracle-service-name-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :service-name 
                                :database "foo"
                                :port 3242}))
      "oracle - service-name, all the required keys are not specified (only port and database are specified)"))
  
  (testing "oracle-service-name-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :service-name}))
      "None of the parameters are specified"))
  
  "tns-name"
  (testing "oracle-tns-name-reqd-only"
    (is (={:classname
           "oracle.jdbc.driver.OracleDriver",
           :jdbc-url "jdbc:oracle:thin:@foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :tns-name
                                :host "local"
                                :database "foo"}))
      "oracle-tns-name all the required keys are specified"))
  
  (testing "oracle-tns-name-reqd and opt"
    (is (= {:classname
            "oracle.jdbc.driver.OracleDriver",
            :jdbc-url "jdbc:oracle:thin:@foo",
            :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :tns-name
                                :database "foo"}))
      "oracle-tns-name all the required and optional keys are specified"))
  
  (testing "oracle-tns-name-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :tns-name}))
      "oracle-tns-name, none of the required keys specified"))
  
  (testing "orcale-tns-name-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :tns-name 
                                :port 2234}))
      "oracle-tns-name,the required keys are not specified (only port is specified)"))
  
  
  "ldap"
  (testing "oracle-ldap-reqd and opts"
    (is (= {:classname
            "oracle.jdbc.driver.OracleDriver",
            :jdbc-url
            "jdbc:oracle:thin:@ldap://local/:2937:foo",
            :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :ldap
                                :database "foo"
                                :host "local"
                                :port 2937}))
      "oracle-ldap all the required and optional keys are specified"))
  
  (testing "oracle-ldap-reqd-only"
    (is (={:classname
           "oracle.jdbc.driver.OracleDriver",
           :jdbc-url
           "jdbc:oracle:thin:@ldap://local/:foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :ldap
                                :host "local"
                                :database "foo"}))
      "oracle-ldap all the required keys are specified"))
  
  (testing "oracle-ldap-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :ldap
                                :host "local"}))
      "oracle-ldap, the required keys are not specified (only host is specified)"))
  
  (testing "oracle-ldap-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          ((jdbc-params :oracle {:style :ldap
                                 :host "local"
                                 :port 324})))
      "oracle-ldap, the required keys are not specified (only port and port are specified)"))
  
  (testing "oracle-ldap-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :ldap
                                :database "foo"}))
      "oracle-ldap, the required keys are not specified (only database is specified)"))
  
  (testing "oracle-ldap-without-reqd-database and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :ldap
                                :database "foo"
                                :port 3242}))
      "oracle-ldap, the required keys are not specified (only port and database are specified)"))
  
  (testing "oracle-ldap-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :ldap}))
      "None of the keys are specified"))
  
  "oci"
  (testing "oracle-oci-reqd"
    (is (={:classname
           "oracle.jdbc.driver.OracleDriver",
           :jdbc-url "jdbc:oracle:oci:@foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :oci
                                :database "foo"}))
      "oracle-oci all the required keys are specified"))
  (testing "oracle-oci-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :oci}))
      "None of the keys are specified"))
  
  "oci8"
  (testing "oracle-oci8-reqd-only"
    (is (={:classname
           "oracle.jdbc.driver.OracleDriver",
           :jdbc-url "jdbc:oracle:oci8:@foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :oci8
                                :database "foo"}))
      "oracle-oci8 all the required keys are specified"))
  
  (testing "oracle-oci8-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :oci8}))
      "None of the keys are specified")))


(deftest test-db2
  (testing "db2-reqd"
    (is (={:classname
           "com.ibm.db2.jcc.DB2Driver",
           :jdbc-url "jdbc:db2://local/foo",
           :test-query
           "select * from sysibm.SYSDUMMY1;"}
          (jdbc-params :db2 {:host "local" :database "foo"}))
      "The required parameters are specified"))
  
  (testing "db2-reqd and opt"
    (is (= {:classname
            "com.ibm.db2.jcc.DB2Driver",
            :jdbc-url "jdbc:db2://local:1234/foo",
            :test-query
            "select * from sysibm.SYSDUMMY1;"}
          (jdbc-params :db2 {:host "local"
                             :port 1234
                             :database "foo"}))
      "All the required and optional parameters are specifed"))
  
  (testing "db2-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :db2 {}))
      "None of the required or optional parameters are specified"))
   
  (testing "db2-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :db2 {:port 1334}))
      "all the required keys are not specified (only port is specified)"))
  
  (testing "db2-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :db2 {:host "local"}))
      "all the required keys are not specified (only host is specified)"))
  
  (testing "db2-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :db2 {:database "foo"}))
      "all the required keys are not specified (only database is specified)"))
  
  (testing "db2-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :db2 {:host "local"
                             :port 2132}))
      "all the required keys are not specified (only port and host are specified)"))
  
  (testing "db2-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :db2 {:database "foo"
                             :port 23890}))
      "all the required keys are not specified (only port and database are specified)")))


(deftest test-sapdb
  (testing "sapdb-reqd-only"
    (is (={:classname
           "com.sap.dbtech.jdbc.DriverSapDB",
           :jdbc-url "jdbc:sapdb://local/foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :sapdb {:host "local" :database "foo"}))
      "The required parameters are specified"))
  
  (testing "sapdb-reqd and opt"
    (is (= {:classname
            "com.sap.dbtech.jdbc.DriverSapDB",
            :jdbc-url
            "jdbc:sapdb://local:123/foo",
            :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :sapdb {:host "local"
                               :port 123
                               :database "foo"}))
      "All the required and optional parameters are specified"))
  
  (testing "sapdb-without-reqd-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sapdb {}))
      "Since none of the required parameters are specified, this will throw an exception"))
  
  (testing "sapdb-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sapdb {:port 1334}))
      "all the required keys are not specified (only port is specified)"))
  
  (testing "sapdb-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sapdb {:host "local"}))
      "all the required keys are not specified (only host is specified)"))
  
  (testing "sapdb-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sapdb {:database "foo"}))
      "all the required keys are not specified (only database is specified)"))
  
  (testing "sapdb-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sapdb {:host "local"
                               :port 2132}))
      "all the required keys are not specified (only port and host are specified)"))
  (testing "sapdb-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sapdb {:database "foo"
                               :port 23890}))
      "all the required keys are not specified (only port and database are specified)")))


(deftest test-sqlserver
  (testing "sqlserver-opt"
    (is (= {:classname
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            :jdbc-url
            "jdbc:sqlserver://:local:foo:1423",
            :test-query "SELECT 1"}
          (jdbc-params :sqlserver {:host "local"
                                   :port 1423
                                   :instance "foo"}))
      "All optional keys are specified"))
  
  (testing "sqlserver-none"
    (is (={:classname
           "com.microsoft.sqlserver.jdbc.SQLServerDriver",
           :jdbc-url "jdbc:sqlserver://",
           :test-query "SELECT 1"}
          (jdbc-params :sqlserver {}))
      "No keys are specified"))
  
  (testing "sqlserver-host and instance"
    (is (= {:classname
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            :jdbc-url
            "jdbc:sqlserver://:local:inst",
            :test-query "SELECT 1"}
          (jdbc-params :sqlserver {:host "local"
                                   :instance "inst"}))
      "Only two optional keys (host and instance) are specified"))
  
  (testing "sql-server-host and port"
    (is (= {:classname
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            :jdbc-url
            "jdbc:sqlserver://:local:123",
            :test-query "SELECT 1"}
          (jdbc-params :sqlserver {:host "local"
                                   :port 123}))
      "Only two optional keys (port and host) are specified"))
  
  (testing "sql-server-instance and port"
    (is (= {:classname
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            :jdbc-url
            "jdbc:sqlserver://:inst:123",
            :test-query "SELECT 1"}
          (jdbc-params :sqlserver {:instance "inst"
                                   :port 123}))
      "Only two optional keys (port and instance) are specified"))
  
  (testing "sqlserver-host"
    (is (= {:classname
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            :jdbc-url "jdbc:sqlserver://:local",
            :test-query "SELECT 1"}
          (jdbc-params :sqlserver {:host "local"}))
      "Only one optional key (host) is specified"))
  
  (testing "sqlserver-instance"
    (is (= {:classname
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            :jdbc-url "jdbc:sqlserver://:inst",
            :test-query "SELECT 1"}
          (jdbc-params :sqlserver {:instance "inst"}))
      "Only one optional key (instance) is specified"))
  
  (testing "sqlserver-port"
    (is (= {:classname
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            :jdbc-url "jdbc:sqlserver://:123",
            :test-query "SELECT 1"}
          (jdbc-params :sqlserver {:port 123}))
      "Only one optional key (port) is specified")))


(deftest test-sybase
  (testing "sybase-reqd"
    (is (={:classname
           "com.sybase.jdbc2.jdbc.SybDriver",
           :jdbc-url "jdbc:sybase:Tds:local",
           :test-query "SELECT 1"}
          (jdbc-params :sybase {:host "local"}))
      "All the required keys are specified"))
  (testing "sybase-reqd and opt"
    (is (= {:classname
            "com.sybase.jdbc2.jdbc.SybDriver",
            :jdbc-url
            "jdbc:sybase:Tds:local:123?ServiceName=:foo?",
            :test-query "SELECT 1"}
          (jdbc-params :sybase {:host "local"
                                :port 123
                                :database "foo"}))
      "All the required and optional keys are specified"))
  
  (testing "sybase-host and port"
    (is (= {:classname
            "com.sybase.jdbc2.jdbc.SybDriver",
            :jdbc-url "jdbc:sybase:Tds:local:123",
            :test-query "SELECT 1"}
          (jdbc-params :sybase {:host "local"
                                :port 123}))
      "One optional (port) and the reqd params are specified"))
  (testing "sybase-host and database"
    (is (= {:classname
            "com.sybase.jdbc2.jdbc.SybDriver",
            :jdbc-url
            "jdbc:sybase:Tds:local?ServiceName=:foo?",
            :test-query "SELECT 1"}
          (jdbc-params :sybase {:host "local"
                                :database "foo"}))
      "One optional (database) and the required params are specified"))
  
  (testing "sybase-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sybase {:port 2223}))
      "all the required keys are not specified (only port is specified)"))
  (testing "sybase-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sybase {:database "foo"}))
      "all the required keys are not specified (only database is specified)"))
  (testing "sybase-without-reqd-port & database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sybase {:port 2123
                                :database "foo"}))
      "all the required keys are not specified (only port and database are specified)"))
  (testing "sybase-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sybase {}))
      "None of the required keys are specified")))
