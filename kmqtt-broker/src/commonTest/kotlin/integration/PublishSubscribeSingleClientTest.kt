package integration

import IgnoreJs
import MQTTClient
import mqtt.Subscription
import mqtt.broker.Broker
import mqtt.packets.Qos
import mqtt.packets.mqttv5.SubscriptionOptions
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals


@IgnoreJs
class PublishSubscribeSingleClientTest {

    @Test
    fun testPublish() {
        val sendPayload = "Test"
        val topic = "test/topic"

        var received = false

        val broker = Broker()
        val client = MQTTClient(5, "127.0.0.1", broker.port, null) {
            assertEquals(topic, it.topicName)
            assertContentEquals(sendPayload.encodeToByteArray().toUByteArray(), it.payload)
            assertEquals(Qos.AT_MOST_ONCE, it.qos)
            received = true
        }
        broker.step()

        client.subscribe(listOf(Subscription(topic, SubscriptionOptions(Qos.AT_MOST_ONCE))))

        broker.step()

        client.publish(false, Qos.AT_LEAST_ONCE, topic, sendPayload.encodeToByteArray().toUByteArray())

        var i = 0
        while (!received && i < 1000) {
            broker.step()
            client.step()
            i++
        }

        broker.stop()

        if (i >= 1000) {
            throw Exception("Test timeout")
        }
    }
}
