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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Generates the scriptlets for RPM to call at the various events in the installation or upgrade process.
 */
@Mojo(name = "generate-rpm-scriptlets", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class GenerateRpmScriptletsMojo extends AbstractMojo {

    /**
     * Directory that the plug-in will search for scriptlets. If not found, the plug-in will generate an empty file.
     */
    @Parameter(name = "scriptletsSourceDir", required = true, defaultValue = "src/main/rpm")
    private File scriptletsSourceDir;

    @Parameter(name = "scriptletsTargetDir", required = true, defaultValue = "${project.build.directory}/rpm-scriptlets")
    private File scriptletsTargetDir;

    private static final List<String> srcFiles = Arrays.asList(
        "build-0-prepare.sh",
        "build-1-install.sh",
        "build-2-clean.sh",
        "0-pre-trans.sh",
        "1-pre-install.sh",
        "2-post-install.sh",
        "3-pre-remove.sh",
        "4-post-remove.sh",
        "5-post-trans.sh",
        "6-verify.sh");

    @Override

    public void execute() throws MojoExecutionException, MojoFailureException {
        for (String name : srcFiles) {
            try {
                new RpmScriptlet(scriptletsSourceDir.toPath().resolve(name), scriptletsTargetDir.toPath().resolve(name), getLog()).generate();
            }
            catch (IOException e) {
                throw new MojoExecutionException(String.format("Could not generate scriptlet %s", name), e);
            }
        }
    }
}

