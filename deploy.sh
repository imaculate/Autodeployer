ln -s $1 project
cd project
. ~/.gvm/bin/gvm-init.sh
gvm use grails $2
grails clean
grails war 

warFile=$(find . -name '*.war')
cp -r $warFile  $3
#cp target/$3 $4
#ssh $4 
#scp -r $3 $5
#exit
#list = $(find $directory -type f -name \*.war)
