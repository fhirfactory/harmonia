# Copyright (c) 2026 Mark Hunter
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

FROM quay.io/infinispan/server:15.0.3.Final

# Copy infinispan-persistence SPI JAR into Infinispan server lib directory
COPY data-services/infinispan-persistence/target/infinispan-persistence-*.jar /opt/infinispan/server/lib/

# Copy custom Infinispan cluster configuration
COPY data-services/infinispan-cluster/src/main/resources/infinispan.xml /opt/infinispan/server/conf/infinispan.xml
COPY data-services/infinispan-cluster/src/main/resources/users.properties /opt/infinispan/server/conf/users.properties
COPY data-services/infinispan-cluster/src/main/resources/groups.properties /opt/infinispan/server/conf/groups.properties

EXPOSE 11222 7800 46655
ENTRYPOINT ["/opt/infinispan/bin/server.sh", "-c", "infinispan.xml", "-b", "0.0.0.0"]
