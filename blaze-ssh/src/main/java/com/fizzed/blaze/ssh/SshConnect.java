/*
 * Copyright 2015 Fizzed, Inc.
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
package com.fizzed.blaze.ssh;

import com.fizzed.blaze.Context;
import com.fizzed.blaze.core.Action;
import java.nio.file.Path;
import com.fizzed.blaze.core.UriMixin;
import com.fizzed.blaze.util.MutableUri;

abstract public class SshConnect extends Action<SshConnect.Result,SshSession> implements UriMixin<SshConnect> {

    public SshConnect(Context context) {
        super(context);
    }

    abstract public SshConnect newConnect(MutableUri uri);
    
    abstract public SshConnect configFile(Path configFile);

    abstract public SshConnect identityFile(Path identityFile);

    abstract public SshConnect keepAliveInterval(long keepAliveInterval);

    abstract public SshConnect knownHostsFile(Path knownHostsFile);
    
    abstract public SshConnect hostChecking(boolean hostChecking);
    
    /**
     * @deprecated
     * @see #hostChecking(boolean)
     */
    abstract public SshConnect disableHostChecking();
    
    /**
     * The connection will be proxied thru the supplied session.  Allows you
     * to connect thru an SSH bastion "jump" host.  Can be done multiple times
     * to connect thru more than one bastion host.
     * @param session The session to proxy thru
     * @return This connect
     * @see #proxy(SshSession, boolean) 
     */
    public SshConnect proxy(SshSession session) {
        return proxy(session, false);
    }
    
    /**
     * The connection will be proxied thru the supplied session.  Allows you
     * to connect thru an SSH bastion "jump" host.  Can be done multiple times
     * to connect thru more than one bastion host.
     * @param session The session to proxy thru
     * @param autoclose If true then the session as a proxy will be closed
     *      when this session is closed too.
     * @return This connect
     */
    abstract public SshConnect proxy(SshSession session, boolean autoclose);
    
    protected Result createResult(SshSession value) {
        return new Result(this, value);
    }
    
    static public class Result extends com.fizzed.blaze.core.Result<SshConnect,SshSession,Result> {
        
        Result(SshConnect action, SshSession value) {
            super(action, value);
        }
        
    }
    
}
