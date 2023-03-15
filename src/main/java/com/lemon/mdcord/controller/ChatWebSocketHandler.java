package com.lemon.mdcord.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.lemon.mdcord.domain.chat.ChannelChat;
import com.lemon.mdcord.dto.chat.ChatCreateRequest;
import com.lemon.mdcord.dto.chat.MessageType;
import com.lemon.mdcord.repository.ChannelListRepository;
import com.lemon.mdcord.service.channel.ChannelListService;
import com.lemon.mdcord.service.channel.ChannelMemberService;
import com.lemon.mdcord.service.chat.ChannelChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private static Map<Long, List<WebSocketSession>> channelMap = new ConcurrentHashMap<>();
    private final ChannelChatService channelChatService;
    private final ChannelMemberService channelMemberService;
    private final ChannelListService channelListService;
    private final ChannelListRepository channelListRepository;
    private final ObjectMapper objectMapper;

    /**
     * application이 실행될 때 단 한번만,
     * 의존성 주입이 끝난 이후 실행
     *
     */
    @PostConstruct
    private void init() {
        // 모든 채널의 ID 목록
        List<Long> channelIds = channelListRepository.findAll().stream()
                .map(o -> o.getId())
                .collect(Collectors.toList());

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
            String payload = message.getPayload();
            ChatCreateRequest request = payloadToChatCreateRequest(payload);
            log.debug("payload : " + payload);

            ChannelChat channelChat = null;
            channelChat = handleChannelChatByMessageType(request, channelChat);
            TextMessage modifiedMessage = modifiedMessage(payload, channelChat);

            // 메시지 소켓 통신
            List<WebSocketSession> webSocketSessions = channelMap.get(request.getChannelId());
            for(WebSocketSession webSocketSession : webSocketSessions) {
                webSocketSession.sendMessage(modifiedMessage);
            }
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
        List<Long> MemberChannels = getMemberChannelIds(memberId);

        // 소속 채널에 webSocket 추가
        for(Long memberChannel : MemberChannels) {
            List<WebSocketSession> webSocketSessions = channelMap.get(memberChannel);
            webSocketSessions.add(session);
            log.debug(session + " 클라이언트 " + memberChannel + " 채널 접속");
        }
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
        List<Long> MemberChannels = getMemberChannelIds(memberId);

        // 소속 채널에 webSocket 제거
        for(Long memberChannel : MemberChannels) {
            List<WebSocketSession> webSocketSessions = channelMap.get(memberChannel);
            webSocketSessions.remove(session);
            log.debug(session + " 클라이언트 " + memberChannel + " 채널 접속 해제");
        }
    }

    /**
     * 메시지 타입에 따른 처리 핸들링
     * 
     * @param request
     * @param channelChat
     * @return
     */
    private ChannelChat handleChannelChatByMessageType(ChatCreateRequest request, ChannelChat channelChat) {
        MessageType messageType = request.getMessageType();
        if(messageType != null && messageType.equals(MessageType.SEND)) {
            channelChat = channelChatService.createChannelChat(request);
        }
        else if(messageType != null && messageType.equals(MessageType.EDIT)) {
            channelChat = channelChatService.changeChannelChatInfo(request);
        }
        else if(messageType != null && messageType.equals(MessageType.DELETE)) {
            channelChat = channelChatService.deleteChannelChatInfo(request);
        }
        else {
            log.error("message type : {}", messageType);
        }
        return channelChat;
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
    private ChatCreateRequest payloadToChatCreateRequest(String payload) {
        Gson gson = new Gson();
        return gson.fromJson(payload, ChatCreateRequest.class);
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

    /**
     * message 정보에 채팅 ID, 생성일시 데이터 추가
     * 
     * @param payload
     * @param channelChat
     * @return
     * @throws JsonProcessingException
     */
    private TextMessage modifiedMessage(String payload, ChannelChat channelChat) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);

        ((ObjectNode)jsonNode).put("chatId", channelChat.getId());
        ((ObjectNode)jsonNode).put("channelName", channelChat.getChannelList().getName());
        ((ObjectNode)jsonNode).put("createDate", channelChat.getCreateDate().toString());

        return new TextMessage(objectMapper.writeValueAsString(jsonNode));
    }
}
