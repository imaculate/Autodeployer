
FILE=$1
 
if [ -d $FILE ];
then
   git pull origin master
else
   git clone $2
fi
 
