/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.tooling.internal.build;

import org.gradle.tooling.internal.protocol.InternalBuildEnvironment;
import org.gradle.tooling.model.build.GradleEnvironment;
import org.gradle.tooling.model.build.JavaEnvironment;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DefaultBuildEnvironment implements InternalBuildEnvironment, Serializable {

    private final File gradleUserHome;
    private final String gradleVersion;
    private final File javaHome;
    private final List<String> effectiveJvmArguments;
    private final List<String> allJvmArguments;
    private final Map<String, String> effectiveSystemProperties;

    public DefaultBuildEnvironment(
        File gradleUserHome,
        String gradleVersion,
        File javaHome,
        List<String> effectiveJvmArguments,
        List<String> allJvmArguments,
        Map<String, String> effectiveSystemProperties) {
        this.gradleUserHome = gradleUserHome;
        this.gradleVersion = gradleVersion;
        this.javaHome = javaHome;
        this.effectiveJvmArguments = effectiveJvmArguments;
        this.allJvmArguments = allJvmArguments;
        this.effectiveSystemProperties = effectiveSystemProperties;
    }

    public GradleEnvironment getGradle() {
        return new GradleEnvironment() {
            @Override
            public File getGradleUserHome() {
                return gradleUserHome;
            }

            public String getGradleVersion() {
                return gradleVersion;
            }
        };
    }

    public JavaEnvironment getJava() {
        return new JavaEnvironment() {
            @Override
            public File getJavaHome() {
                return javaHome;
            }

            @Override
            public List<String> getJvmArguments() {
                return effectiveJvmArguments;
            }

            @Override
            public List<String> getEffectiveJvmArguments() {
                return allJvmArguments;
            }

            @Override
            public Map<String, String> getSystemProperties() {
                return effectiveSystemProperties;
            }

        };
    }

}
