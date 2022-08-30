#!/bin/bash
set -e


cd ../back-end/AccountingService||return

docker build -t teamj-archi-cloud-accounting-service .

cd ../CatalogueManager||return

docker build  -t teamj-archi-cloud-catalogue-manager .

cd ../CustomerCare||return

docker build  -t teamj-archi-cloud-customer-care .

cd ../SiteMonitoring||return

docker build  -t teamj-archi-site-monitoring .

# External Services containers
cd ../../external-services
cd Bank||return
docker build -f Dockerfile -t teamj-archi-cloud-bank .



cd ../delivery||return

docker build  -t teamj-archi-cloud-delivery .

cd ../partner||return
docker build -f Dockerfile -t teamj-archi-cloud-partner .

# Tests containers
cd ../../
cd integrations/demotest/buy_product_issue_1||return
docker build -f update_inventorie_issue_1.dockerfile -t polypet_j_test_buy_product_issue_1 .

cd ../update_inventorie_issue_2||return
docker build -f update_inventorie_issue_2.dockerfile -t polypet_j_test_update_products_issue_2 .

cd ../consult_statistique_issue_3||return
docker build -f consult_stats_issue_3.dockerfile -t polypet_j_test_consult_stats_issue_3 .

# Database containers
cd ../../../
cd postgresqldb
docker build -t polypet-postgresdb .

