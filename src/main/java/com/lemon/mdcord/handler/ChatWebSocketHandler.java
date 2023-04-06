package com.lemon.mdcord.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.lemon.mdcord.dto.socket.MessageType;
import com.lemon.mdcord.dto.socket.SockectMessageRequest;
import com.lemon.mdcord.repository.ChannelListRepository;
import com.lemon.mdcord.service.MessageTypeFactory;
import com.lemon.mdcord.service.MessageTypeInterface;
import com.lemon.mdcord.service.channel.ChannelListService;
import com.lemon.mdcord.service.channel.ChannelMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
    // 채널별 접속 세션 Map 
    private static Map<Long, List<WebSocketSession>> channelMap = new ConcurrentHashMap<>();
    // 모든 세션 접속자 상태
    private static List<WebSocketSession> memberStateList = new ArrayList<>();
    private final ChannelMemberService channelMemberService;
    private final ChannelListService channelListService;
    private final ChannelListRepository channelListRepository;
    private final MessageTypeFactory messageTypeFactory;
    private final ObjectMapper objectMapper;

    /**
     * application이 실행될 때 단 한번만,
     * 의존성 주입이 끝난 이후 실행
     */
    @PostConstruct
    private void init() {
        // 모든 채널의 ID 목록
        List<Long> channelIds = channelListRepository.findAll().stream()
                .map(o -> o.getId())
                .collect(Collectors.toList());

        // 모든 접속자 상태 OFF
        channelMemberService.changeAllStateOFF();

        // 채널 ID별 Map
        for(Long channelId : channelIds) {
            channelMap.put(channelId, new ArrayList<>());
            log.debug("channelId : {}", channelId);
        }
    }

    /**
     * Client가 채팅 입력 시 호출
     * 
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            // DTO로 변환
            SockectMessageRequest request = payloadToSockectMessageRequest(message.getPayload());
            if(request.getMessageType() == null) {
                log.error("message type : {}", request.getMessageType());
                return;
            }

            // 메시지 타입에 따른 처리 핸들링
            handleSockectMessageByMessageType(request, message);
        } catch (Exception e) {
            log.error("getClass : {}", e.getClass());
            log.error("getLocalizedMessage : {}", e.getLocalizedMessage());
            log.error("getMessage : {}", e.getMessage());
            log.error("getCause : {}", e.getCause());
        }
    }

    /**
     * Client가 접속 시 호출
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 사용자 ID를 파라미터로 받음
        String memberId = getMemberId(session);
        // 사용자가 속한 채널 ID 목록
        List<Long> memberChannels = getMemberChannelIds(memberId);
        // 현재 접속 사용자 목록
        memberStateList.add(session);

        // 소속 채널에 webSocket 추가
        for(Long memberChannel : memberChannels) {
            List<WebSocketSession> webSocketSessions = channelMap.get(memberChannel);
            webSocketSessions.add(session);
            log.debug(session + " 클라이언트 " + memberChannel + " 채널 접속");
        }

        // 세션 접속 시 사용자 접속 정보 전송
        sendMemberAccessInfo(memberId, memberChannels, "ON");
    }

    /**
     * Client가 접속 해제 시 호출
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // 사용자 ID를 파라미터로 받음
        String memberId = getMemberId(session);
        // 사용자가 속한 채널 목록
        List<Long> memberChannels = getMemberChannelIds(memberId);

        // 소속 채널에 webSocket 제거
        for(Long memberChannel : memberChannels) {
            List<WebSocketSession> webSocketSessions = channelMap.get(memberChannel);
            webSocketSessions.remove(session);
            log.debug(session + " 클라이언트 " + memberChannel + " 채널 접속 해제");
        }

        // 세션 해제 시 사용자 접속 정보 전송 
        sendMemberAccessInfo(memberId, memberChannels, "OFF");
    }

    /**
     * 세션 접속 / 해제 시 사용자 접속 정보 전송 
     * 
     * @param memberId
     * @param memberChannels
     * @param state
     */
    private void sendMemberAccessInfo(String memberId, List<Long> memberChannels, String state) {
        if(memberChannels.size() > 0) {
            // 사용자 + 접속 정보 TextMessage화
            TextMessage message = null;
            try {
                Map<String, Object> memberAccessMap = new HashMap<>();
                memberAccessMap.put("messageType", MessageType.ACCESS_SESSION);
                Map<String, String> messageInfo = new HashMap<>();
                messageInfo.put(memberId, state);
                memberAccessMap.put("messageInfo", messageInfo);

                JsonNode jsonNode = objectMapper.readTree(new Gson().toJson(memberAccessMap));

                message = new TextMessage(objectMapper.writeValueAsString(jsonNode));
            } catch(Exception e) {
                log.error("getClass : {}", e.getClass());
                log.error("getLocalizedMessage : {}", e.getLocalizedMessage());
                log.error("getMessage : {}", e.getMessage());
                log.error("getCause : {}", e.getCause());
            }

            // TODO - 부적절함. 수정 필요 - d
            // TextMessage가 null이 아닐 경우, 현재 세션 접속자들에게 알림
            if(message != null) {
                for(WebSocketSession webSocketSession : memberStateList) {
                    try {
                        webSocketSession.sendMessage(message);
                    } catch(Exception e) {
                        log.error("getClass : {}", e.getClass());
                        log.error("getLocalizedMessage : {}", e.getLocalizedMessage());
                        log.error("getMessage : {}", e.getMessage());
                        log.error("getCause : {}", e.getCause());
                    }
                }
            }
        }
        channelMemberService.changeMemberState(memberId, state);
    }

    /**
     * 메시지 타입에 따른 처리 핸들링
     * 
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    private void handleSockectMessageByMessageType(SockectMessageRequest request, TextMessage message) throws IOException {
        String payload = message.getPayload();
        log.debug("payload : " + payload);

        MessageType messageType = request.getMessageType();
        if(messageType == null) {
            log.error("message type : {}", messageType);
        }

        MessageTypeInterface messageTypeInterface = messageTypeFactory.getMessageTypeInterface(messageType.name());
        messageTypeInterface.handle(messageType.name(), payload, channelMap);
    }

    /**
     * 사용자가 소속된 채널 ID 목록
     * 
     * @param memberId
     * @return
     */
    private List<Long> getMemberChannelIds(String memberId) {
        Set<Long> joinedDept0ChannelMemberId = channelMemberService.findByMemberId(memberId).stream()
                .map(o -> o.getChannelList().getId())
                .collect(Collectors.toSet());

        Set<Long> joinedDept1ChannelMemberId = channelListService.findByParentId(joinedDept0ChannelMemberId).stream()
                .map(o -> o.getId())
                .collect(Collectors.toSet());

        return channelListService.findByParentId(joinedDept1ChannelMemberId).stream()
                .map(o -> o.getId())
                .collect(Collectors.toList());
    }

    /**
     * payload 데이터를 ChatCreateRequest로 변환
     * 
     * @param payload
     * @return
     */
    private SockectMessageRequest payloadToSockectMessageRequest(String payload) {
        return new Gson().fromJson(payload, SockectMessageRequest.class);
    }

    /**
     * session parameter에서 사용자 ID 추출
     * 
     * @param session
     * @return
     */
    private String getMemberId(WebSocketSession session) {
        return session.getUri().getQuery().split("=")[1];
    }
}
