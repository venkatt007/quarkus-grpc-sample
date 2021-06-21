package com.quarkus.sample;

import javax.enterprise.context.ApplicationScoped;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

@ApplicationScoped
public class MyInterceptor implements ServerInterceptor {

	@Override
	public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
			ServerCallHandler<ReqT, RespT> next) {

		System.out.println("Authority -- " + call.getAuthority());
		System.out.println("Headers -- " + headers.toString());
		ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);
		return delegate;
	}

}
