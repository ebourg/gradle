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

package org.gradle.test.fixtures.file;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.io.File;

public class TestDistributionDirectoryProvider extends AbstractTestDirectoryProvider {
    static {
        // NOTE: the space in the directory name is intentional
        root = new TestFile(new File("build/tmp/test distros"));
    }

    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        return doApply(base, method, target);
    }

    public static TestDistributionDirectoryProvider newInstance() {
        return new TestDistributionDirectoryProvider();
    }

    public static TestDistributionDirectoryProvider newInstance(FrameworkMethod method, Object target) {
        TestDistributionDirectoryProvider testDirectoryProvider = new TestDistributionDirectoryProvider();
        testDirectoryProvider.init(method.getName(), target.getClass().getSimpleName());
        return testDirectoryProvider;
    }

}