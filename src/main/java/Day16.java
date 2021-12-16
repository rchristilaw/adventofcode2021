import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day16 extends BaseDay {
    private final static Logger log = Logger.getLogger(Day16.class.getName());
    private final static String inputFile = "day16.txt";


    public static void main(String... args) throws URISyntaxException, IOException {
        final var day16 = new Day16();
        day16.runPart1();
//        day16.runPart2();
    }

    public void runPart1() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        var msg = new Message(decodeHexMessage(input));

        msg.packet = parsePacket(msg);

        var total = 0;
        log.info("TOTAL: " + msg.packet.packetTotal);
    }

    private Packet parsePacket(Message msg) {
        final var packet = new Packet();

        packet.version = Integer.parseInt(msg.getNext(3), 2);
        msg.versionSum +=  packet.version;

        packet.type = Integer.parseInt(msg.getNext(3), 2);

        if (packet.type == 4) {
            final var decimal = new StringBuilder();
            while(msg.getNext(1).equals("1")) {
                var cur = msg.message.substring(msg.position, msg.position + 4);
                decimal.append(msg.getNext( 4));
            }
            decimal.append(msg.getNext( 4));
            packet.packetTotal = Long.parseLong(decimal.toString(), 2);
            return packet;
        } else {
            var lengthType =  msg.getNext(1);
            if (lengthType.equals("0")) {
                var length =  Integer.parseInt(msg.getNext(15), 2);
                final var curPos = msg.position;

                while (msg.position < curPos + length) {
                    packet.subPackets.add(parsePacket(msg));
                }
            } else {
                var subPacketCount =  Integer.parseInt(msg.getNext(11), 2);

                for(int s = 0; s < subPacketCount; s++) {
                    packet.subPackets.add(parsePacket(msg));
                }
            }
        }

        packet.packetTotal = calculatePacketTotal(packet);
        return packet;
    }

    public void runPart2() throws URISyntaxException, IOException {
        final var input = readClassPathResource(inputFile);

        var total = 0;
        log.info("TOTAL: " + total);
    }

    private String decodeHexMessage(String message) {
        final var messageArr = message.split("");

        final var decodedMsg = new StringBuilder();
        for(var hexVal : messageArr) {
            decodedMsg.append(HexToBinary.getBinaryFromHex(hexVal));
        }

        return decodedMsg.toString();
    }

    private long calculatePacketTotal(Packet packet) {
        int total = 0;
        if (packet.type == 4) {
            return Integer.parseInt(packet.content);
        } else if (packet.type == 0) {
            return packet.subPackets.stream()
                .mapToLong(it -> it.packetTotal)
                .reduce(0, Math::addExact);
        } else if (packet.type == 1) {
            return packet.subPackets.stream()
                .mapToLong(it -> it.packetTotal)
                .reduce(1, Math::multiplyExact);
        } else if (packet.type == 2) {
            return packet.subPackets.stream()
                .mapToLong(it -> it.packetTotal)
                .reduce(Long.MAX_VALUE, Math::min);
        } else if (packet.type == 3) {
            return packet.subPackets.stream()
                .mapToLong(it -> it.packetTotal)
                .reduce(0, Math::max);
        } else if (packet.type == 5) {
            return packet.subPackets.get(0).packetTotal > packet.subPackets.get(1).packetTotal
                ? 1 : 0;
        } else if (packet.type == 6) {
            return packet.subPackets.get(0).packetTotal < packet.subPackets.get(1).packetTotal
                ? 1 : 0;
        } else if (packet.type == 7) {
            return packet.subPackets.get(0).packetTotal == packet.subPackets.get(1).packetTotal
                ? 1 : 0;
        }
        throw new RuntimeException();
    }


    private class Message {
        private String message;
        private int versionSum;
        private int position;
        private Packet packet;

        Message(String message) {
            this.message = message;
            this.position = 0;
        }

        private String getNext(int i) {
            final var part = message.substring(position, position + i);
            position += i;
            return part;
        }
    }

    @NoArgsConstructor
    private class Packet {
        int version;
        int type;

        long packetTotal;

        String content;

        List<Packet> subPackets = new ArrayList<Packet>();
    }

    @AllArgsConstructor
    enum HexToBinary {
        ZERO("0", "0000"),
        ONE("1", "0001"),
        TWO("2", "0010"),
        THREE("3", "0011"),
        FOUR("4", "0100"),
        FIVE("5", "0101"),
        SIX("6", "0110"),
        SEVEN("7", "0111"),
        EIGHT("8", "1000"),
        NINE("9", "1001"),
        A("A", "1010"),
        B("B", "1011"),
        C("C", "1100"),
        D("D", "1101"),
        E("E", "1110"),
        F("F", "1111");

        private String hexVal;
        private String binaryVal;

        public static String getBinaryFromHex(String hex) {
            return Arrays.stream(HexToBinary.values())
                .filter(it -> it.hexVal.equals(hex))
                .map(it -> it.binaryVal)
                .findFirst().orElseThrow();
        }
    }

}