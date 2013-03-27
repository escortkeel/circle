# cIRCle
cIRCle (pronnounced "circle") is an IRC client library created and developed by Keeley Hoek. It fundamentally aims to be lightweight and easy to use.

The project was started on March 27 2013, when Keeley was fourteen.

The source code is hosted on <a href="https://github.com/escortkeel/cIRCle">GitHub</a> and is released under the terms of the <a href="https://raw.github.com/escortkeel/cIRCle/master/LICENSE">Simplified BSD License</a>.

## Documentation
Coming soon.

## Example
The following code connects to the "irc.freenode.net" IRC network with the nick "cIRCle" and prints out the MOTD, before terminating.
```java
import java.io.IOException;
import com.github.escortkeel.circle.IRCClient;
import com.github.escortkeel.circle.event.IRCAdapter;
import com.github.escortkeel.circle.event.IRCMotdEvent;

public class Main {
    public static void main(String[] args) throws IOException {        
        final IRCClient c = new IRCClient("irc.freenode.net", "cIRCle");
        c.addClient(new IRCAdapter() {
            @Override
            public void onMotd(IRCMotdEvent event) {
                System.out.println(event.getMotd());

                try {
                    c.close();
                } catch (IOException ex) {
                }
            }
        });
        
        while(!c.isClosed());
    }
}
```
