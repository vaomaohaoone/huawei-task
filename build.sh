echo "============ BUILD PACKAGES ============"
mvn clean install
echo "============ BUILD SERVER IMAGE ============"
docker build -t netty-server:stable --build-arg JAR_FILE=./server/target/server-1.0-SNAPSHOT.jar .
echo "============ BUILD SUCCESSFULLY ============"