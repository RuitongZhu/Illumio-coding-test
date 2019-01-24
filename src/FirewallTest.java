import static org.junit.jupiter.api.Assertions.*;

class FirewallTest {

    @org.junit.jupiter.api.Test
    void accept_packet() {
        Firewall firewall = new Firewall("rules.csv");
        assertTrue(firewall.accept_packet("inbound", "tcp", 80, "192.168.1.2"));
        assertTrue(firewall.accept_packet("inbound", "udp", 53, "192.168.2.1"));
        assertTrue(firewall.accept_packet("outbound", "tcp", 10234, "192.168.10.11"));
        assertFalse(firewall.accept_packet("inbound", "tcp", 81, "192.168.1.2"));
        assertFalse(firewall.accept_packet("inbound", "udp", 24, "52.12.48.92"));
    }
}