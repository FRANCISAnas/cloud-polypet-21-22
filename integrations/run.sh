#!/bin/bash

set -e

cd demotest/buy_product_issue_1/
bash runtests.sh
docker container rm $(docker container ls -a --quiet)

cd ../update_inventorie_issue_2/
bash runtests.sh
docker container rm $(docker container ls -a --quiet)

cd ../consult_statistique_issue_3/
bash runtests.sh
docker container rm $(docker container ls -a --quiet)