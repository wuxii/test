/*
 * Copyright 2002-2014 the original author or authors.
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
package org.moon.test.mapper.vo.deep;

import org.moon.test.mapper.vo.VoSupport;

/**
 * @author wuxii@foxmail.com
 */
public class SubSubDest extends VoSupport {

    private static final long serialVersionUID = 1L;
    private String name;

    public SubSubDest() {
        super();
    }

    public SubSubDest(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{\"name\":\"" + name + "\"}";
    }

}
