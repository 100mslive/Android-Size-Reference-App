#!/usr/bin/env python3
import re
import subprocess
import sys

prefixedArticle = """---
title: SDK Size Impact
nav: 3.4
---
Each library has its own size impact that can vary been ABIs (x86, arm64-v8a etc), the table below lists them all.

You can take a look at the reference project [here](https://github.com/100mslive/Android-Size-Reference-App/)

These are accuarate for sdk version `HMS_SDK_VERSION_PLACEHOLDER` and room-kit version `HMS_ROOMKIT_VERSION_PLACEHOLDER`.

## Increase in Android APK size:

"""

roomKitHeader = """
### Room Kit
"""

roomKitFooter = """
The `room-kit` module already includes the following libraries:
- `android-sdk`
- `video-view`
- `hls-player`
- `video-filters`

"""

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

def sizeof_fmt(num, suffix="B"):
    for unit in ("", "K", "M", "G", "T", "P", "E", "Z"):
        if abs(num) < 1024.0:
            return f"{num:3.1f}{unit}{suffix}"
        num /= 1024.0
    return f"{num:.1f}Yi{suffix}"

def populateArchTableData():
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


archTableHeader = """|  Module Name     	|  arm64-v8a   |	armeabi-v7	|	x86		| x86_64"
"| -------------------|--------------|----------------|-----------|-------|"
"""

def getSingleArchData(a):
	s = archMaps[a]
	v8a = sizeof_fmt(s.arm64v8a - subtractBy(s.name, "arm64-v8a"))
	v7a = sizeof_fmt(s.armeabiv7a - subtractBy(s.name, "armeabi-v7a"))
	x86 = sizeof_fmt(s.x86 - subtractBy(s.name, "x86"))
	x86_64 = sizeof_fmt(s.x8664 - subtractBy(s.name, "x86_64"))
	return f"| {s.name} 	| **{v8a}**	|	**{v7a}**  	|	**{x86}**	| 	**{x86_64}** 	|"

def writeDataToFile(data, filePath):
	with open(filePath, "w") as doc:
		# Writing data to a file
		doc.writelines(data)	

def getArticleString(version):
	article = [prefixedArticle, archTableHeader,]
	# Print everything except room kit and the base
	for a in archMaps:
		if(a == "Without Sdk" or a == "Room Kit"):
			continue
		s = archMaps[a]
		article.append(getSingleArchData(a))
	article.extend([roomKitHeader,archTableHeader,getSingleArchData("Room Kit"),roomKitFooter])
	return "\n".join(article)

def printEntireArchTable():
	print(archTableHeader)
	for a in archMaps:
		if(a == "Without Sdk"):
			continue
		s = archMaps[a]
		print(getSingleArchData(a))


populateArchTableData()
if(len(sys.argv) == 1):
	printEntireArchTable()
else:
	print(f"Writing article for version {sys.argv[2]} to path: {sys.argv[1]}")
	writeDataToFile(getArticleString(sys.argv[2]), sys.argv[1])
