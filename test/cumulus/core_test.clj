(ns cumulus.core-test
  (:require [clojure.test :refer :all]
    [cumulus.core :refer :all]))
;reqd and optional params
(deftest test-odbc
  (testing "ODBC-reqd and opt"
    (is (= {:classname
            "sun.jdbc.odbc.JdbcOdbcDriver",
            :jdbc-url "jdbc:odbc:foo",
            :test-query nil}
          (jdbc-params :odbc {:dsn "foo"}))
      "All the parameters - required and optional are specified"))
  (testing "odbc-reqd-only"
    (is (= {:classname
            "sun.jdbc.odbc.JdbcOdbcDriver",
            :jdbc-url "jdbc:odbc:foo",
            :test-query nil}
          (jdbc-params :odbc {:dsn "foo"}))
      "Only the required parameters are specified"))
  (testing "odbc-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :odbc {})))
    "Should throw an exception if the required parameters aren't specified"))


(deftest test-axiondb
  (testing "axiondb-memory-reqd and opt"
    (is (= {:classname
            "org.axiondb.jdbc.AxionDriver",
            :jdbc-url "jdbc:axiondb:foo",
            :test-query "SELECT 1"}
          (jdbc-params :axiondb {:target :memory 
                                 :database "foo"}))
      "Axiondb - target - memory, all the required and optional paramers are specified"))
  (testing "axiondb-filesys-reqd and opt"
    (is (= {:classname
            "org.axiondb.jdbc.AxionDriver",
            :jdbc-url "jdbc:axiondb:foo:c:/u",
            :test-query "SELECT 1"}
          (jdbc-params :axiondb {:target :filesys 
                                 :database "foo"
                                 :db-path "c:/u"})) 
      "Axiondb - target -filesys, all the required and optional paramers are specified"))
  (testing "axiondb-memory-reqd"
    (is (={:classname
           "org.axiondb.jdbc.AxionDriver",
           :jdbc-url "jdbc:axiondb:foo",
           :test-query "SELECT 1"}
          (jdbc-params :axiondb {:target :memory 
                                 :database "foo"}))
      "Axiondb - target - memory, only the required paramers are specified"))
  (testing "axiondb-filesys-reqd"
    (is (={:classname
           "org.axiondb.jdbc.AxionDriver",
           :jdbc-url "jdbc:axiondb:foo:c/u",
           :test-query "SELECT 1"}
          (jdbc-params :axiondb {:target :filesys
                                 :database "foo"
                                 :db-path "c/u"}))
       "Axiondb - target - filesys, only the required paramers are specified"))
  (testing "axiondb-without-reqd-memory"
    (is (thrown? IllegalArgumentException
          (jdbc-params :axiondb {:target :memory}))
      "Axiondb - target - memory,should throw exception if the required parameters aren't specified"))
  (testing "axiondb-without-reqd-filesys-none"
    (is (thrown? IllegalArgumentException
          (jdbc-params :axiondb {:target :filesys}))
      "Axiondb - target - filesys,should throw exception if the required parameters aren't specified"))
  (testing "axiondb-without-reqd-filesys-db"
    (is (thrown? IllegalArgumentException
          (jdbc-params :axiondb {:target :filesys
                                 :database "foo"}))
      "Axiondb - target - filesys,given only database
       should throw exception as all the required parameters aren't specified (db-path)"))
  (testing "axiondb-without-reqd-filesys-db-path"
    (is (thrown? IllegalArgumentException
          (jdbc-params :axiondb {:target :filesys
                                 :db-path "c/u"})))
    "Axiondb - target - filesys,given only db-path
       should throw exception as all the required parameters aren't specified (database)"))


(deftest test-derby
  (testing "derby-memory-reqd and opt"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby:memory:foo;create=true;",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :memory 
                               :database "foo"}))
      "Derby - target - memory, all the required and optional paramers are specified "))
  (testing "derby-filesys-reqd and opt"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby:directory:foo;create=true;",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :filesys 
                               :database "foo"}))
      "Derby - target - filesys, all the required and optional paramers are specified"))
  (testing "derby-classpath-reqd and opt"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url "jdbc:derby:classpath:foo",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :classpath
                               :database "foo"}))
      "Derby - target -classpath, all the required and optional paramers are specified" ))
  (testing "derby-jar-reqd and opt"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby:jar:(c:/user)foo",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :jar
                               :database "foo"
                               :jar-path "c:/user"}))
      "Derby - target - jar, all the required and optional paramers are specified"))  
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
      "Derby - target - network, all the required and optional paramers are given "))
  (testing "derby-memory-reqd-only"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby:memory:foo;create=true;",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :memory 
                               :database "foo"}))
      "Derby - target - memory, only the required paramers are specified" ))
  (testing "derby-filesys-reqd-only"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby:directory:foo;create=true;",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :filesys
                               :database "foo"}))
      "Derby - target - filesys, only the required paramers are specified"))
  (testing "derby-classpath-reqd-only"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url "jdbc:derby:classpath:foo",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :classpath 
                               :database "foo"}))
      "Derby - target - classpath, only the required paramers are specified"))
  (testing "derby-jar-reqd-only"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url "jdbc:derby:jar:(c:/u)foo",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :jar 
                               :jar-path "c:/u"
                               :database "foo"}))
      "Derby - target - jar, only the required paramers are specified"))
  (testing "derby-network-reqd-only"
    (is (= {:classname
            "org.apache.derby.jdbc.EmbeddedDriver",
            :jdbc-url
            "jdbc:derby://localhost/foo;create=true;",
            :test-query "values(1)"}
          (jdbc-params :derby {:target :network 
                               :host "localhost"
                               :database "foo"}))
      "Derby - target - network, only the required paramers are specified"))
  (testing "derby-without-reqd-memory"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :memory}))
      "Derby - memory - should throw an exception if no required parameters are specified"))
  (testing "derby-without-reqd-filesys"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :filesys}))
      "Derby - filesys - should throw an exception if no required parameters are specified"))
  (testing "derby-without-reqd-classpath"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :classpath}))
      "Derby - classpath - should throw an exception if no required parameters are specified"))
  (testing "derby-without-reqd-jar"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :jar}))
      "Derby - jar - should throw an exception if no required parameters are specified"))
  (testing "derby-jar-without-reqd-jar-path"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :jar 
                               :jar-path "c:/u"}))
      "Derby - jar - given only the jar-path, should throw an exception since all the required parameters aren't specified"))
  (testing "derby-jar-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :jar 
                               :database "foo"}))
      "Derby - jar - given only the database, should throw an exception as all the required parameters aren't specified"))
  (testing "derby-without-reqd-network"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :network}))
      "Derby - network - should throw an exception if no required parameters are specified"))
  (testing "derby-without-reqd-network-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :network 
                               :host "local"}))
      "Derby - network - given only the host, should throw an exception as all the required paramaeters aren't specified"))
  (testing "derby-without-reqd-network-host & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :network 
                               :host "local"
                               :port 123}))
      "Derby - network - given only the host and the port, should throw an exception as all the reqired parameters aren't specified"))
  (testing "derby-without-reqd-network-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :network 
                               :database "foo"}))
      "Derby - network - given only the database, should throw an exception as all the required parameters aren't specified"))
  (testing "derby-without-reqd-network-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :network 
                               :database "foo"
                               :port 123}))
      "Derby - network - given only the database and the port, should throw an exception as all the required parameters aren't specified"))
  (testing "derby-without-reqd-network-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :derby {:target :network
                               :port 2324})))
    "Derby - network - given only the port, should throw an exception as all the required parameters aren't specified"))

