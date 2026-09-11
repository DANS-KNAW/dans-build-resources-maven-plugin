/*
 * Copyright (C) 2026 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
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
import java.nio.charset.StandardCharsets;

@Mojo(name = "get-helper-script", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class GetHelperScriptMojo extends AbstractMojo {

    @Parameter(name = "helper", required = true, defaultValue = "add-swagger-ui.sh")
    private String helper;

    /**
     * Directory in which to place the license header template.
     */
    @Parameter(name = "targetDir", required = true, defaultValue = "${project.build.directory}")
    private File targetDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            getLog().info(String.format("Copying helper script %s to %s", helper, targetDir));
            var script = IOUtils.resourceToString(String.format("/helper-scripts/%s", helper), StandardCharsets.UTF_8);
            var outputFile = targetDir.toPath().resolve(helper);
            FileUtils.createParentDirectories(outputFile.toFile());
            FileUtils.writeStringToFile(outputFile.toFile(), script, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            throw new MojoExecutionException("Could not read add-swagger-ui.sh from resources", e);
        }

    }
}
