#source $(dirname $0)/profile.sh
export JAVA_HOME=$_JAVA_HOME
export WHICH_DATABASE="primary"
export DB_URL="jdbc:mysql://localhost:3306/td1"
export SCHEMA_NAME="td1"
export DB_USERNAME="root"
export DB_PASSWORD="123456"
export DOMAIN_PACKAGE="com.cyd.primary.domain"
mvn liquibase:diff
