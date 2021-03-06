/*
 * Copyright (c) 2008-2017 Nelson Carpentier, Jakub Białek
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
 * 
 */

package com.google.code.ssm.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.ssm.aop.support.AnnotationData;
import com.google.code.ssm.aop.support.AnnotationDataBuilder;
import com.google.code.ssm.api.InvalidateAssignCache;

/**
 * 
 * @author Nelson Carpentier
 * @author Jakub Białek
 * 
 */
@Aspect
public class InvalidateAssignCacheAdvice extends CacheAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(InvalidateAssignCacheAdvice.class);

    @Pointcut("@annotation(com.google.code.ssm.api.InvalidateAssignCache)")
    public void invalidateAssign() {
        /* pointcut definition */
    }

    @Around("invalidateAssign()")
    public Object cacheInvalidateAssign(final ProceedingJoinPoint pjp) throws Throwable {
        if (isDisabled()) {
            getLogger().info("Cache disabled");
            return pjp.proceed();
        }

        final Object result = pjp.proceed();

        // This is injected caching. If anything goes wrong in the caching, LOG
        // the crap outta it, but do not let it surface up past the AOP injection itself.
        String cacheKey = null;
        try {
            final Method methodToCache = getCacheBase().getMethodToCache(pjp, InvalidateAssignCache.class);
            final InvalidateAssignCache annotation = methodToCache.getAnnotation(InvalidateAssignCache.class);
            final AnnotationData data = AnnotationDataBuilder.buildAnnotationData(annotation, InvalidateAssignCache.class, methodToCache);

            cacheKey = getCacheBase().getCacheKeyBuilder().getAssignCacheKey(data);

            getCacheBase().getCache(data).delete(cacheKey);
        } catch (Exception ex) {
            warn(ex, "Caching on method %s and key [%s] aborted due to an error.", pjp.toShortString(), cacheKey);
        }
        return result;
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

}
