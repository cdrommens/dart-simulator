FROM eclipse-temurin:21.0.5_11-jre
ARG version=v20.15.0

## INSTALL NODE
RUN apt update -y && apt install curl -y \
&& curl -fsSL https://nodejs.org/dist/$version/node-$version-linux-arm64.tar.gz -o node.tar.gz \
&& tar -xzvf node.tar.gz && rm node.tar.gz

ENV PATH="$PATH:/node-$version-linux-arm64/bin"

## INSTALL DYNAMODB ADMIN
RUN npm install -g dynamodb-admin

## INSTALL DYNAMODB LOCAL
RUN cd /usr/lib && \
    curl -L --insecure --max-time 900 -v https://d1ni2b6xgvw0s0.cloudfront.net/v2.x/dynamodb_local_latest.tar.gz | tar xz
RUN mkdir -p /var/lib/dynamodb
VOLUME /var/lib/dynamodb

RUN apt-get update && \
    apt-get install -y supervisor nginx && \
    apt-get clean && \
    rm -fr /var/lib/apt/lists/* /tmp/* /var/tmp/*

COPY nginx-proxy.conf /etc/nginx-proxy.conf
COPY supervisord.conf /etc/supervisord.conf
RUN mkdir -p /var/log/supervisord

# Configuration for dynamo-admin to know where to hit dynamo.
ENV DYNAMO_ENDPOINT http://localhost:8002/

# For dinghy users.
ENV VIRTUAL_HOST dynamo.docker
ENV VIRTUAL_PORT 8000

# Main proxy on 8000, dynamo-admin on 8001, dynamodb on 8002
EXPOSE 8000 8001 8002

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]