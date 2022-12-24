/*
 * Copyright (C) 2022 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.maven.buildresources;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Read
 */
@Mojo(name = "get-license-header-template", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GetLicenseHeaderTemplateMojo extends AbstractMojo {

    /**
     * Name of a license header template included in the plugin. (Currently only 'apache2.txt'.)
     */
    @Parameter(name = "license", required = true, defaultValue = "apache2.txt")
    private String license;

    /**
     * Directory in which to place the license header template.
     */
    @Parameter(name = "targetDir", required = true, defaultValue = "${project.build.directory}")
    private File targetDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Path outputFile = targetDir.toPath().resolve(license);
        try {
            getLog().info(String.format("Copying %s to %s", license, outputFile));
            FileUtils.createParentDirectories(outputFile.toFile());
            Files.write(outputFile, getLicenseHeaderText().getBytes(UTF_8));
        }
        catch (IOException e) {
            throw new MojoExecutionException(String.format("Could not write license header to %s", outputFile), e);
        }
    }

    private String getLicenseHeaderText() throws MojoExecutionException {
        try {
            return IOUtils.resourceToString("/licenses/" + license, UTF_8);
        }
        catch (IOException e) {
            throw new MojoExecutionException("Could not read license header text", e);
        }
    }

}
