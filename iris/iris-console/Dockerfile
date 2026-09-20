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

# Build stage
FROM node:20-alpine AS build-stage
WORKDIR /app

# Copy shared library package definition and application package definition
COPY iris-befe/frontend/package*.json ./iris-befe/frontend/
COPY iris-console/package*.json ./iris-console/

# Copy shared library source
COPY iris-befe/frontend ./iris-befe/frontend

# Install dependencies and build iris-console
WORKDIR /app/iris-console
RUN npm install
COPY iris-console .
RUN npm run build

# Production stage
FROM nginx:alpine AS production-stage
COPY --from=build-stage /app/iris-console/dist /usr/share/nginx/html
COPY iris-console/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
