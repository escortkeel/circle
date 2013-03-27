
import java.io.IOException;
import me.escortkeel.circle.IRCClient;
import me.escortkeel.circle.event.IRCAdapter;
import me.escortkeel.circle.event.IRCMotdEvent;

public class NewMain {
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