(def lol (slurp "https://www.ecobici.df.gob.mx/availability_map/getJsonObject"))


(def url "http://api.labcd.mx/v1/movilidad/estaciones-ecobici")

(def data (json (slurp url)))

(defn parse-location
  [m]
  (if-let [location (:location m)]
    (let [latitude (re-find #"[^, ]+" location)
          longitude (second (re-seq #"[^, ]+" location))]
        (assoc m :latitude latitude :longitude longitude))))

(def result (csv (map parse-location data)))

(spit "ecobicis.csv")

api.labcd.mx/v1/movilidad/estaciones-ecobici/
(defn estacion [s] (str "http://api.labcd.mx/v1/movilidad/estaciones-ecobici/" s))

(defn data-estacion
  ([] (loop [results []
             ids (rest (range))]
        (if-let [r (data-estacion (first ids))]
          (recur (conj results r)
                 (rest ids))
          results)))
  ([id] (try (json (slurp (estacion id)))
             (catch Exception e))))
