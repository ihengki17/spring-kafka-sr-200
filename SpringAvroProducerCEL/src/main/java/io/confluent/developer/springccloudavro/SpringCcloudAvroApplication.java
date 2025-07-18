package io.confluent.developer.springccloudavro;

import com.github.javafaker.Faker;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;


import io.confluent.developer.avro.Transaction;
import lombok.RequiredArgsConstructor;


@SpringBootApplication
public class SpringCcloudAvroApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringCcloudAvroApplication.class, args);
  }

  @Bean
  NewTopic newtopicAvro() {
    return TopicBuilder.name("transation_rules").partitions(6).replicas(3).build();
  }
}

@RequiredArgsConstructor
@Component
class Producer {

  // private final KafkaTemplate<Integer, Hobbit> template;
  private final  KafkaTemplate<String, Transaction> template;

  Faker faker;

  @EventListener(ApplicationStartedEvent.class)
  public void generate() {

    faker = Faker.instance();
    while (true) {
      final String cust_id = faker.idNumber().valid();
      String formatted_cust_id = cust_id.replaceAll("-", "");
      final String trans_id = faker.number().digits(9);
      final String card_number = faker.business().creditCardNumber();
      String formatted_card_number = card_number.replaceAll("-", "");
      final String amount = faker.commerce().price();
      final String card_type = faker.business().creditCardType();
      Transaction send_value = new Transaction(formatted_cust_id,trans_id,formatted_card_number,amount,card_type);
      template.send("transation_rules",formatted_card_number, send_value);
      System.out.println("Sent "+send_value);
      try {
        Thread.sleep(100); // Sleep for 5 seconds
      } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
      }
    }
  }
}
