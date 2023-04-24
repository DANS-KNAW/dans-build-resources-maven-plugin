dans-build-resources-maven-plugin
=================================

Maven plugin to generate build resources for DANS projects.


SYNOPIS
-------

    <plugin>
        <groupId>nl.knaw.dans.build</groupId>
        <artifactId>dans-build-resources-maven-plugin</artifactId>
        <version>...</version>
        <executions>
            <execution>
                <id>generate-build-resources</id>
                <goals>
                    <goal>generate-build-resources</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

DESCRIPTION
-----------

This plugin generates build resources for DANS projects. The following resources are generated:

* RPM scriptlets. The parent POM dd-parent expects all scriptlets supported by rpm-maven-plugin to be present. The GenerateRpmScriptletsMojo generates the
  empty scriptlets for the ones that are not present in the project.
* Include helper bash functions in RPM scriptlets. The RpmScriptletsMojo generates the helper bash functions that are included in the project's RPM scriptlets
  by means of an `include` directive.
* License header template. The parent POM dd-parent expects a license header template to be present. Rather than having to copy the template from the parent
  POM, the GenerateLicenseHeaderMojo generates the template in the project. 