#!/usr/bin/python3.5

import sys
import random
from socket import *

def help():
  s = '''
  server.py - server program for integer stream
  USAGE:
    server.py -h
    server.py <#int> <min> <max>
  OPTIONS:
    -h   get this help page
    <#int> number of bits (default is 1000000)
    <min> minimum delay (default is 5000)
    <max> maximum delay (default is 10000)
  EXAMPLE:
    server.py -h
    server.py 10 1000 2000
  CONTACT:
    Vivek Rajaram, M.S.
  '''
  print(s)
  raise SystemExit(1)

num, min, max = (100000, 5000, 10000)
if len(sys.argv) == 2 and sys.argv[1] == "-h":
  help()
elif len(sys.argv) == 4:
  num = int(sys.argv[1])
  min = int(sys.argv[2])
  max = int(sys.argv[3])
  if num <= 0 or min <= 0:
    help()
  if min > max:
    help()
else:
  help()

s = socket(AF_INET, SOCK_STREAM)
s.bind(('', 0))
host, port = s.getsockname()
print("connect to port number %s" % port)
s.listen(10)
while True:
  client, addr = s.accept()
  print("Got a connection from %s" % str(addr))
  random.seed(32767)
  for i in range(num):
    j = random.randint(0,1)
#    print(j)
    x = str(j)
    x = x + "\n"
    client.send(x.encode('ascii'))
    for k in range(random.randint(min,max)):
      j = j + k
  client.close()