# Change Log
Release notes and TODO items.

## 0.1.2 / 2016-July-??

* Fix wrong driver classname in Derby network connections
* Validate test query as either string or unspecified
* Infer adapter name for implicit types


## 0.1.1 / 2016-July-14

* Fix bad arity call in `assert-as` (@NandhithaR)


## 0.1.0 / 2016-July-14

Extracted from [clj-dbcp](https://github.com/kumarshantanu/clj-dbcp) version `0.8.1`:

* Support for in-memory database drivers (@NandhithaR)
  * Axion
  * Derby
  * H2
  * HSQLDB
  * SQLite
* Support for filesystem database drivers (@NandhithaR)
  * Axion
  * Derby
  * H2
  * HSQLDB
  * SQLite
* Support for network OSS database drivers (@NandhithaR)
  * Derby
  * H2
  * HSQLDB
  * CUBRID
  * Firebird
  * SQL Server (jTDS)
  * Sybase (jTDS)
  * MonetDB
  * MySQL
  * PostgreSQL
  * PgSQL (http://impossibl.github.io/pgjdbc-ng/)
* Support for network proprietary database drivers (@NandhithaR)
  * Oracle
  * IBM DB2
  * SapDB
  * SQL Server
  * Sybase