(deftest test-h2
  (testing "h2-memory-reqd and opt"
    (is (= {:classname "org.h2.Driver",
            :jdbc-url "jdbc:h2:mem:foo",
            :test-query "SELECT 1"}
          (jdbc-params :h2 {:target :memory
                            :database "foo"}))
      "h2 - memory - given all the required and optional parameters"))
  (testing "h2-filesys-reqd and opt"
    (is (= {:classname "org.h2.Driver",
            :jdbc-url "jdbc:h2:file:foo",
            :test-query "SELECT 1"}
          (jdbc-params :h2 {:target :filesys 
                            :database "foo"}))
      "h2 - filesys - given all the required and optional parameters"))
  (testing "h2-network-reqd and opt"
    (is (= {:classname "org.h2.Driver",
            :jdbc-url
            "jdbc:h2:tcp:local:2030/foo",
            :test-query "SELECT 1"}
          (jdbc-params :h2 {:target :network 
                            :database "foo"
                            :host "local"
                            :port 2030}))
      "h2 - network - given all the required and optional parameters"))
  (testing "h2-memory-reqd-only"
    (is (={:classname "org.h2.Driver",
           :jdbc-url "jdbc:h2:mem:foo",
           :test-query "SELECT 1"}
          (jdbc-params :h2 {:target :memory
                            :database "foo"}))
      "h2 - memory - given only the required parameters"))
  (testing "h2-filesys-reqd-only"
    (is (={:classname "org.h2.Driver",
           :jdbc-url "jdbc:h2:file:foo",
           :test-query "SELECT 1"}
          (jdbc-params :h2 {:target :filesys
                            :database "foo"}))
      "h2 - filesys - given only the required parameters"))
  (testing "h2-network-req-only"
    (is (={:classname "org.h2.Driver",
           :jdbc-url "jdbc:h2:tcp:local/foo",
           :test-query "SELECT 1"}
          (jdbc-params :h2 {:target :network
                            :database "foo"
                            :host "local"}))
      "h2 - network - given only the required parameters"))
  (testing "h2-without redq-memory"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :memory}))
      "h2 - memory - should throw an exception as the required parameters aren't specified"))
  (testing "h2-without-reqd-filesys"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :filesys}))
      "h2 - filesys - should throw an exception as the required parameters aren't specified"))
  (testing "h2-without-reqd-network"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :network}))
      "h2 - network - should throw an exception as the required parameters aren't specified"))
  (testing "h2-without-reqd-network-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :network 
                            :port 233}))
      "h2 - network - given only the port should throw an exception as all the required parameters aren't specified"))
  (testing "h2-without-reqd-network-databse"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :network
                            :database "foo"}))
      "h2 - network - given only the database should throw an exception as all the required parameters aren't specified"))
  (testing "h2-without-reqd-network-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :network 
                     :host "local"}))
      "h2 - network - given only the host should throw an exception as all the required parameters aren't specified"))
  (testing "h2-without-reqd-network-host &port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :network 
                            :host "local"
                            :port 2342}))
      "h2 - network - given only the port and host should throw an exception as all the required parameters aren't specified"))
  (testing "h2-without-reqd-network-database and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :h2 {:target :network 
                            :database "foo"
                            :port 2334}))
      "h2 - network - given only the port and database should throw an exception as all the required parameters aren't specified")))

