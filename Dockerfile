FROM openjdk:8
COPY . /usr/app
WORKDIR /usr/app
EXPOSE 8891
ADD run.sh /
RUN chmod +x /run.sh
CMD ["/run.sh"]
