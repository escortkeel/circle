# cIRCle
cIRCle (pronnounced "circle") is an IRC client library created and developed by Keeley Hoek. It fundamentally aims to be lightweight and easy to use.

The project was started on March 27 2013, when Keeley was fourteen.

The source code is hosted on <a href="https://github.com/escortkeel/cIRCle">GitHub</a> and is released under the terms of the <a href="https://raw.github.com/escortkeel/cIRCle/master/LICENSE">Simplified BSD License</a>.

## Documentation
Coming soon.

## Example
The following code connects to the "irc.freenode.net" IRC network with the nick "cIRCleMan" and joins channel #botwar. Upon completion, it will send the message "Hi!" to #botwar, before quitting with the message "Bye!".
```java
import com.github.escortkeel.circle.IRCClient;
import com.github.escortkeel.circle.IRCAdapter;
import com.github.escortkeel.circle.event.IRCChannelJoinEvent;
import com.github.escortkeel.circle.exception.IRCNameException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IRCNameException, IOException, InterruptedException {
        IRCClient c = IRCClient.create("irc.freenode.net", "cIRCleMan", new IRCAdapter() {
            @Override
            public void onChannelJoin(IRCChannelJoinEvent e) {
                e.getClient().privmsg(e.getChannel(), "Hi!");
                e.getClient().quit("Bye!");
            }
        });
        c.join("#botwar");
        c.waitFor();
    }
}
```