(deftest test-hsqldb
  (testing "hsqldb-memory-reqd and opt"
    (is (= {:classname "org.hsqldb.jdbcDriver",
            :jdbc-url "jdbc:hsqldb:mem:foo",
            :test-query
            "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"}
          (jdbc-params :hsqldb {:target :memory
                                :database "foo"}))
      "hsqldb - memory - all the required and optional parameters are specified"))
  (testing "hsqldb-filesys-reqd and opt"
    (is (= {:classname "org.hsqldb.jdbcDriver",
            :jdbc-url "jdbc:hsqldb:file:foo",
            :test-query
            "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"}
        (jdbc-params :hsqldb {:target :filesys
                              :database "foo"}))
       "hsqldb - filesys - all the required and optional parameters are specified"))
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
      "hsqldb - network - all the required and optional parameters are specified"))
  (testing "hsqldb-memory-reqd"
    (is (= {:classname "org.hsqldb.jdbcDriver",
            :jdbc-url "jdbc:hsqldb:mem:foo",
            :test-query
            "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"}
          (jdbc-params :hsqldb {:target :memory
                                :database "foo"}))
      "hsqldb - memory - only the required parameters are specified"))
  (testing "hsqldb-filesys-reqd"
    (is (= {:classname "org.hsqldb.jdbcDriver",
            :jdbc-url "jdbc:hsqldb:file:foo",
            :test-query
            "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"}
          (jdbc-params :hsqldb {:target :filesys 
                                :database "foo"}))
      "hsqldb - filesys - only the required parameters are specified"))
  (testing "hsqldb-network-reqd"
    (is (= {:classname "org.hsqldb.jdbcDriver",
            :jdbc-url
            "jdbc:hsqldb:hsql://local/foo",
            :test-query
            "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"}
          (jdbc-params :hsqldb {:target :network 
                                :database "foo"
                                :host "local"}))
      "hsqldb - network - only the required parameters are specified"))
  (testing "hsqldb-without-reqd-memory"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :memory}))
      "hsqldb - memory - throws an exception as none of the required parameters are specified"))
  (testing "hsqldb-without-reqd-filesys"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :filesys}))
      "hsqldb - filesys - throws an exception as none of the required parameters are specified"))
  (testing "hsqldb-without-reqd-network"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :network}))
      "hsqldb - network - throws an exception as none of the required parameters are specified"))
  (testing "hsqldb-without-reqd-network-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :network 
                                :host "local"}))
      "hsqldb - network - given only the host, should throw an exception as all the required parameters aren't specified"))
  (testing "hsqldb-without-reqd-network-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :network 
                                :host "local"
                                :port 2133}))
      "hsqldb - network - given only the host and port, should throw an exception as all the required parameters aren't specified"))
  (testing "hsqldb-without-reqd-network-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :network
                                :database "foo"}))
      "hsqldb - network - given only the database, should throw an exception as all the required parameters aren't specified"))
  (testing "hsqldb-without-reqd-network-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :network 
                                :port 2143}))
      "hsqldb - network - given only the port, should throw an exception as all the required parameters aren't specified"))
  (testing "hsqldb-without-reqd-network-port and database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :hsqldb {:target :network 
                                :database :foo
                                :port 23214})))
    "hsqldb - network - given only the port and database, should throw an exception as all the required parameters aren't specified"))


