package com.diploma.server

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

@Component
class MdnsPublisher {
    @EventListener(ApplicationReadyEvent::class)
    fun publishMdnsService() {
        val address = InetAddress.getLocalHost()
        val jmdns = JmDNS.create(address)

        val serviceInfo = ServiceInfo.create(
            "_http._tcp.local.",     // Тип сервиса
            "shop",        // Имя сервиса
            8080,                    // Порт (укажи порт, на котором работает Spring Boot)
            "path=/api"              // Описание (необязательно)
        )

        jmdns.registerService(serviceInfo)
        println("✅mDNS service is registered as '${serviceInfo.name}.local:${serviceInfo.port}'")
    }
}