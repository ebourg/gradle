/*
 * Copyright 2012 the original author or authors.
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

package org.gradle.scala.compile

import org.gradle.integtests.fixtures.TargetVersions
import org.gradle.api.JavaVersion

import spock.lang.IgnoreIf
import org.gradle.util.VersionNumber

@TargetVersions(["2.8.2", "2.9.2", "2.10.0-RC1"])
@IgnoreIf({ !JavaVersion.current().java6Compatible && AntScalaCompilerIntegrationTest.versionNumber > VersionNumber.parse("2.9.9") })
class AntScalaCompilerIntegrationTest extends BasicScalaCompilerIntegrationTest {
    String compilerConfiguration() {
        '''
compileScala.scalaCompileOptions.with {
    useAnt = true
}
'''
    }

    String logStatement() {
        "Compiling with Ant scalac task"
    }
}
