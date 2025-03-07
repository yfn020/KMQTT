package mqtt.packets.mqttv5

import mqtt.MQTTException
import mqtt.packets.MQTTControlPacketType
import mqtt.packets.MQTTDeserializer
import mqtt.packets.mqtt.MQTTPingreq
import socket.streams.ByteArrayOutputStream

public class MQTT5Pingreq : MQTTPingreq(), MQTT5Packet {

    override fun toByteArray(): UByteArray {
        return ByteArrayOutputStream().wrapWithFixedHeader(MQTTControlPacketType.PINGREQ, 0)
    }

    public companion object : MQTTDeserializer {

        override fun fromByteArray(flags: Int, data: UByteArray): MQTT5Pingreq {
            checkFlags(flags)
            if (data.isNotEmpty())
                throw MQTTException(ReasonCode.MALFORMED_PACKET)
            return MQTT5Pingreq()
        }
    }
}
