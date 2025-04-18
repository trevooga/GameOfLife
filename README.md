# GameOfLife
 
## How to start
`Launch`: you must to compile it `./gradlew bulild`
So `./gradlew run` to `run` the application

## How to control

Press `UP` to up  
Press `DOWN` to down  
Press `LEFT` to left  
Press `RIGHT` to right  
Press `ENTER` to start/stop the game  
Press `SPACE` to toggle the cell  

cat << EOF > /usr/lib/systemd/system/postgresql-15.service
[Unit]
Description=PostgreSQL database server
After=network.target
 
[Service]
Type=forking
 
User=postgres
Group=postgres
 
# Note: avoid inserting whitespace in these Environment= lines, or you may
# break postgresql-setup.
 
# Location of database directory
Environment=PGDATA=/pgdata/15
 
# Maximum number of seconds pg_ctl will wait for postgres to start.  Note that
# PGSTARTTIMEOUT should be less than TimeoutSec value.
Environment=PGSTARTTIMEOUT=270
 
ExecStart=/usr/lib/postgresql/15/bin/pg_ctl start -D \${PGDATA} -s -w -t \${PGSTARTTIMEOUT}
ExecStop=/usr/lib/postgresql/15/bin/pg_ctl stop -D \${PGDATA} -s -m fast
ExecReload=/usr/lib/postgresql/15/bin/pg_ctl reload -D \${PGDATA} -s
 
# Give a reasonable amount of time for the server to start up/shut down.
# Ideally, the timeout for starting PostgreSQL server should be handled more
# nicely by pg_ctl in ExecStart, so keep its timeout smaller than this value.
TimeoutSec=300
 
[Install]
WantedBy=multi-user.target
EOF
