package networktopology;

public class FirewallRule {
    public String source;
    public String destination;
    public int port;
    public String protocol;
    public String action;

    public FirewallRule(String source, String destination, int port, String protocol, String action) {
        this.source = source;
        this.destination = destination;
        this.port = port;
        this.protocol = protocol;
        this.action = action;
    }

    @Override
    public String toString() {
        return "Rule [Source: " + source + ", Destination: " + destination +
               ", Port: " + port + ", Protocol: " + protocol + ", Action: " + action + "]";
    }
}
