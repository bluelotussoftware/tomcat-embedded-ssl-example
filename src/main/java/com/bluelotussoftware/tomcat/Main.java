/*
 * Copyright 2016 Blue Lotus Software, LLC.
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
package com.bluelotussoftware.tomcat;

import com.bluelotussoftware.tomcat.embedded.TomcatEmbeddedRunner;
import org.apache.catalina.LifecycleException;

/**
 *
 * @author John Yeary <jyeary@bluelotussoftware.com>
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) throws LifecycleException {
        TomcatEmbeddedRunner runner = new TomcatEmbeddedRunner();
        runner.start();
    }
}
