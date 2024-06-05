import re
import subprocess

command = """find . -name '*.apk' | while read line; do
     ls -l "$line" |  awk '{gsub(/\\.\\/app\\/build\\/outputs\\/apk\\//,"",$9);gsub(/.*release\\/app-[0-9]/,"",$9);gsub(/-release\\.apk/,"",$9);print}' | awk '{print $9, $5}'
done"""

ret = subprocess.run(command, capture_output=True, shell=True)

text = ret.stdout.decode()

class TableSection:
	name = ""
	x86 = 0
	x8664 = 0
	armeabiv7a = 0
	arm64v8a = 0

# Holder for all arch names and sizes for each library
archMaps = dict()

# The size of the apk without any 100ms libraries
baseSdkName = "Android Sdk"
baseApkName = "Without Sdk"
roomkitName = "Room Kit"
sizeInfo = text.splitlines()
sizeInfo.sort()

def removeNumber(line):
	return line.split("-",1)[1]

sizeInfo = map(removeNumber, sizeInfo)

def subtractBy(libraryName, architecture):
	if(libraryName == baseApkName):
		return 0
	elif(libraryName == baseSdkName or libraryName == roomkitName):
		# For both the Core SDK and Room Kit, don't subtract the SDK size.
		return archMaps[baseApkName].x86
	elif architecture == "x86":
		return archMaps[baseSdkName].x86
	elif architecture == "x86_64":
		return archMaps[baseSdkName].x8664
	elif architecture == "armeabi-v7a":
		return archMaps[baseSdkName].armeabiv7a
	elif architecture == "arm64-v8a":
		return archMaps[baseSdkName].arm64v8a
	else:
		print("Missing arch {} is size {}".format(architecture, sizes))


for line in sizeInfo:
	l = line.split()
	l[1] = int(l[1])
	name = re.split('-(armeabi-v7a$|x86_64$|x86$|arm64-v8a$)',l[0])
	architecture = name[1]
	libraryName = name[0].replace("-"," ").title()
	sizes = l[1]
	if libraryName not in archMaps:
		archMaps[libraryName] = TableSection()
		archMaps[libraryName].name = libraryName

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

def sizeof_fmt(num, suffix="B"):
    for unit in ("", "K", "M", "G", "T", "P", "E", "Z"):
        if abs(num) < 1024.0:
            return f"{num:3.1f}{unit}{suffix}"
        num /= 1024.0
    return f"{num:.1f}Yi{suffix}"


for a in archMaps:
	if(a == "Without Sdk"):
		continue
	s = archMaps[a]
	print("|  Phone Architecture     	|  "+ s.name + "   |")
	print("| --------------------------------- | ---------  |")
	print("| (armeabi-v7)                      | **{}**  |".format(sizeof_fmt((s.armeabiv7a - subtractBy(s.name, "armeabi-v7a")))))
	print("| (arm64-v8a) **(most common one)** | **{}**  |".format(sizeof_fmt((s.arm64v8a - subtractBy(s.name, "arm64-v8a")))))
	print("| (x86_64)                          | **{}**  |".format(sizeof_fmt((s.x8664 - subtractBy(s.name, "x86_64")))))
	print("| (x86)                             | **{}**  |".format(sizeof_fmt((s.x86 - subtractBy(s.name, "x86")))))
	print()
	print()

