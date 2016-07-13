# cumulus

A Clojure library to obtain JDBC connection parameters for popular databases and JDBC drivers.


## Usage

Leiningen coordinates: `[cumulus "0.1.0-SNAPSHOT"]`

_Not on Clojars yet. Build locally and run `lein install`._


### Quickstart

```clojure
(require '[cumulus.core :as c])

;; MySQL connection params
(c/jdbc-params :mysql {:host "localhost"
<<<<<<< HEAD
=======
                       :port 3020
>>>>>>> b4d50413030030f1409ec7588d56bf2f78047999
                       :database "foo"
                       :username "dbuser"
                       :password "s3cr3t"})
```


### Generic JDBC connections

| `:adapter`     | Required keys            | Desired keys  |
|----------------|--------------------------|---------------|
| `:jdbc`        | `:classname` `:jdbc-url` | `:test-query` |
| `:subprotocol` | `:classname` `:subname`  | `:test-query` |


### ODBC connections (likely applicable for the Windows platform)

| `:adapter`   | Required keys | Optional keys |
|--------------|---------------|---------------|
| `:odbc`      | `:dsn`        | `:lite?`      |
| `:odbc-lite` | `:dsn`        |               |


### Open Source embedded databases

| Database | `:adapter` | `:target`  | Required keys           | Optional keys |
|----------|------------|------------|-------------------------|---------------|
| Axion    | `:axiondb` | `:memory`  | `:database`             |               |
|          |            | `:filesys` | `:database` `:db-path`  |               |
| Derby    | `:derby`   | `:memory`  | `:database`             |               |
|          |            | `:filesys` | `:database`             |               |
|          |            |`:classpath`| `:database`             |               |
|          |            | `:jar`     | `:jar-path` `:database` |               |
|          |            | `:network` | `:host` `:database`     | `:port`       |
| H2       | `:h2`      | `:memory`  | `:database`             |               |
|          |            | `:filesys` | `:database`             |               |
|          |            | `:network` | `:host` `:database`     | `:port`       |
| HSQLDB   | `:hsqldb`  | `:memory`  | `:database`             |               |
|          |            | `:filesys` | `:database`             |               |
|          |            | `:network` | `:host` `:database`     | `:port`       |
| Mckoi    | `:mckoi`   |            | `:database`             |               |
| SQLite   | `:sqlite`  | `:memory`  |                         |               |
|          |            | `:filesys` | `:database`             |               |


### Open Source drivers, network connections

| Database             | `:adapter`        | Required keys       | Optional keys |
|----------------------|-------------------|---------------------|---------------|
| CUBRID               | `:cubrid`         | `:host` `:database` | `:port`       |
| Firebird             | `:firebird`       | `:host` `:database` | `:port`       |
| SQL Server (jTDS)    | `:jtds-sqlserver` | `:host` `:database` | `:port`       |
| Sybase (jTDS)        | `:jtds-sybase`    | `:host` `:database` | `:port`       |
| MonetDB              | `:monetdb`        | `:host` `:database` | `:port`       |
| MySQL                | `:mysql`          | `:host` `:database` | `:port`       |
| PostgreSQL (official)| `:postgresql`     | `:host` `:database` | `:port`       |
| PostgreSQL (PgSQL)   | `:pgsql`          | `:host` `:database` | `:port`       |


### Proprietary Oracle drivers (`:adapter` = `:oracle`, default `:style` = `:system-id`)

|`:style`       | Required keys                       | Optional keys |
|---------------|-------------------------------------|---------------|
|`:system-id`   | `:host`, `:database`/`:system-id`   | `:port`       |
|`:service-name`| `:host`, `:database`/`:service-name`| `:port`       |
|`:tns-name`    | `:database`/`:tns-name`             | `:port`       |
|`:ldap`        | `:host`, `:database`/`:system-id`/`:service-name`, `:ldap-str` | `:port` |
|`:oci`         | `:database`/`:tns-alias`            |               |
|`:oci8`        | `:database`/`:tns-alias`            |               |


### Other proprietary drivers, network connections

| Database   | `:adapter`   | Required keys                    | Optional keys |
|------------|--------------|----------------------------------|---------------|
| IBM DB2    | `:db2`       | `:host` `:database`              | `:port`       |
| SapDB      | `:sapdb`     | `:host` `:database`              | `:port`       |
| SQL Server | `:sqlserver` |                                  | `:host` `:instance` `:port` |
| Sybase     | `:sybase`    | `:host`                          | `:port` `:database` |



## License

Copyright © 2016 Shantanu Kumar (kumar.shantanu@gmail.com, shantanu.kumar@concur.com)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version
