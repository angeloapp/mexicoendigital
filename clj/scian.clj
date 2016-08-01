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

(defn padre
  "A es padre de b"
  [a b]
  (.startsWith (:codigo b) (:codigo a)))

;; le process




(def df (csv "denue.csv"))
(def scian (csv "scian.csv"))

(defn busca-categoria [data cat]
  (filter #(.startsWith  (:codigo-de-la-clase-de-actividad-scian %) cat)
          data))

(defn busca-cat [data cat]
  (filter #(.startsWith  (:codigo %) cat)
          data))

(defn denue-keys [m]
  (s/join "," (map name (keys (digitalize (zipmap (keys m) (keys m)))))))

(def categorias-top (filter #(> 100 (read-string (:codigo %))) scian))

(defn assoc-cat [cat order]
  (assoc cat (keyword (str "codigo-" order)) (s/join (take order (:codigo cat)))))

(def categorias-2
  (let [dos (filter #(> 1000 (read-string (:codigo %))) scian)
        uno (filter #(> 100 (read-string (:codigo %))) scian)]
    (map #(assoc-cat % 2)
         (difference (set dos) (set uno)))))

(defn csv-codigo [data codigo]
  (csv (str "denue/" codigo ".csv")
       (busca-categoria data codigo)))

(defn denue-df [id]
  (s/replace "denue/id.csv" #"id" (str id)))

(defn merge-csvs [a & others]
  (csv a (apply concat (csv a) (map csv others)))) ;TODO remove others

(defn categorias-menu
  [nombres]
  (zipmap (map #(-> % standard-keyword name) nombres)
          nombres))

(def codigos '({:codigo "11",
               :nombre
               "Agricultura, cría y explotación de animales, aprovechamiento forestal, pesca y caza"}
              {:codigo "21", :nombre "Minería"}
              {:codigo "22",
               :nombre
               "Generación, transmisión y distribución de energía eléctrica, suministro de agua y de gas por ductos al consumidor final"}
              {:codigo "23", :nombre "Construcción"}
              {:codigo "31", :nombre "Industrias manufactureras"}
              {:codigo "32", :nombre "Industrias manufactureras"}
              {:codigo "33", :nombre "Industrias manufactureras"}
              {:codigo "43", :nombre "Comercio al por mayor"}
              {:codigo "46", :nombre "Comercio al por menor"}
              {:codigo "48", :nombre "Transportes, correos y almacenamiento"}
              {:codigo "49", :nombre "Transportes, correos y almacenamiento"}
              {:codigo "51", :nombre "Información en medios masivos"}
              {:codigo "52", :nombre "Servicios financieros y de seguros"}
              {:codigo "53",
               :nombre
               "Servicios inmobiliarios y de alquiler de bienes muebles e intangibles"}
              {:codigo "54",
               :nombre "Servicios profesionales, científicos y técnicos"}
              {:codigo "55", :nombre "Corporativos"}
              {:codigo "56",
               :nombre
               "Servicios de apoyo a los negocios y manejo de residuos y desechos, y servicios de remediación"}
              {:codigo "61", :nombre "Servicios educativos"}
              {:codigo "62", :nombre "Servicios de salud y de asistencia social"}
              {:codigo "71",
               :nombre
               "Servicios de esparcimiento culturales y deportivos, y otros servicios recreativos"}
              {:codigo "72",
               :nombre
               "Servicios de alojamiento temporal y de preparación de alimentos y bebidas"}
              {:codigo "81",
               :nombre "Otros servicios excepto actividades gubernamentales"}
              {:codigo "93",
               :nombre
               "Actividades legislativas, gubernamentales, de impartición de justicia y de organismos internacionales y extraterritoriales"}))

(print (options (categorias-menu (map :nombre codigos))))

;!
(defn name-changer [route f s]
  (mv (str route) (f s)))

(defn denue-file [id]
  (str "denue/" id ".csv"))

(defn denue-file& [id]
  (str "denue/" id ".csv"))

(defn denue-name-changer [id nombre]
  (mv (denue-file id) (denue-file nombre)))

(defn categorias-csv-renames
  [cats]
  (map #(vector (:codigo %) (-> (:nombre %) standard-keyword name))
       cats))

(defn make-category-files [data codigos]
  (pmap (fn [[id nombre]] (csv (str "denue/" nombre ".csv")
                        (busca-categoria data id)))
       (categorias-csv-renames codigos)))

(defn idiomatic-string [s]
  (name (standard-keyword s)))

(defn add-slug [cats]
  (map #(assoc % :nombre (idiomatic-string (:nombre %)))
       cats))

(defn second-level-resolution
  [data cats1 cats2]
  (let [cats1& (add-slug cats1)
        cats2& (add-slug cats2)]
    ;(make-category-files data cats2)
    (map vizi-add-denue
         (map :nombre cats2&))
    (map #(make-dataviz (:nombre %) (map :nombre (busca-cat cats2& (:codigo %))))
         cats1&)))
    ; falta agregar al html del menu)
