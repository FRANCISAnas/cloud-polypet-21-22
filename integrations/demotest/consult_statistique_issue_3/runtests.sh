#!/bin/bash

docker-compose -f docker-compose.consult_stats_issue3.yml up --abort-on-container-exit --exit-code-from testrunner
