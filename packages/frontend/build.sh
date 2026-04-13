#!/bin/sh
set -e

cd ../
mkdir output
cp -R ./sohawgi_Front/* ./output
cp -R ./output ./sohawgi_Front/

cd sohawgi_Front || exit 1

npm install
CI=false npm run build