echo "Count of workers: $1 from port $2"
initial_port=$2;
for (( i = 0; i < $1; i++ )); do
  docker run -d -p "$initial_port":"$initial_port" -e PORT="$initial_port" netty-server:stable
  initial_port=$((initial_port+1))
done
echo "$1 workers deployed from $2 to $((initial_port-1)) ports inclusively"
