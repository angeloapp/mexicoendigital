(ns )

                                        ;downlaod and uncompress http://www.openwlanmap.org/db/db.tar.bz2

(wget "http://www.openwlanmap.org/db/db.tar.bz2")

;arriba izq 19.470456,-99.1707797
 ;abajo der 19.304125,-99.0735657

(defn is-in-cdmx? [m]
  (let [lat (:lat m)
        lon (:lon m)]
    (and (> lat 19.304125)
         (< lat 19.470456)
         (> lon -99.1707797)
         (< lon -99.0735657))))

(def wi (map digitalize (tsv "/Users/nex/Downloads/db/db-smaller.csv")))

(def wicdmx (filter is-in-cdmx? wi))
