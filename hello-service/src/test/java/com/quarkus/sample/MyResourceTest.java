package com.quarkus.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.google.protobuf.ByteString;

import examples.HelloReply;
import examples.HelloRequest;
import examples.MutinyGreeterGrpc;
import fileupload.File;
import fileupload.FileUploadRequest;
import fileupload.FileUploadResponse;
import fileupload.MetaData;
import fileupload.MutinyFileServiceGrpc;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;

@QuarkusTest
public class MyResourceTest {

	@Inject
	@GrpcClient("hello")
	MutinyGreeterGrpc.MutinyGreeterStub mutiny;

	@Inject
	@GrpcClient("file-upload")
	MutinyFileServiceGrpc.MutinyFileServiceStub fileUploadSvc;

	@Test
	public void helloGrpc() throws InterruptedException, ExecutionException {
		HelloReply helloReply = mutiny.sayHello(HelloRequest.newBuilder().setName("Test").build()).subscribe()
				.asCompletionStage().get();
		assertEquals("Hello Test", helloReply.getMessage());
	}

	@Test
	public void testFileUpload() throws InterruptedException, ExecutionException, IOException {

		// input file for testing
		Path path = Paths.get("C:\\Users\\venkatesh.k\\Documents\\Quarkus\\Solutions\\hello-service\\src\\test\\resources\\test.txt");

		List<FileUploadRequest> requests = new ArrayList<FileUploadRequest>();

//		// upload file as chunk
		InputStream inputStream = Files.newInputStream(path);
		byte[] bytes = new byte[4096];
		int size;
		while ((size = inputStream.read(bytes)) > 0) {
			FileUploadRequest uploadRequest = FileUploadRequest.newBuilder().setMetadata(MetaData.newBuilder().setName("test").setType("txt").build())
					.setFile(File.newBuilder().setContent(ByteString.copyFrom(bytes, 0, size)).build()).build();
			requests.add(uploadRequest);
		}

		Multi<FileUploadRequest> fileUploadRequest = Multi.createFrom().items(requests.stream());

		FileUploadResponse fileUploadResponse = fileUploadSvc.upload(fileUploadRequest).subscribe().asCompletionStage()
				.get();

		assertEquals("File upload successful", fileUploadResponse.getName());
	}

}