(deftest test-mckoi
  (testing "mckoi"
    (is (= {:classname "com.mckoi.JDBCDriver",
            :jdbc-url "jdbc:mckoi:local://foo",
            :test-query "SELECT 1"}
          (jdbc-params :mckoi {:database "foo"}))
      "All the required and optional parameters are specified"))
  (testing "mckoi-without reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mckoi {})))
    "Throws an exception as all the required parameters aren't specified"))


(deftest test-sqlite
  (testing "sqlite-memory"
    (is (= {:classname "org.sqlite.JDBC",
            :jdbc-url "jdbc:sqlite::memory:",
            :test-query "SELECT 1"}
          (jdbc-params :sqlite {:target :memory}))
      "sqlite - memory - all the required and optional parameters are specified"))
  (testing "sqlite-filesys"
    (is (= {:classname "org.sqlite.JDBC",
            :jdbc-url "jdbc:sqlite:foo",
            :test-query "SELECT 1"}
          (jdbc-params :sqlite {:target :filesys
                                :database "foo"}))
      "sqlite - filesys - all the required and optional parameters are specified"))
  (testing "sqlite-memory-reqd-only"
    (is (={:classname "org.sqlite.JDBC",
           :jdbc-url "jdbc:sqlite::memory:",
           :test-query "SELECT 1"}
          (jdbc-params :sqlite {:target :memory}))
      "sqlite - memory - only the required parameters are specified"))
  (testing "sqlite-filesys-reqd-only"
    (is (={:classname "org.sqlite.JDBC",
           :jdbc-url "jdbc:sqlite:foo",
           :test-query "SELECT 1"}
          (jdbc-params :sqlite {:target :filesys
                                :database "foo"}))
      "sqlite - filesys - only the required parameters are specified"))
  (testing "sqlite-without-reqd-filesys"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sqlite {:target :filesys}))
      "sqlite - filesys - should throw an exception as the required parameters aren't specified")))


(deftest test-cubrid
  (testing "cubrid-reqd and opts"
    (is (= {:classname
            "cubrid.jdbc.driver.CUBRIDDriver",
            :jdbc-url
            "jdbc:cubrid:localhost:3030:foo",
            :test-query "SELECT 1;"}
          (jdbc-params :cubrid {:host "localhost"
                                :port 3030
                                :database "foo"}))
      "All the required and optional parameters are specifed"))
  (testing "cubrid-reqd-only"
    (is (={:classname
           "cubrid.jdbc.driver.CUBRIDDriver",
           :jdbc-url "jdbc:cubrid:local:foo",
           :test-query "SELECT 1;"}
          (jdbc-params :cubrid {:host "local" :database "foo"}))
      "Only the required parameters are specified"))
  (testing "cubrid-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :cubrid {}))
      "None of the required or optional parameters are specified"))
  (testing "cubrid-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :cubrid {:port 1334}))
      "Only the port is specified, should throw exception as all the required parameters aren't specified"))
  (testing "cubrid-without-reqd-host, should thorw an exception since all the required parameters aren't specifed"
    (is (thrown? IllegalArgumentException
          (jdbc-params :cubrid {:host "local"}))
      "Only the host is specified, should thorw an exception since all the required parameters aren't specifed"))
  (testing "cubrid-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :cubrid {:database "foo"}))
      "Only the databse is specified, should thorw an exception since all the required parameters aren't specifed"))
  (testing "cubrid-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :cubrid {:host "local"
                         :port 2132}))
      "Only the host and port are specified, should thorw an exception since all the required parameters aren't specifed"))
  (testing "cubrid-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :cubrid {:database "foo"
                                :port 23890}))
      "Only the database and port are specified, should thorw an exception since all the required parameters aren't specifed")))


