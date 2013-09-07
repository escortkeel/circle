/*
 * Copyright (c) 2013, escortkeel
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.escortkeel.circle;

/**
 *
 * @author escortkeel
 */
public class IRCUser {

    private final String nickname;
    private final String username;
    private final String hostname;

    public IRCUser(String raw) {
        int split = raw.indexOf("!~");
        if (split == -1) {
            nickname = raw;
            raw = null;
        } else {
            nickname = raw.substring(0, split);
            raw = raw.substring(split + 2);
        }
        
        if(raw == null) {
            username = null;
        } else {            
            split = raw.indexOf("@");
            if (split == -1) {
                username = raw;
            } else {
                username = raw.substring(0, split);
                raw = raw.substring(split + 1);                
            }
        }
        
        if(raw == null) {
            hostname = null;
        } else {
            hostname = raw;
        }
    }

    /**
     * Returns the nickname associated with this
     * <code>IRCUser</code> instance.
     *
     * @return the nickname associated with this <code>IRCUser</code>
     * instance.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the username associated with this
     * <code>IRCUser</code> instance.
     *
     * @return the username associated with this <code>IRCUser</code>
     * instance.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the hostname associated with this
     * <code>IRCUser</code> instance.
     *
     * @return the hostname associated with this <code>IRCUser</code>
     * instance.
     */
    public String getHostname() {
        return hostname;
    }
}
