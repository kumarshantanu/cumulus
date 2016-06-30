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


