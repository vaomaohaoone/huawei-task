echo "Worker ports: $1"
java -jar -Dports="$1" ./client/target/client-1.0-SNAPSHOT.jar