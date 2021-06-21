package com.quarkus.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import examples.HelloReply;
import examples.HelloRequest;
import examples.MutinyGreeterGrpc;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class MyResourceTest {
	
	@Inject 
	@GrpcClient("second")
	MutinyGreeterGrpc.MutinyGreeterStub secondStub;
	
	@Inject 
	@GrpcClient("hello")
	MutinyGreeterGrpc.MutinyGreeterStub helloStub;

	@Test
	public void helloSecondServiceHello() throws InterruptedException, ExecutionException {
		HelloReply helloReply = secondStub.sayHello(HelloRequest.newBuilder().setName("Test").build()).subscribe().asCompletionStage().get();
		assertEquals("Hello Test from second service", helloReply.getMessage());
	}
	
	@Test
	public void helloTest() throws InterruptedException, ExecutionException {
		HelloReply helloReply = helloStub.sayHello(HelloRequest.newBuilder().setName("Test").build()).subscribe().asCompletionStage().get();
		assertEquals("Hello Test", helloReply.getMessage());
	}

}