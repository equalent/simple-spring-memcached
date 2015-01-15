/*
 * Copyright (c) 2008-2015 Nelson Carpentier, Jakub Białek
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.google.code.ssm.providers;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

/**
 * 
 * @author Nelson Carpentier
 * @author Jakub Białek
 * 
 */
public class CacheConfigurationTest {

    @Test
    public void testInAndOut() {
        final boolean consistent = RandomUtils.nextBoolean();
        final int opTimeout = RandomUtils.nextInt();
        final boolean useBinaryProt = RandomUtils.nextBoolean();

        final CacheConfiguration conf = new CacheConfiguration();
        conf.setConsistentHashing(consistent);
        conf.setOperationTimeout(opTimeout);
        conf.setUseBinaryProtocol(useBinaryProt);

        assertEquals(consistent, conf.isConsistentHashing());
        assertEquals(opTimeout, conf.getOperationTimeout().intValue());
        assertEquals(useBinaryProt, conf.isUseBinaryProtocol());
    }
}