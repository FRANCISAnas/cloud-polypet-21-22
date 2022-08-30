#!/bin/bash

kubectl set env deployment customer-care-deployment DEPLOY_DATE="$(date)"

kubectl set env deployment sitemonitoring-deployment DEPLOY_DATE="$(date)"

kubectl set env deployment catalogmanager-deployment DEPLOY_DATE="$(date)"

kubectl set env deployment accounting-deployment DEPLOY_DATE="$(date)"