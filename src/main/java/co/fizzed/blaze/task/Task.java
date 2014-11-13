/*
 * Copyright 2014 Fizzed Inc.
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
package co.fizzed.blaze.task;

import co.fizzed.blaze.action.*;
import co.fizzed.blaze.core.Context;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 *
 * @author joelauer
 */
public abstract class Task<T> extends Action<T> {
    private final static Logger logger = LoggerFactory.getLogger(Task.class);
    
    private final AtomicBoolean called;
    private Result<T> result;
    
    public Task(Context context) {
        super(context);
        this.called = new AtomicBoolean(false);
    }
    
    public boolean getCalled() {
        return this.called.get();
    }
    
    public boolean called() {
        return this.called.get();
    }
    
    @Override
    protected Result<T> execute() throws Exception {
        // first caller wins
        if (!this.called.compareAndSet(false, true)) {
            logger.debug("Task [" + this.getName() + "] already called; ignoring duplicate call and returning previous result");
            return this.result;
        }
        // problem is that a task may call other tasks...
        // we need to keep track of task we are replacing...
        String lastTaskName = MDC.get("task");
        MDC.put("task", this.getName());
        long started = System.currentTimeMillis();
        logger.info("Task running...");
        this.result = executeTask();
        long finished = System.currentTimeMillis();
        logger.info("Task finished (" + (finished-started) + " ms)");
        MDC.put("task", lastTaskName);
        return result;
    }
    
    abstract protected Result<T> executeTask() throws Exception;
    
}
