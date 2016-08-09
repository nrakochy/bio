# bio

Test driving a cli + webserver in Clojure with fake people data

### Run application:

To start the server-
```
lein do clean, deps, run
```

You can navigate then navigate to localhost:3000.

To run the cli parser, which can take any number of optional paths-to-files-directories
to parse-
```
lein run -m bio.project-reqs "./resources/sample"

```

### Tests:

```
lein test 
```

### Compile 

```
lein do clean, uberjar
```

After compilation, you can run the jar 
```
java -jar target/snapshot-number.jar

```