(deftest test-firebird
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
      "All the required and optional parameters are specifed"))
  (testing "firebird-reqd-only"
    (is (={:classname
           "org.firebirdsql.jdbc.FBDriver",
           :jdbc-url
           "jdbc:firebirdsql://local/foo",
           :test-query
           "SELECT CAST(1 AS INTEGER) FROM rdb$database;"}
          (jdbc-params :firebird {:host "local" :database "foo"}))
      "Only the required parameters are specified"))
  (testing "firebird-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :firebird {}))
      "None of the required or optional parameters are specified"))
  (testing "firebird-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :firebird {:port 1334}))
      "Only the port is specified, should throw exception as all the required parameters aren't specified"))
  (testing "firebird-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :firebird {:host "local"}))
      "Only the host is specified, should thorw an exception since all the required parameters aren't specifed"))
  (testing "firebird-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :firebird {:database "foo"}))
      "Only the database is specified, should throw an exception since all the required parameters aren't specified"))
  (testing "firebird-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :firebird {:host "local"
                                  :port 2132}))
      "Only port and host are specified, should throw an exception since all the required parameters aren't specifed"))
  (testing "firebird-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :firebird {:database "foo"
                                  :port 23890}))
      "Only database and port are specifed, should throw an exception since all the required parameters aren't specified")))

(deftest test-jtds-sqlserver
  (testing "jtds-sqlserver-reqd and opt"
    (is (= {:classname
            "net.sourceforge.jtds.jdbc.Driver",
            :jdbc-url
            "jdbc:jtds:sqlserver://local:2020foo",
            :test-query "select 1;"}
          (jdbc-params :jtds-sqlserver {:host "local"
                                        :port 2020
                                        :database "foo"}))
      "All the required and optional parameters are specifed"))
  (testing "jtds-sqlserver-reqd-only"
    (is (={:classname
           "net.sourceforge.jtds.jdbc.Driver",
           :jdbc-url
           "jdbc:jtds:sqlserver://localfoo",
           :test-query "select 1;"}
          (jdbc-params :jtds-sqlserver {:host "local" :database "foo"}))
      "Only the required parameters are specified"))
  (testing "jtds-sqlserver-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sqlserver {}))
      "None of the required or optional parameters are specified"))
  (testing "jtds-sqlserver-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sqlserver {:port 1334}))
      "Only the port is specified, should throw exception as all the required parameters aren't specified"))
  (testing "jtds-sqlserver-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sqlserver {:host "local"}))
      "Only the host is specified, should throw an exception as all the required parameters aren't specified"))
  (testing "jtds-sqlserver-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sqlserver {:database "foo"}))
      "Only database is specified, should throw exception as all the required parameters aren't specified"))
  (testing "jtds-sqlserver-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sqlserver {:host "local"
                         :port 2132}))
      "Only the port and the host are specifed, will throw an exception as all the parameters are not specified"))
  (testing "jtds-sqlserver-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sqlserver {:database "foo"
                         :port 23890}))
      "Only the database and port are specifed, will throw an exception as all the parameters are not specified")))


(deftest test-jtds-sybase
  (testing "jtds-sybase"
    (is (= {:classname
            "net.sourceforge.jtds.jdbc.Driver",
            :jdbc-url
            "jdbc:jtds:sybase://local:1223foo",
            :test-query "select 1;"}
          (jdbc-params :jtds-sybase {:host "local"
                                     :port 1223
                                     :database "foo"}))
      "All the required and optional parameters are specifed"))
  (testing "jtds-sybase-reqd"
    (is (={:classname
           "net.sourceforge.jtds.jdbc.Driver",
           :jdbc-url
           "jdbc:jtds:sybase://localfoo",
           :test-query "select 1;"}
          (jdbc-params :jtds-sybase {:host "local" :database "foo"}))
       "Only the required parameters are specified"))
   (testing "jtds-sybase-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sybase {}))
      "None of the required or optional parameters are specified"))
  (testing "jjtds-sybase-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sybase {:port 1334}))
      "Only the port is specified, should throw exception as all the required parameters aren't specified"))
  (testing "jtds-sybase-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sybase {:host "local"}))
      "Only host is specified, will throw an exception as all the required parameters are not specifed"))
  (testing "jtds-sybase-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sybase {:database "foo"}))
      "Only database is specifed, will throw an exception as all the required parameters are not specified"))
  (testing "jtds-sybase-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sybase {:host "local"
                                     :port 2132}))
      "Only the host and port are specified, will throw an exception as all the required parameters are not specifed"))
  (testing "jtds-sybase-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :jtds-sybase {:database "foo"
                         :port 23890}))
      "Only database is specified, will throw an exception as all the required parameters are not specified")))


