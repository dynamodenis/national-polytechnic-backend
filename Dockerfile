FROM ubuntu:latest
RUN \
# Update
apt-get update -y && \
# Install Java
apt-get install default-jre -y

ADD ./target/nnp-diary.jar nnp-diary.jar

EXPOSE 3505

CMD java -jar nnp-diary.jar
