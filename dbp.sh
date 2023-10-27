mvn clean install -DskipTests &&
    docker image rm -f vutiendat3601/goword-api:1.0.0 &&
    docker build -t vutiendat3601/goword-api:1.0.0 -f Dockerfile.local . &&
    docker push vutiendat3601/goword-api:1.0.0
