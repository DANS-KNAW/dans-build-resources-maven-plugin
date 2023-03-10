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

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RpmScriptlet {
    private static final Pattern includeDirectivePattern = Pattern.compile("^#include\\s+<(.*?)>", Pattern.MULTILINE);

    private final Path src;
    private final Path target;

    private final Log log;

    public RpmScriptlet(Path src, Path target, Log log) {
        this.src = src;
        this.target = target;
        this.log = log;
    }

    public void generate() throws IOException {
        boolean isEmpty = true;

        String scriptText = "";
        if (!Files.exists(target.getParent())) {
            log.info("Creating destination dir: " + target.getParent().toAbsolutePath());
            Files.createDirectories(target.getParent());
        }

        if (Files.exists(src)) {
            scriptText = Files.readString(src, StandardCharsets.UTF_8);
            Matcher m = includeDirectivePattern.matcher(scriptText);
            List<String> includeDirectives = new ArrayList<>();
            List<String> includeFileNames = new ArrayList<>();

            // Find all the "#include" directives.
            while (m.find()) {
                includeDirectives.add(m.group(0));
                includeFileNames.add(m.group(1));
            }

            // Map include directive to replacement text.
            Map<String, String> includeDirectiveToReplacementText = new HashMap<>();
            for (int i = 0; i < includeDirectives.size(); ++i) {
                String includeText = IOUtils.resourceToString("/includes/" + includeFileNames.get(i), StandardCharsets.UTF_8);
                includeDirectiveToReplacementText.put(includeDirectives.get(i), includeText);
            }

            // Do the replacements.
            for (String includeDirective : includeDirectiveToReplacementText.keySet()) {
                scriptText = scriptText.replace(includeDirective, includeDirectiveToReplacementText.get(includeDirective));
            }
            isEmpty = false;
        }
        else {
            log.debug("Source file " + src.getFileName() + " not found; creating empty file in destination.");
        }

        Files.write(target, scriptText.getBytes(StandardCharsets.UTF_8));
        log.info(String.format("Generated scriptlet %s%s", target, isEmpty ? " (empty)": ""));
    }
}
