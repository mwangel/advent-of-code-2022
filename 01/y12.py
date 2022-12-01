#!/usr/bin/python3
with open('data') as f:
    lines = f.readlines()

loads = []
v = 0
for s in lines:
    if not s.strip():
        loads.append( v )
        v = 0
    else:
        v += int(s)

loads.sort()
loads.reverse()
print('python3, day 1, part 1 :', loads[0])
print('python3, day 1, part 2 :', loads[0] + loads[1] + loads[2])
