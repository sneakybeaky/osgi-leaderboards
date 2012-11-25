osgi-leaderboards
=================

Leaderboards in on OSGi container, including Redis backed ones

Installing in Karaf
===================

Install the shared bundle first :

    install -s mvn:com.ninedemons.osgi/leaderboard-shared/1.0.0-SNAPSHOT

Now install the redis pipelined implementation

    install -s mvn:com.ninedemons.osgi/redis-pipelined-leaderboard/1.0.0-SNAPSHOT
