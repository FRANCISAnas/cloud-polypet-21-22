#!/bin/bash
heroku login
heroku container:login
#create an heroku app and give the name below (heroku create)
# shellcheck disable=SC1066
$HEROKU_APP="APP_NAME" #set the real name of your application on heroku!!!!!!!!!!

heroku container:push web -a $HEROKU_APP
heroku container:release web -a $HEROKU_APP


