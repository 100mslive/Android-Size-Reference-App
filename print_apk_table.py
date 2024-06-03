import re
import subprocess

command = """find . -name '*.apk' | while read line; do
     ls -l "$line" |  awk '{gsub(/\\.\\/app\\/build\\/outputs\\/apk\\//,"",$9);gsub(/.*release\\/app-[0-9][0-9]-/,"",$9);gsub(/-release-unsigned\\.apk/,"",$9);gsub(/0-without-sdk\\/release\\/app-0-/,"",$9);print}' | awk '{print $5, $9}'
done"""

ret = subprocess.run(command, capture_output=True, shell=True)

text = ret.stdout.decode()

class TableSection:
	name = ""
	x86 = 0
	x8664 = 0
	armeabiv7a = 0
	arm64v8a = 0

archMaps = dict()

for line in text.splitlines():
	l = line.split()
	l[0] = int(l[0]) - 2507294
	name = re.split('-(armeabi-v7a$|x86_64$|x86$|arm64-v8a$)',l[1])
	architecture = name[1]
	libraryName = name[0].replace("-"," ").title()
	archMaps[libraryName] = TableSection()
	archMaps[libraryName].name = libraryName

for line in text.splitlines():
	l = line.split()
	l[0] = int(l[0]) - 2507294
	name = re.split('-(armeabi-v7a$|x86_64$|x86$|arm64-v8a$)',l[1])
	architecture = name[1]
	libraryName = name[0].replace("-"," ").title()
	sizes = round(l[0]/(1024*1024),1)
	if architecture == "x86":
		archMaps[libraryName].x86 = sizes
	elif architecture == "x86_64":
		archMaps[libraryName].x8664 = sizes
	elif architecture == "armeabi-v7a":
		archMaps[libraryName].armeabiv7a = sizes
	elif architecture == "arm64-v8a":
		archMaps[libraryName].arm64v8a = sizes
	else:
		print("Missing arch {} is size {}".format(architecture, sizes))

for a in archMaps:
	if(a == "Without Sdk"):
		continue
	s = archMaps[a]
	print("|  Phone Architecture     	|  "+ s.name + "   |")
	print("| --------------------------------- | ---------  |")
	print("| (armeabi-v7)                      | **{}MB**  |".format(s.armeabiv7a))
	print("| (arm64-v8a) **(most common one)** | **{}MB**  |".format(s.arm64v8a))
	print("| (x86_64)                          | **{}MB**  |".format(s.x8664))
	print("| (x86)                             | **{}MB**  |".format(s.x86))
	print()
	print()

