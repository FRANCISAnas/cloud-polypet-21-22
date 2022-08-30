#!/bin/bash

set -e
#before


docker-compose -f docker-compose.update_inventorie_issue1.yml up --abort-on-container-exit --exit-code-from testrunner