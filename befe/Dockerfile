FROM quay.io/wildfly/wildfly:latest-jdk21

# Copy BEFE WAR file to deployments directory
COPY target/befe.war /opt/jboss/wildfly/standalone/deployments/ROOT.war

EXPOSE 8080 9990
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
