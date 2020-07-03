#!/usr/bin/env python3
USAGE = '''
Auto-generate Gradle file with current Emarsys SDK version from the Github API.

Usage:
    ./gradle/update.py          # prints output to stdout
    ./gradle/update.py -u       # updates version.gradle file in place 
'''
from urllib.request import urlopen
import json
import os
import sys

VERSION_GRADLE = os.path.dirname(os.path.realpath(__file__)) + '/gradle/version.gradle'
REPOSITORY = "emartech/android-emarsys-sdk"
FORMAT = '''
ext {{
    emarsys_sdk_version = "{sdk_version}"
    emarsys_sdk_version_code = {sdk_version_code}
}}
'''

def fetch(repository):
    with urlopen("https://api.github.com/repos/{}/releases".format(repository)) as res:
        data = json.loads(res.read())
    return { "sdk_version": data[0]["tag_name"], "sdk_version_code": len(data) }


if __name__ == '__main__':
    if len(sys.argv) == 1:
        print(FORMAT.format(**fetch(REPOSITORY)), file=sys.stdout)
    elif len(sys.argv) == 2 and sys.argv[1] == '-u':
        with open(VERSION_GRADLE, 'w') as f:
            print(FORMAT.format(**fetch(REPOSITORY)), file=f)
    else:
        print(USAGE, sys.stderr)
