source $(dirname $0)/profile.sh
export JAVA_HOME=$_JAVA_HOME
export DEV_DB_USERNAME=$_DEV_DB_USERNAME && export DEV_DB_PASSWORD=$_DEV_DB_PASSWORD && export DEV_DB_URL=$_DEV_DB_URL && mvn
