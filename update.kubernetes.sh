#!/bin/bash

gcloud=$(pwd)/tools/google-cloud-sdk/bin/gcloud

$gcloud components install kubectl
#kubectl

$gcloud container clusters get-credentials polypet-cluster --zone europe-west1-d --project helpful-envoy-330512

envsubst < kubernetes.yaml | kubectl apply -f -

echo "Updating deployments"
bash update_deployment.sh