(deftest test-monetdb
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
  (testing "monetdb-reqd-only"
    (is (={:classname
           "nl.cwi.monetdb.jdbc.MonetDriver",
           :jdbc-url "jdbc:monetdb://local/foo",
           :test-query "SELECT 1;"}
          (jdbc-params :monetdb {:host "local" :database "foo"}))
      "Only the required parameters are specified"))
  (testing "monetdb-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :monetdb {}))
      "None of the required or optional parameters are specified"))
  (testing "monetdb-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :monetdb {:port 1334}))
      "Only the port is specified, should throw exception as all the required parameters aren't specified"))
  (testing "monetdb-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :monetdb {:host "local"}))
      "Only the host is specified, should throw exception as all the required parameters aren't specified"))
  (testing "monetdb-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :monetdb {:database "foo"}))
      "Only the database is specified, should throw exception as all the required parameters aren't specified"))
  (testing "monetdb-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :monetdb {:host "local"
                                 :port 2132}))
      "Only the port and host are specified, should throw exception as all the required parameters aren't specified"))
  (testing "monetdb-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :monetdb {:database "foo"
                         :port 23890}))
      "Only the port and database are specified, should throw exception as all the required parameters aren't specified")))


(deftest test-mysql
  (testing "mysql-reqd and opt"
    (is (= {:classname "com.mysql.jdbc.Driver",
            :jdbc-url
            "jdbc:mysql://localhost :1332/foo",
            :test-query "SELECT 1;"}
          (jdbc-params :mysql {:host "localhost "
                               :port 1332
                               :database "foo"}))
      "All the required and optional parameters are specifed"))
  (testing "mysql-reqd-only"
    (is (={:classname "com.mysql.jdbc.Driver",
           :jdbc-url "jdbc:mysql://local/foo",
           :test-query "SELECT 1;"}
          (jdbc-params :mysql {:host "local" :database "foo"}))
      "Only the required parameters are specified"))
  (testing "mysql-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mysql {}))
      "None of the required or optional parameters are specified"))
  (testing "mysql-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mysql {:port 1334}))
      "Only the port is specified, should throw exception as all the required parameters aren't specified"))
  (testing "mysql-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mysql {:host "local"}))
      "Only the host is specified, should throw exception as all the required parameters aren't specified"))
  (testing "mysql-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mysql {:database "foo"}))
      "Only the database is specified, should throw exception as all the required parameters aren't specified"))
  (testing "mysql-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mysql {:host "local"
                               :port 2132}))
      "Only the port and host are specified, should throw exception as all the required parameters aren't specified"))
  (testing "mysql-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :mysql {:database "foo"
                               :port 23890}))
      "Only the port and database are specified, should throw exception as all the required parameters aren't specified")))


(deftest test-postgresql
  (testing "postgresql"
    (is (= {:classname "org.postgresql.Driver",
            :jdbc-url
            "jdbc:postgresql://localhost:1345/foo",
            :test-query "SELECT version();"}
          (jdbc-params :postgresql {:host "localhost"
                                    :port 1345
                                    :database "foo"}))
      "All the required and optional parameters are specifed"))
  (testing "postgresql-reqd-only"
    (is (={:classname "org.postgresql.Driver",
           :jdbc-url
           "jdbc:postgresql://local/foo",
           :test-query "SELECT version();"}
          (jdbc-params :postgresql {:host "local" :database "foo"}))
      "Only the required parameters are specified"))
  (testing "postgresql-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :postgresql {}))
      "None of the required or optional parameters are specified"))
  (testing "postgresql-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :postgresql {:port 1334}))
      "Only the port is specified, should throw exception as all the required parameters aren't specified"))
  (testing "postgresql-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :postgresql {:host "local"}))
      "Only the host is specified, should throw exception as all the required parameters aren't specified"))
  (testing "postgresql-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :postgresql {:database "foo"}))
      "Only the database is specified, should throw exception as all the required parameters aren't specified"))
  (testing "postgresql-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :postgresql {:host "local"
                                    :port 2132}))
      "Only the port and host are specified, should throw exception as all the required parameters aren't specified"))
  (testing "postgresql-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :postgresql {:database "foo"
                                    :port 23890}))
      "Only the port and database are specified, should throw exception as all the required parameters aren't specified")))


(deftest test-db2
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
  (testing "db2-reqd-only"
    (is (={:classname
           "com.ibm.db2.jcc.DB2Driver",
           :jdbc-url "jdbc:db2://local/foo",
           :test-query
           "select * from sysibm.SYSDUMMY1;"}
          (jdbc-params :db2 {:host "local" :database "foo"}))
      "Only the required parameters are specified"))
   (testing "db2-without-reqd"
     (is (thrown? IllegalArgumentException
           (jdbc-params :db2 {}))
       "None of the required or optional parameters are specified"))
  (testing "db2-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :db2 {:port 1334}))
      "Only the port is specified, should throw exception as all the required parameters aren't specified"))
  (testing "db2-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :db2 {:host "local"}))
      "Only the host is specified, should throw exception as all the required parameters aren't specified"))
  (testing "db2-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :db2 {:database "foo"}))
      "Only the database is specified, should throw exception as all the required parameters aren't specified"))
  (testing "db2-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :db2 {:host "local"
                             :port 2132}))
      "Only the port and host specified, should throw exception as all the required parameters aren't specified"))
  (testing "db2-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :db2 {:database "foo"
                             :port 23890}))
      "Only the port and database specified, should throw exception as all the required parameters aren't specified")))


