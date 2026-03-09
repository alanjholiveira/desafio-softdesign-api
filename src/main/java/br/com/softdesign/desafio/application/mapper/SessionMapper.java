package br.com.softdesign.desafio.application.mapper;

import br.com.softdesign.desafio.application.rest.v1.request.SessionRequest;
import br.com.softdesign.desafio.application.rest.v1.response.SessionResponse;
import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.domain.entity.Session;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SessionMapper {

    public static SessionResponse toResponse(Session session) {
        return SessionResponse.builder()
                .id(session.getId())
                .poll(session.getPoll().getName())
                .expiration(session.getExpiration())
                .createdAt(session.getCreatedAt())
                .isOpenSession(session.isOpenSession())
                .build();
    }

    public static Session toEntity(SessionRequest request) {
        return Session.builder()
                .expiration(request.getExpiration())
                .poll(Poll.builder().id(request.getPollId()).build())
                .build();
    }
}
