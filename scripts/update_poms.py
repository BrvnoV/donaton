import xml.etree.ElementTree as ET
import os

ET.register_namespace('', "http://maven.apache.org/POM/4.0.0")
ET.register_namespace('xsi', "http://www.w3.org/2001/XMLSchema-instance")

namespaces = {'mvn': 'http://maven.apache.org/POM/4.0.0'}

core_services = ['ms-campanas', 'ms-donaciones', 'ms-voluntarios']
all_services = core_services + ['donaton-bff']

def update_pom(path, is_core):
    tree = ET.parse(path)
    root = tree.getroot()
    
    # Add validation dependency if core
    if is_core:
        dependencies = root.find('mvn:dependencies', namespaces)
        if dependencies is not None:
            # Check if it exists
            exists = False
            for dep in dependencies.findall('mvn:dependency', namespaces):
                art = dep.find('mvn:artifactId', namespaces)
                if art is not None and art.text == 'spring-boot-starter-validation':
                    exists = True
                    break
            if not exists:
                dep = ET.Element('{http://maven.apache.org/POM/4.0.0}dependency')
                grp = ET.SubElement(dep, '{http://maven.apache.org/POM/4.0.0}groupId')
                grp.text = 'org.springframework.boot'
                art = ET.SubElement(dep, '{http://maven.apache.org/POM/4.0.0}artifactId')
                art.text = 'spring-boot-starter-validation'
                
                # Append with formatting
                dep.tail = '\n    '
                dependencies.append(dep)
                print(f"Added validation to {path}")

    # Add jacoco check rule
    build = root.find('mvn:build', namespaces)
    if build is not None:
        plugins = build.find('mvn:plugins', namespaces)
        if plugins is not None:
            for plugin in plugins.findall('mvn:plugin', namespaces):
                art = plugin.find('mvn:artifactId', namespaces)
                if art is not None and art.text == 'jacoco-maven-plugin':
                    executions = plugin.find('mvn:executions', namespaces)
                    if executions is not None:
                        # Check if check exists
                        exists = False
                        for exec_node in executions.findall('mvn:execution', namespaces):
                            id_node = exec_node.find('mvn:id', namespaces)
                            if id_node is not None and id_node.text == 'check-cobertura':
                                exists = True
                                break
                        if not exists:
                            execution = ET.Element('{http://maven.apache.org/POM/4.0.0}execution')
                            id_node = ET.SubElement(execution, '{http://maven.apache.org/POM/4.0.0}id')
                            id_node.text = 'check-cobertura'
                            goals = ET.SubElement(execution, '{http://maven.apache.org/POM/4.0.0}goals')
                            goal = ET.SubElement(goals, '{http://maven.apache.org/POM/4.0.0}goal')
                            goal.text = 'check'
                            configuration = ET.SubElement(execution, '{http://maven.apache.org/POM/4.0.0}configuration')
                            rules = ET.SubElement(configuration, '{http://maven.apache.org/POM/4.0.0}rules')
                            rule = ET.SubElement(rules, '{http://maven.apache.org/POM/4.0.0}rule')
                            element = ET.SubElement(rule, '{http://maven.apache.org/POM/4.0.0}element')
                            element.text = 'BUNDLE'
                            limits = ET.SubElement(rule, '{http://maven.apache.org/POM/4.0.0}limits')
                            limit = ET.SubElement(limits, '{http://maven.apache.org/POM/4.0.0}limit')
                            counter = ET.SubElement(limit, '{http://maven.apache.org/POM/4.0.0}counter')
                            counter.text = 'LINE'
                            value = ET.SubElement(limit, '{http://maven.apache.org/POM/4.0.0}value')
                            value.text = 'COVEREDRATIO'
                            minimum = ET.SubElement(limit, '{http://maven.apache.org/POM/4.0.0}minimum')
                            minimum.text = '0.60'
                            
                            execution.tail = '\n        '
                            executions.append(execution)
                            print(f"Added jacoco check to {path}")
                            break

    # Write out with correct xml declaration
    tree.write(path, encoding='UTF-8', xml_declaration=True)

for svc in all_services:
    update_pom(os.path.join(svc, 'pom.xml'), svc in core_services)