(deftest test-oracle
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
      "oracle-system-id all the required and optional params are specified"))
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
      "oracle-service-name all the required and optional params are specified"))
  (testing "oracle-tns-name-reqd and opt"
    (is (= {:classname
            "oracle.jdbc.driver.OracleDriver",
            :jdbc-url "jdbc:oracle:thin:@foo",
            :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :tns-name
                                :database "foo"}))
      "oracle-tns all the required and optional params are specified"))
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
      "oracle-ldap all the required and optional params are specified"))
  (testing "oracle-oci-reqd and opt"
    (is (= {:classname
            "oracle.jdbc.driver.OracleDriver",
            :jdbc-url "jdbc:oracle:oci:@foo",
            :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :oci
                                :database "foo"}))
      "oracle-oci all the required and optional params are specified"))
  (testing "oracle-oci8-reqd and opt"
    (is (= {:classname
            "oracle.jdbc.driver.OracleDriver",
            :jdbc-url "jdbc:oracle:oci8:@foo",
            :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :oci8
                                :database "foo"}))
      "oracle-oci8 all the required and optional params are specified"))
  (testing "oracle-system-id-reqd-only"
    (is (={:classname
           "oracle.jdbc.driver.OracleDriver",
           :jdbc-url
           "jdbc:oracle:thin:@local:foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :system-id
                                :host "local"
                                :database "foo"}))
      "oracle-system-id all the required params are specified"))
  (testing "oracle-service-name-reqd-only"
    (is (={:classname
           "oracle.jdbc.driver.OracleDriver",
           :jdbc-url
           "jdbc:oracle:thin:@//local/foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :service-name
                                :host "local"
                                :database "foo"}))
      "oracle-service-name all the required params are specified"))
  (testing "oracle-tns-name-reqd-only"
    (is (={:classname
           "oracle.jdbc.driver.OracleDriver",
           :jdbc-url "jdbc:oracle:thin:@foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :tns-name
                                :host "local"
                                :database "foo"}))
      "oracle-tns all the required params are specified"))
  (testing "oracle-ldap-reqd-only"
    (is (={:classname
           "oracle.jdbc.driver.OracleDriver",
           :jdbc-url
           "jdbc:oracle:thin:@ldap://local/:foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :ldap
                                :host "local"
                                :database "foo"}))
      "oracle-ldap all the required params are specified"))
  (testing "oracle-oci-reqd-only"
    (is (={:classname
           "oracle.jdbc.driver.OracleDriver",
           :jdbc-url "jdbc:oracle:oci:@foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :oci
                                :database "foo"}))
      "oracle-oci all the required params are specified"))
  (testing "oracle-oci8-reqd-only"
    (is (={:classname
           "oracle.jdbc.driver.OracleDriver",
           :jdbc-url "jdbc:oracle:oci8:@foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :oracle {:style :oci8
                                :database "foo"}))
      "oracle-oci8 all the required params are specified"))
  (testing "oracle-without-reqd-system-id-host"
    (is (thrown? IllegalArgumentException 
          (jdbc-params :oracle {:style :system-id 
                                :host "local"}))
      "oracle-system-id only host is specified, should throw exception as all the required parameters aren't specified"))
  (testing "oracle-without-reqd-system-id-host & port"
    (is (thrown? IllegalArgumentException 
          (jdbc-params :oracle {:style :system-id
                                :host "local"
                                :port 324}))
      "oracle-system-id only host and port are specified, should throw exception as all the required parameters aren't specified"))
  (testing "oracle-without-reqd-systemid-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :system-id 
                                :database "foo"}))
      "oracle-system-id only database is specified, should throw exception as all the required parameters aren't specified"))
  (testing "oracle-without-reqd-system-id-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :system-id 
                                :database "foo"
                                :port 3242}))
      "oracle-system-id only database and port are specified, should throw exception as all the required parameters aren't specified"))
  (testing "oracle-without-reqd-service-name-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :service-name 
                                :host "local"}))
      "oracle - service-name, only host specified, should throw an exception as all the required parameters aren't specified"))
  (testing "oracle-without-reqd-service-name-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :service-name
                                :host "local"
                                :port 324}))
      "oracle - service-name, only host and port are specified, should throw an exception as all the required parameters aren't specified"))
  (testing "oracle-without-reqd-service-name-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :service-name
                                :database "foo"}))
      "oracle - service-name, only database specified, should throw an exception as all the required parameters aren't specified"))
  (testing "oracle-without-reqd-service-name-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :service-name 
                                :database "foo"
                                :port 3242}))
      "oracle - service-name, only database and port are specified, should throw an exception as all the required parameters aren't specified"))
  (testing "oracle-without-reqd-ldap-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :ldap
                                :host "local"}))
      "oracle-ldap, only host specified, should throw an exception as all the required params are not specified"))
  (testing "oracle-without-reqd-ldap-host and port"
    (is (thrown? IllegalArgumentException
          ((jdbc-params :oracle {:style :ldap
                               :host "local"
                               :port 324})))
      "oracle-ldap, only host and port are specified, should throw an exception as all the required params are not specified"))
  (testing "oracle-without-reqd-ldap-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :ldap
                                :database "foo"}))
      "oracle-ldap, only database specified, should throw an exception as all the required params are not specified"))
  (testing "oracle-without reqd-ldap-database and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :ldap
                                :database "foo"
                                :port 3242}))
      "oracle-ldap, only database and port specified, should throw an exception as all the required params are not specified"))
  (testing "oracle-without-reqd-tns-name"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :tns-name}))
      "oracle-tns, none of the required params specified, will throw an exception"))
  (testing "oracle-without-reqd-oci"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :oci}))
      "oracle-oci, none of the required params specified, will throw an exception"))
  (testing "oracle-without-reqd-oci8"
    (is (thrown? IllegalArgumentException
          (jdbc-params :oracle {:style :oci8}))
      "oracle-oci8, none of the required params specified, will throw an exception")))


