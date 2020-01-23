package com.rabbitmq.example.publisherservice;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@EnableBinding(Source.class)
public class MessagePublisher {

    @Autowired
    Source source;

    @PostMapping(value="/txn")
    public String sendMessage(@RequestBody String payload){
        ObjectMapper mapper = new ObjectMapper();
        Transaction txn = null;
        try {
            txn = mapper.readValue(payload, Transaction.class);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        source.output().send(MessageBuilder.withPayload(txn).setHeader("myheader", "myheaderValue").build());
        System.out.println("Successfully sent to rabbitmq");
        return "success";
    }
}
