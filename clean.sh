#!/usr/bin/env bash

#
# Name: warning
# Date: 2021-01-14
# displays warning message in standard output.
#
warning() {
    tries=1
    maxTries=3

    echo ""
    echo " Recreate SSL Keys & Certificates"
    echo " ------------------------------------------------------------"
    echo ""
    echo " Press Ctrl-c to abort."
    echo ""
    echo -e " \e[5mWARNING:"
    echo -e " \e[0mAll  the  keys   and  certificates  will  be  destroyed!"
    echo " Access to all cipher properties inside any configuration"
    echo " repository will be  lost unless a backup of the keys has"
    echo " been  created  or  you  re-encrypt  all  your  parameter"
    echo " values."
    echo ""
    echo " Are  you  sure  that  you  want to  recreate  the Keys &"
    echo " Certificates?"
    echo ""
    echo -ne " Type RECREATE to confirm: "
    read -r prompt
    while [ ${prompt} != 'RECREATE' -a ${tries} -lt ${maxTries} ]; do
        tries=$((tries+1))
        echo -e '\e[2A'
        echo -ne " Type RECREATE to confirm: \e[K"
        read -r prompt
    done

    if [ ${tries} -eq ${maxTries} ]; then
        exit 1
    fi
}

warning

cd ./src/main/resources/ssl

echo -ne "> Cleaning old resources...             "
rm -fr *
echo -e " [done]"

echo -ne "> Creating Server Keys & Certificates..."
openssl req -newkey rsa:4096 -sha256 -x509 -days 7300 -passout pass:letmein -keyout server.key -out server.crt -subj '/CN=Config Server' -reqexts SAN -config <(cat /etc/ssl/openssl.cnf <(printf "[SAN]\nsubjectAltName=DNS:localhost,IP:127.0.0.1")) -extensions SAN 2>/dev/null
openssl pkcs12 -export -out server.p12 -name ConfigServer -inkey server.key -passin pass:letmein -in server.crt -password pass:letmein
keytool -import -trustcacerts -noprompt -alias ConfigServer -file server.crt -keystore truststore.jks -storetype PKCS12 -storepass changeit 2>/dev/null
echo -e " [done]"

echo -ne "> Creating Client Keys & Certificates..."
openssl req -newkey rsa:4096 -sha256 -out client.csr -passout pass:letmein -keyout client.key -subj '/CN=Config Client' -reqexts SAN -config <(cat /etc/ssl/openssl.cnf <(printf "[SAN]\nsubjectAltName=DNS:localhost,IP:127.0.0.1")) 2>/dev/null
openssl x509 -req -sha256 -days 3650 -in client.csr -CA server.crt -CAkey server.key -passin pass:letmein -CAcreateserial -out client.crt -extensions SAN -extfile <(cat /etc/ssl/openssl.cnf <(printf "[SAN]\nsubjectAltName=DNS:localhost,IP:127.0.0.1")) 2>/dev/null
openssl pkcs12 -export -name ConfigClient -inkey client.key -passin pass:letmein -in client.crt -out client.p12 -caname ConfigServer -CAfile server.crt -certfile server.crt -password pass:letmein
keytool -import -noprompt -alias ConfigClient -file client.crt -keystore truststore.jks -storetype PKCS12 -storepass changeit 2>/dev/null
echo -e " [done]"
