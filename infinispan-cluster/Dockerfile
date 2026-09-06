FROM quay.io/infinispan/server:15.0.3.Final

# Copy persistence-tier SPI JAR into Infinispan server lib directory
COPY persistence-tier/target/persistence-tier-*.jar /opt/infinispan/server/lib/

# Copy custom Infinispan cluster configuration
COPY infinispan-cluster/src/main/resources/infinispan.xml /opt/infinispan/server/conf/infinispan.xml
COPY infinispan-cluster/src/main/resources/users.properties /opt/infinispan/server/conf/users.properties
COPY infinispan-cluster/src/main/resources/groups.properties /opt/infinispan/server/conf/groups.properties

EXPOSE 11222 7800 46655
ENTRYPOINT ["/opt/infinispan/bin/server.sh", "-c", "infinispan.xml", "-b", "0.0.0.0"]
