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


import io.confluent.developer.avro.User;
import lombok.RequiredArgsConstructor;


@SpringBootApplication
public class SpringCcloudAvroApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringCcloudAvroApplication.class, args);
  }

  @Bean
  NewTopic newtopicAvro() {
    return TopicBuilder.name("test_schema_recordname").partitions(6).replicas(3).build();
  }
}

@RequiredArgsConstructor
@Component
class Producer {

  // private final KafkaTemplate<Integer, Hobbit> template;
  private final  KafkaTemplate<String, User> template;

  Faker faker;

  @EventListener(ApplicationStartedEvent.class)
  public void generate() {

    faker = Faker.instance();
    while (true) {
      final String cust_id = faker.idNumber().valid();
      String formatted_cust_id = cust_id.replaceAll("-", "");
      final String ktp1 = "327106301";
      final String ktp2 = faker.number().digits(8);
      String formatted_ktp = ktp1.concat(ktp2);
      final String fullname = faker.name().fullName();
      final String phone_number = faker.phoneNumber().cellPhone();
      User send_value = new User(formatted_cust_id,formatted_ktp,fullname,phone_number);
      template.send("test_schema_recordname",formatted_ktp, send_value);
      System.out.println("Sent "+send_value);
      try {
        Thread.sleep(100); // Sleep for 5 seconds
      } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
      }
    }
  }
}
