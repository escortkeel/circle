/*
 * Copyright (c) 2012, Keeley Hoek
 * All rights reserved.
 * 
 * Redistribution and use of this software in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 *   Redistributions of source code must retain the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer.
 * 
 *   Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer in the documentation and/or other
 *   materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package me.escortkeel.circle;

import me.escortkeel.circle.exception.IRCNameReservedException;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;
import me.escortkeel.circle.event.IRCRawMessageEvent;
import me.escortkeel.circle.event.IRCClient;
import me.escortkeel.circle.event.IRCMotdEvent;

/**
 *
 * @author Keeley Hoek (escortkeel@live.com)
 */
public class IRCConnection implements Closeable {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintStream out;
    private final String nickname;
    private final String password;
    private final String username;
    private final String realname;
    private final boolean invisible;
    private final StringBuilder motd = new StringBuilder();
    private final ArrayList<IRCClient> clients = new ArrayList<>();

    public IRCConnection(String address, String nick) throws IOException, IRCNameReservedException {
        this(address, 6667, nick);
    }

    public IRCConnection(String address, int port, String nick) throws IOException {
        this(address, 6667, nick, nick, nick, false);
    }

    public IRCConnection(String address, int port, String nickname, String username, String realname, boolean invisible) throws IOException {
        Objects.requireNonNull(address);
        Objects.requireNonNull(nickname);
        Objects.requireNonNull(username);
        Objects.requireNonNull(realname);

        if (nickname.contains(" ")) {
            throw new IllegalArgumentException("Nickname must not contain spaces");
        }

        if (username.contains(" ")) {
            throw new IllegalArgumentException("Username must not contain spaces");
        }

        this.socket = new Socket(address, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintStream(socket.getOutputStream());

        this.nickname = nickname;
        this.password = Long.toString(new SecureRandom().nextLong(), 36);
        this.username = username;
        this.realname = realname;

        this.invisible = invisible;

        handshake();
    }

    public void addClient(IRCClient client) {
        clients.add(client);
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    private void handshake() throws IOException {
        out.println("PASS " + password);
        out.println("USER " + username + " " + (invisible ? "8" : "0") + " * :" + realname);
        out.println("NICK " + nickname);

        Thread worker = new Thread("cIRCle Thread") {
            @Override
            public void run() {
                try {
                    String s;
                    while ((s = in.readLine()) != null) {
                        handleMessage(s);
                    }
                } catch (IOException ex) {
                    try {
                        close();
                    } catch (IOException ex2) {
                    }
                }
            }
        };
        
        worker.setDaemon(true);
        worker.start();
    }

    private void handleMessage(String raw) {
        String source = null;
        int split = raw.indexOf(' ');
        if (raw.startsWith(":")) {
            source = raw.substring(1, split);
            raw = raw.substring(split + 1);
            split = raw.indexOf(' ');
        }

        String keyword = raw.substring(0, split);
        String args = raw.substring(split + 1);

        int reply = -1;
        try {
            if (keyword.length() == 3) {
                reply = Integer.parseInt(keyword);
            }
        } catch (NumberFormatException nfe) {
        }

        if (reply == -1) {
            switch (keyword) {
                case "PING":
                    out.println("PONG " + args);
                    break;
            }
        } else {
            switch (IRCReply.toEnum(reply)) {
                case MOTDSTART: {
                    int motdStart = args.indexOf(':');
                    String sub = args.substring(motdStart);
                    if (motdStart != -1 && sub.length() != 1 && sub.length() > 3) {
                        motdStart = sub.indexOf(' ');
                        sub = sub.substring(motdStart + 1, sub.length() - 3);

                        if (motdStart != -1 && sub.length() != 1) {
                            motd.append(sub).append("\n");
                        }
                    }
                    break;
                }
                case MOTD: {
                    int motdStart = args.indexOf(':');
                    String sub = args.substring(motdStart);
                    if (motdStart != -1 && sub.length() != 1) {
                        motdStart = sub.indexOf(' ');
                        sub = sub.substring(motdStart + 1);

                        if (motdStart != -1 && sub.length() != 1) {
                            motd.append(sub).append("\n");
                        }
                    }
                    break;
                }
                case ENDOFMOTD: {
                    int motdStart = args.indexOf(':');
                    if (motdStart != -1 && args.substring(motdStart).length() != 1) {
                        motd.append(args.substring(motdStart + 1)).append("\n");
                    }
                    fireMotdEvent(new IRCMotdEvent(motd.toString()));
                    break;
                }
            }
        }

        fireRawMessageEvent(new IRCRawMessageEvent(source, raw));
    }

    private void fireMotdEvent(IRCMotdEvent e) {
        for (IRCClient l : clients) {
            l.onMotd(e);
        }
    }

    private void fireRawMessageEvent(IRCRawMessageEvent e) {
        for (IRCClient l : clients) {
            l.onRawMessage(e);
        }
    }
}
