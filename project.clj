(defproject bio "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [reagent "0.5.1"]
                 [binaryage/devtools "0.6.1"]
                 [re-frame "0.7.0"]
                 [compojure "1.5.0"]
                 [yogthos/config "0.8"]
                 [ring "1.4.0"]
		 [clj-time "0.12.0"]]

  :plugins [[lein-cljsbuild "1.1.3"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :test-paths ["src/clj/test"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler bio.handler/dev-handler}

  :profiles
  {:dev
   {:dependencies []

    :plugins      [[lein-figwheel "0.5.4-3"]]
    }}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "bio.core/mount-root"}
     :compiler     {:main                 bio.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :jar true
     :compiler     {:main            bio.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}

    ]}

  :main bio.server

  :aot [bio.server]

  :prep-tasks [["cljsbuild" "once" "min"] "compile"]
  )
