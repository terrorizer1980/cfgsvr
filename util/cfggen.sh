#!/usr/bin/env bash

> $2
printf -- "---\nhttp:\n  error:\n    codes:\n      5xx:\n        description: 'Server Error'\n        codes:\n" >> $2
while IFS='|' read -r code description; do
  cipher=$(curl -s --cacert src/main/resources/ssl/server.crt --cert-type P12 -E src/main/resources/ssl/client.p12:letmein -X POST -H 'Content-Type: text/plain' https://localhost:8443/encrypt -d "${description}")
  printf "        - code: %s\n          description: '{cipher}%s'\n" "${code}" "${cipher}" >> $2
done < $1