package com.quarkus.sample;

import fileupload.FileUploadRequest;
import fileupload.FileUploadResponse;
import fileupload.MutinyFileServiceGrpc;
import fileupload.Status;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@GrpcService
public class FileUploadService extends MutinyFileServiceGrpc.FileServiceImplBase {
	@Override
	public Uni<FileUploadResponse> upload(Multi<FileUploadRequest> request) {
		
		request.subscribe().with(
                item -> {
                	System.out.println(item.getMetadata());
                	System.out.println(item.getFile());
                },
                failure -> System.out.println("Failed with " + failure),
                () -> System.out.println("Completed"));
		
		return Uni.createFrom().item(FileUploadResponse.newBuilder().setStatus(Status.SUCCESS).setName("File upload successful").build());
	}
}
