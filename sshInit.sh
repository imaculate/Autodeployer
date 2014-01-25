VAR=$(expect -c "expect '' \
  {eval spawn \
  ssh-copy-id -i /home/imaculate/.ssh/id_rsa.pub $1; \
  interact -o -nobuffer -re .*assword.* return; \
  send "$2\r"; send -- "\r"; \
  expect eof;} ")

echo "$VAR"




