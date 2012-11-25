Redis Pipelined Leaderboards
============================

Leaderboard implementation using pipelined Redis commands

To run the integration tests make sure you have a Redis server running. If this is on localhost port 6379 you can simply
 do

    mvn clean verify -Pinttests

If they're any different then do


    mvn clean verify -Dredis.host=<hostname> -Dredis.port=<port> -Pinttests

The reports will be generated in target/failsafe-reports/html.