(deftest test-sapdb
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
  (testing "sapdb-reqd-only"
    (is (={:classname
           "com.sap.dbtech.jdbc.DriverSapDB",
           :jdbc-url "jdbc:sapdb://local/foo",
           :test-query "SELECT 1 FROM DUAL"}
          (jdbc-params :sapdb {:host "local" :database "foo"}))
      "All the required parameters are specified"))
  (testing "sapdb-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sapdb {}))
      "Since none of the required parameters are specified, this will throw an exception"))
  (testing "sapdb-without-reqd-port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sapdb {:port 1334}))
      "Only the port is specified, should throw exception as all the required parameters aren't specified"))
  (testing "sapdb-without-reqd-host"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sapdb {:host "local"}))
      "Only the host is specified, should throw exception as all the required parameters aren't specified"))
  (testing "sapdb-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sapdb {:database "foo"}))
      "Only the database is specified, should throw exception as all the required parameters aren't specified"))
  (testing "sapdb-without-reqd-host and port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sapdb {:host "local"
                               :port 2132}))
      "Only the port and host specified, should throw exception as all the required parameters aren't specified"))
  (testing "sapdb-without-reqd-database & port"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sapdb {:database "foo"
                               :port 23890}))
      "Only the port and database specified, should throw exception as all the required parameters aren't specified")))


(deftest test-sqlserver
  (testing "sqlserver-reqd and opt"
    (is (= {:classname
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            :jdbc-url
            "jdbc:sqlserver://:local:foo:1423",
            :test-query "SELECT 1"}
          (jdbc-params :sqlserver {:host "local"
                                   :port 1423
                                   :instance "foo"}))
      "All optional keys are specified"))
  (testing "sqlserver-reqd-only"
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
  (testing "sybase-reqd and opt"
    (is (= {:classname
            "com.sybase.jdbc2.jdbc.SybDriver",
            :jdbc-url
            "jdbc:sybase:Tds:local:123?ServiceName=:foo?",
            :test-query "SELECT 1"}
          (jdbc-params :sybase {:host "local"
                         :port 123
                         :database "foo"}))
      "All the required and optional params are specified"))
  (testing "sybase-reqd-only"
    (is (={:classname
           "com.sybase.jdbc2.jdbc.SybDriver",
           :jdbc-url "jdbc:sybase:Tds:local",
           :test-query "SELECT 1"}
          (jdbc-params :sybase {:host "local"}))
      "All the required params are specified"))
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
      "Only port is specified, should throw an exception as all the parameters are not specified"))
  (testing "sybase-without-reqd-database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sybase {:database "foo"}))
      "Only the database is specified, should throw an exception as all the required parameters are not specified"))
  (testing "sybase-without-reqd-port & database"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sybase {:port 2123
                         :database "foo"}))
      "Only port and database are specified, should throw an exception as all the required parameters are not specified"))
  (testing "sybase-without-reqd"
    (is (thrown? IllegalArgumentException
          (jdbc-params :sybase {}))
      "None of the required parameters are specified, this should throw an exception")))
