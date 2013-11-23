# circle [![Build Status](https://travis-ci.org/escortkeel/circle.png?branch=master)](https://travis-ci.org/escortkeel/circle)
circle is a fully asynchronous IRC client library for Java. It aims to be lightweight and easy to use.

The project was started on March 27 2013.

The source code is hosted on [GitHub](https://github.com/escortkeel/circle) and is released under the terms of the [Simplified BSD License](https://raw.github.com/escortkeel/circle/master/LICENSE).

## Documentation
Coming soon.

## Example
The following code connects to the "irc.freenode.net" IRC network with the nickname "cIRCler" and joins the channel #botwar. It will send the message "Hi!" all members of #botwar, before quitting with the message "Bye!".
```java
import java.io.IOException;
import com.github.escortkeel.circle.*;
import com.github.escortkeel.circle.event.*;

public class Main {
    public static void main(String[] args) throws Exception {
        IRCClient c = new IRCClient("cIRCler", new IRCAdapter() {
            @Override
            public void onChannelJoin(IRCChannelJoinEvent e) {
                e.getClient().privmsg(e.getChannel(), "Hi!");
                e.getClient().quit("Bye!");
            }
        });
        c.connect("irc.freenode.net");
        c.join("#botwar");
        c.waitFor();
    }
}
```
