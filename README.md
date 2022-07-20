## Distributed storage of map objects  
### Requirements: 
1) java with version 10+ 
2) maven 
3) docker 
### Build (check .sh scripts are executable before) 
./build.sh 
### Run server 
./run_server.sh {count_of_workers} {initial_port}  
example: ./run_server.sh 2 8463 
### Run client 
./run_client.sh {list of ports, separated by "|"}  
example: ./run_client.sh "8463|8464" 
#### Links 
https://www.baeldung.com/netty  
https://www.baeldung.com/java-netty-http-server  
https://netty.io  
https://stackoverflow.com  
#### Time spent 
~10 hours 


