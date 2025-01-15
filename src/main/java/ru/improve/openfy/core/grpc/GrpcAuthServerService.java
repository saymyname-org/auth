package ru.improve.openfy.core.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.oauth2.jwt.Jwt;
import ru.improve.openfy.core.models.Session;
import ru.improve.openfy.core.repositories.SessionRepository;
import ru.improve.openfy.core.security.TokenService;
import ru.improve.openfy.core.services.SessionService;
import ru.improve.skufify.grpc.AuthClientGrpc;
import ru.improve.skufify.grpc.AuthClientService;

@RequiredArgsConstructor
@GrpcService
public class GrpcAuthServerService extends AuthClientGrpc.AuthClientImplBase {

    private final SessionService sessionService;

    private final SessionRepository sessionRepository;

    private final TokenService tokenService;

    @Override
    public void checkUserByToken(AuthClientService.CheckUserRequest request,
                                 StreamObserver<AuthClientService.CheckUserResponse> responseObserver) {

        String token = request.getToken();
        if (token == null) {
            createAndSendResponse(null, false, responseObserver);
            return;
        }

        Jwt jwtToken = tokenService.parseJwt(token);
        long sessionId = tokenService.getSessionId(jwtToken);

        Session session = sessionRepository.findById(sessionId).orElse(null);
        if (session == null || !sessionService.checkSessionEnable(session)) {
            createAndSendResponse(null, false, responseObserver);
            return;
        }

        createAndSendResponse(session.getUser().getId(), true, responseObserver);
    }

    private void createAndSendResponse(Integer userId, boolean isAuth,
                                       StreamObserver<AuthClientService.CheckUserResponse> responseObserver) {

        AuthClientService.CheckUserResponse checkUserResponse = AuthClientService.CheckUserResponse.newBuilder()
                .setIsAuth(isAuth)
                .build();

        if (userId != null) {
            checkUserResponse.newBuilderForType().setUserId(userId);
        }

        responseObserver.onNext(checkUserResponse);
        responseObserver.onCompleted();
    }
}
