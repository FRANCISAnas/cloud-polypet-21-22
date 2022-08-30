#!/bin/bash

wait-for-it -t 0 $POSTGRES_SERVER_URL:5432
java -jar AccountingService.jar