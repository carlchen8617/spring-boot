#!/bin/bash

ip=$(ifconfig | grep -A3 en0 | grep inet | sed  s/netmask.*//g | sed -e s/inet//g | awk '{ print $1 }'
)
echo $ip
