#!/bin/bash

CESTA="pumec.zapto.org:8080/Mobile-Royal-Stats-Server_war/rest/apiee/swagger.json"

echo "Generujem z adresy: ${CESTA}"

if [[ ! -f client/swagger-codegen-cli.jar ]]; then
    echo "Stahujem swaggerka"
    mvn dependency:copy -Dartifact=io.swagger:swagger-codegen-cli:2.4.4 -DoutputDirectory=client -Dmdep.stripVersion=true
fi
echo "Mam swaggerka, generujem."

java -jar client/swagger-codegen-cli.jar generate  \
     -i http://${CESTA} \
     -l android \
     -o client

echo "Mam nagenerovane, balim"

cd client

 sed -i.bak -e '14,23d' pom.xml

mvn install

rm -r *

echo "hotovo!!!"