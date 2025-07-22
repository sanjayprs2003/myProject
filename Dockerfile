FROM mysql:8.0

# MySQL environment variables
ENV MYSQL_USER=newuser
ENV MYSQL_PASSWORD=password
ENV MYSQL_DATABASE=Project
ENV MYSQL_ROOT_PASSWORD=rootpassword

# Set locale
ENV LANG en_US.utf8

# Change MySQL port to 8080
RUN echo "[mysqld]\nport=8080" > /etc/mysql/conf.d/custom-port.cnf

# Copy initialization SQL
COPY project_dump.sql /docker-entrypoint-initdb.d/

# Expose new port
EXPOSE 8080

# Start MySQL (default CMD is fine)
CMD ["mysqld"]
