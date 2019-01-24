import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Firewall {

    PortNode[] ports = new PortNode[65535];

    Firewall(String file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] rules = line.split(",");
                addPort(rules);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean accept_packet(String direction, String protocol, int port, String ip) {
        if (ports[port] == null) {
            return false;
        }
        return ports[port].check(direction, protocol, ip);
    }

    void addPort(String[] input) {
        String portNum = input[2];
        if (portNum.contains("-")) {
            String[] ports = portNum.split("-");
            for (int i = Integer.parseInt(ports[0]); i < Integer.parseInt(ports[1]); i++) {
                if (this.ports[i] == null) {
                    this.ports[i] = new PortNode();
                }
                this.ports[i].addRef(input[0], input[1], input[3]);
            }
        } else {
            int port = Integer.parseInt(portNum);
            if (this.ports[port] == null) {
                this.ports[port] = new PortNode();
            }
            this.ports[port].addRef(input[0], input[1], input[3]);
        }
    }

    class PortNode {
        List<IPNode> inboundTcp = new ArrayList<>();
        List<IPNode> inboundUdp = new ArrayList<>();
        List<IPNode> outboundTcp = new ArrayList<>();
        List<IPNode> outboundUdp = new ArrayList<>();

        void addRef(String direction, String protocol, String ip) {
            if (direction.equals("inbound")) {
                if (protocol.equals("tcp")) {
                    inboundTcp.add(new IPNode(ip));
                } else {
                    inboundUdp.add(new IPNode(ip));
                }
            } else {
                if (protocol.equals("tcp")) {
                    outboundTcp.add(new IPNode(ip));
                } else {
                    outboundUdp.add(new IPNode(ip));
                }
            }
        }

        boolean check(String direction, String protocol, String ip) {
            List<IPNode> target = inboundTcp;
            if (direction.equals("inbound")) {
                if (protocol.equals("udp")) {
                    target = inboundUdp;
                }
            } else {
                if (protocol.equals("tcp")) {
                    target = outboundTcp;
                } else {
                    target = outboundUdp;
                }
            }
            if (target == null) {return false;}
            for (IPNode node : target) {
                if (node.checkMatch(ip)) {
                    return true;
                }
            }
            return false;
        }
    }

    class IPNode {
        int[] start = new int[4];
        int[] end = new int[4];

        IPNode (String s) {
            if (s.contains("-")) {
                String[] ips = s.split("-");
                set(ips[0], ips[1]);
            } else {
                set(s, s);
            }
        }

        void set(String s, String e) {
            String[] sIP = s.split("\\.");
            String[] eIP = e.split("\\.");
            for (int i = 0; i < 4; i++) {
                start[i] = Integer.parseInt(sIP[i]);
                end[i] = Integer.parseInt(eIP[i]);
            }
        }

        boolean checkMatch(String s) {
            String[] ip = s.split("\\.");
            for (int i = 0; i < 4; i++) {
                int num = Integer.parseInt(ip[i]);
                if (num < start[i] || num > end[i]) {
                    return false;
                }
            }
            return true;
        }
    }

}

