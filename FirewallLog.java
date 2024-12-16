package networktopology;

public class FirewallLog {
    public String source;
    public String destination;
    public int port;
    public String protocol;
    public String status;

    public FirewallLog(String source, String destination, int port, String protocol, String status) {
        this.source = source;
        this.destination = destination;
        this.port = port;
        this.protocol = protocol;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Log [Source: " + source + ", Destination: " + destination +
               ", Port: " + port + ", Protocol: " + protocol + ", Status: " + status + "]";
    }
}
