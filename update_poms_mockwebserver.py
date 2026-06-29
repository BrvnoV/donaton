import re
import sys

def add_mockwebserver(pom_path):
    with open(pom_path, 'r') as f:
        content = f.read()

    dependency = """
        <dependency>
            <groupId>com.squareup.okhttp3</groupId>
            <artifactId>mockwebserver</artifactId>
            <version>4.12.0</version>
            <scope>test</scope>
        </dependency>
"""
    if "mockwebserver" not in content:
        content = content.replace("</dependencies>", dependency + "    </dependencies>", 1)
        with open(pom_path, 'w') as f:
            f.write(content)

add_mockwebserver("ms-donaciones/pom.xml")
