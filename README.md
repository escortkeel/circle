# circle
circle is a fully asynchronous IRC client library for Java. It aims to be lightweight and easy to use.

The project was started on March 27 2013.

The source code is hosted on <a href="https://github.com/escortkeel/cIRCle">GitHub</a> and is released under the terms of the <a href="https://raw.github.com/escortkeel/cIRCle/master/LICENSE">Simplified BSD License</a>.

## Documentation
Coming soon.

## Example
The following code connects to the "irc.freenode.net" IRC network with the nickname "cIRCler" and joins channel #botwar. It will send the message "Hi!" all members of #botwar, before quitting with the message "Bye!".
```java
import com.github.escortkeel.circle.IRCClient;
import com.github.escortkeel.circle.IRCAdapter;
import com.github.escortkeel.circle.event.IRCChannelJoinEvent;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
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
