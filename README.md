Karaf-leaderboards-feature
==========================

Packages up all leaderboard bundles into a Karaf feature.

Getting Started
---------------

1. Install [Redis](http://redis.io/) & create a sorted set to use. A quick way to do this is via a Lua script such as :

  ```lua
  local i = 0;

  while ( i < 500000 ) do 
      redis.call("zadd", 'leaderboard', i, "member_"..i) 
      i = i + 1 
  end
  ```

Save this as lb.lua and then from a shell do

  ```bash
  redis-cli --eval lb.lua
  ```

Check this worked by

  ```bash
  redis-cli zcard leaderboard
  ```

You should see `(integer) 500000`

2. Install [Karaf](http://karaf.apache.org/)

Modify the Pax maven url resolver config and add the ninedemons repo. Edit the file `${KARAF_HOME}/etc/org.ops4j.pax.url.mvn.cfg` and modify the value of `org.ops4j.pax.url.mvn.repositories` so it looks like

    org.ops4j.pax.url.mvn.repositories= \
        http://ninedemons-maven-repo.s3.amazonaws.com/snapshot@snapshots, \
        .....

3. Provision Karaf

Start Karaf and install the SCR extension (used by the dummy leaderboard service) :

    karaf@root> features:install eventadmin
    karaf@root> features:install scr

Now install Apache CXF for the REST framework :

    karaf@root> features:addurl mvn:org.apache.cxf.karaf/apache-cxf/2.7.1/xml/features
    karaf@root> features:install cxf
    
Finally, install the leaderboards

    karaf@root> features:addUrl mvn:com.ninedemons.osgi.leaderboards/features/1.0.0-SNAPSHOT/xml/features
    karaf@root> features:install leaderboard-remoting

4. Try out the service 


A users contextual leaderboard

  ```bash
  curl http://localhost:8181/cxf/leaderboard/me/member_99
  ```

A page from the leaderboard

  ```bash
  curl http://localhost:8181/cxf/leaderboard/page/0
  ```

A leaderboard of friends 

  ```bash
  curl "http://localhost:8181/cxf/leaderboard/friends?id=member_99&id=member_1"
  ```

Try the URLs above with both '.xml' and '.json' appended.
