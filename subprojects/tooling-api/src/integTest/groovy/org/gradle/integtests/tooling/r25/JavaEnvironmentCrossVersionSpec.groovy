/*
 * Copyright 2015 the original author or authors.
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




package org.gradle.integtests.tooling.r25

import org.gradle.integtests.tooling.fixture.TargetGradleVersion
import org.gradle.integtests.tooling.fixture.ToolingApiSpecification
import org.gradle.integtests.tooling.fixture.ToolingApiVersion
import org.gradle.tooling.ProjectConnection
import org.gradle.tooling.model.build.BuildEnvironment

class JavaEnvironmentCrossVersionSpec extends ToolingApiSpecification {

    def setup() {
        //this test does not make any sense in embedded mode
        //as we don't own the process
        toolingApi.requireDaemons()
    }

    @TargetGradleVersion('>=2.5')
    @ToolingApiVersion('>=2.5')
    def "tooling api honours differentiates jvm args from system properties specified in gradle.properties"() {
        file('build.gradle') << """
assert java.lang.management.ManagementFactory.runtimeMXBean.inputArguments.contains('-Xmx16m')
assert System.getProperty('some-prop') == 'some-value'
"""
        file('gradle.properties') << "org.gradle.jvmargs=-Dsome-prop=some-value -Xmx16m"

        when:
        BuildEnvironment env = toolingApi.withConnection { connection ->
            connection.newBuild().run() //the assert
            connection.getModel(BuildEnvironment)
        }

        then: "complete jvm args include the max memory from gradle.properties"
        env.java.effectiveJvmArguments.contains('-Xmx16m')

        and: "effective system properties contains the custom system property"
        env.java.systemProperties['some-prop'] == 'some-value'
    }

    @ToolingApiVersion(">=2.5")
    @TargetGradleVersion(">=2.5")
    def "provide Java environment information on BuildEnvironment"() {
        file("build.gradle")

        when: "a new BuildEnvironment is created"
        def buildEnvironment = withConnection { ProjectConnection connection ->
            connection.getModel(BuildEnvironment)
        }

        then: "a Java environment is provided"
        buildEnvironment != null
        def java = buildEnvironment.java
        java != null

        and: "all JVM arguments are the same as effective JVM arguments"
        java.effectiveJvmArguments.size() == java.jvmArguments.size()
    }


    @ToolingApiVersion(">=2.5")
    @TargetGradleVersion(">=2.5")
    def "provide Java environment information on BuildEnvironment using properties in JVM args"() {
        file("build.gradle")

        when: "a new BuildEnvironment is created"
        def buildEnvironment = withConnection { ProjectConnection connection ->
            connection.model(BuildEnvironment)
                    .setJvmArguments('-Dfoo=bar')
                    .get()
        }

        then: "a Java environment is provided"
        buildEnvironment != null
        def java = buildEnvironment.java
        java != null

        and: "effective system properties contains user specified system properties"
        java.systemProperties.foo == 'bar'

        and: "JVM arguments are different from effective JVM arguments"
        java.jvmArguments.size() > 0
        java.effectiveJvmArguments.size() == java.jvmArguments.size() + 1
    }

    @ToolingApiVersion(">=2.5")
    @TargetGradleVersion(">=2.5")
    def "provide Java environment information on BuildEnvironment using user JVM args"() {
        file("build.gradle")

        when: "a new BuildEnvironment is created"
        def buildEnvironment = withConnection { ProjectConnection connection ->
            connection.model(BuildEnvironment)
                    .setJvmArguments('-XX:MaxPermSize=128m')
                    .get()
        }

        then: "a Java environment is provided"
        buildEnvironment != null
        def java = buildEnvironment.java
        java != null

        and: "effective system properties are not empty"
        !java.systemProperties.isEmpty()

        and: "effective JVM arguments are the same as JVM arguments because MaxPermSize is managed"
        java.effectiveJvmArguments.size() == java.jvmArguments.size()
    }


}
