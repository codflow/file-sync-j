



with publickey


./backup.sh -D [localdir] -S [source-dir] -remote [source-host] -u [username]

for example:
./backup.sh -D ~/sync -S /home/userx/bkexam22 -remote 192.168.13.156 -u root


with password

./backup.sh -D [localdir] -S [source-dir] -remote [source-host] -u [username] -p [password]

for example:
./backup.sh -D ~/sync -S /home/userx/bkexam22 -remote 192.168.13.156 -u root -p password