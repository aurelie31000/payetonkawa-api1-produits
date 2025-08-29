package com.payetonkawa.product.config;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.Serializable;
@Configuration
public class RabbitMQConfig {
    public static final String PRODUIT_CHANGE_QUEUE = "produit-change-queue";
    @Bean
    public Queue produitChangeQueue() {
        return new Queue(PRODUIT_CHANGE_QUEUE, false);
    }
}
