FROM ubuntu:latest
LABEL authors="j"

ENTRYPOINT ["top", "-b"]