#!/bin/bash

set -e

function download_gcloud_sdk(){
    mkdir tools
    curl -o tools/google-cloud-sdk.tar.gz https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-364.0.0-linux-x86_64.tar.gz
    tar -xzf tools/google-cloud-sdk.tar.gz -C tools
    
}

if [ ! -d "tools" ]; then
    download_gcloud_sdk
fi


echo "$GOOGLE_CLOUD_CREDENTIALS" | base64 -d > gcp_credentials.json

gcloud=$(pwd)/tools/google-cloud-sdk/bin/gcloud


$gcloud auth activate-service-account --key-file=gcp_credentials.json

rm gcp_credentials.json

$gcloud config set project helpful-envoy-330512


cd back-end/CatalogueManager
$gcloud builds submit --tag gcr.io/helpful-envoy-330512/catalog_manager
cd ../CustomerCare
$gcloud builds submit --tag gcr.io/helpful-envoy-330512/customer_care
cd ../AccountingService
$gcloud builds submit --tag gcr.io/helpful-envoy-330512/accounting
cd ../SiteMonitoring
$gcloud builds submit --tag gcr.io/helpful-envoy-330512/site_monitoring

cd ../../
## 
