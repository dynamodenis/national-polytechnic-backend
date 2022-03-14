#mvn clean install -DskipTests

#mv ./target/hop-business-management.jar .

git add .

git commit -m "$1"

# shellcheck disable=SC2046
git push origin $(git rev-parse --abbrev-ref HEAD)

