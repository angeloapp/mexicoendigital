(use 'clj-excel.core)

(def scian (flatten (digitalize (vecs->maps ((-> "/Users/nex/Downloads/est_ingles.xlsx" workbook-hssf lazy-workbook) "Español-Inglés")))))

(defn remove-lasT
  "If the string ends with a T, remove it"
  [s]
  (if (.endsWith s "T")
    (s/join (butlast s))
    s))

(defn cleanT [o]
  (if (string? o)
    (remove-lasT o)
    o))

(def cleanscian (map #(zipmap (keys %) (map cleanT (vals %)))
                     scian))


;; le process

(defn padre
  "A es padre de b"
  [a b]
  (.startsWith (:codigo b) (:codigo a)))

(defn busca-categoria [data cat]
  (filter #(.startsWith  (:codigo-de-la-clase-de-actividad-scian %) cat)
          data))

q
(def df (csv "/Users/nex/github/webvr-cities/data/denue-cdmx.csv"))
(def scian (csv "/Users/nex/git/dora/scian.csv"))

(defn denue-keys [m]
  (s/join "," (map name (keys (digitalize (zipmap (keys m) (keys m)))))))

(def categorias-top (filter #(> 100 (read-string (:codigo %))) scian))

(defn csv-codigo [data codigo]
  (csv (str "denues/denue-df-" codigo ".csv")
       (busca-categoria data codigo)))

(defn denue-df [s]
  (s/replace "denues/denue-df-{s}.csv" #"s" s))

(defn merge-csvs [a & others]
  (csv a (apply concat (csv a) (map csv others)))) ;TODO remove others
