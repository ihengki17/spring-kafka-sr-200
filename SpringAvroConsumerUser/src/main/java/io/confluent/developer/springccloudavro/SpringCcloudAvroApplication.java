package io.confluent.developer.springccloudavro;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import io.confluent.developer.avro.User;

@SpringBootApplication
public class SpringCcloudAvroApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringCcloudAvroApplication.class, args);
  }
}

@Component
class Consumer {

  @KafkaListener(topics = {"test_schema_recordname"})
  public void consume(ConsumerRecord<Integer, User> record) {
    System.out.println("received = " + record.value() + " with key " + record.key());
  }
}
