# cIRCle
cIRCle (pronnounced "circle") is an IRC client library created and developed by Keeley Hoek. It fundamentally aims to be lightweight and easy to use.

The project was started on March 27 2013, when Keeley was fourteen.

The source code is hosted on <a href="https://github.com/escortkeel/cIRCle">GitHub</a> and is released under the terms of the <a href="https://raw.github.com/escortkeel/cIRCle/master/LICENSE">Simplified BSD License</a>.

## Documentation
Coming soon.

## Example
The following code connects to the "irc.freenode.net" IRC servers with the nick "cIRCle" and prints out the MOTD, before terminating.
```
import java.io.IOException;
import me.escortkeel.circle.IRCConnection;
import me.escortkeel.circle.event.IRCClient;
import me.escortkeel.circle.event.IRCMotdEvent;

public class Main {
    public static void main(String[] args) throws IOException {        
        final IRCConnection c = new IRCConnection("irc.freenode.net", "cIRCle");
        c.addClient(new IRCClient() {
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