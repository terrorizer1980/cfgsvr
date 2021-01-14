#!/usr/bin/env bash

cd ./src/main/resources/ssl

echo -ne "> Cleaning old resources...             "
rm -fr *
echo -e " [done]"

echo -ne "> Creating Server Keys & Certificates..."
openssl req -newkey rsa:4096 -sha256 -x509 -days 7300 -nodes -keyout server.key -out server.crt -subj '/CN=Config Server' -reqexts SAN -config <(cat /etc/ssl/openssl.cnf <(printf "[SAN]\nsubjectAltName=DNS:localhost,IP:127.0.0.1")) -extensions SAN 2>/dev/null
openssl pkcs12 -export -out server.p12 -name ConfigServer -inkey server.key -in server.crt -password pass:letmein
keytool -import -trustcacerts -noprompt -alias ConfigServer -file server.crt -keystore truststore.jks -storetype PKCS12 -storepass changeit 2>/dev/null
echo -e " [done]"

echo -ne "> Creating Client Keys & Certificates..."
openssl req -newkey rsa:4096 -sha256 -out client.csr -nodes -keyout client.key -subj '/CN=Config Client' -reqexts SAN -config <(cat /etc/ssl/openssl.cnf <(printf "[SAN]\nsubjectAltName=DNS:localhost,IP:127.0.0.1")) 2>/dev/null
openssl x509 -req -sha256 -days 3650 -in client.csr -CA server.crt -CAkey server.key -CAcreateserial -out client.crt -extensions SAN -extfile <(cat /etc/ssl/openssl.cnf <(printf "[SAN]\nsubjectAltName=DNS:localhost,IP:127.0.0.1")) 2>/dev/null
openssl pkcs12 -export -name ConfigClient -inkey client.key -in client.crt -out client.p12 -caname ConfigServer -CAfile server.crt -certfile server.crt -password pass:letmein
keytool -import -noprompt -alias ConfigClient -file client.crt -keystore truststore.jks -storetype PKCS12 -storepass changeit 2>/dev/null
echo -e " [done]"
