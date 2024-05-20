package com.fx.funxtion.global.websocket.handler;


import com.fx.funxtion.domain.chat.entity.ChatMessage;
import com.fx.funxtion.domain.chat.entity.ChatRoom;
import com.fx.funxtion.domain.chat.repository.ChatMessageRepository;
import com.fx.funxtion.domain.chat.service.ChatService;
import com.fx.funxtion.domain.safepayment.entity.SafePayments;
import com.fx.funxtion.domain.safepayment.repository.SafePaymentsRepository;
import com.fx.funxtion.domain.safepayment.service.SafePaymentsService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    // 웹소켓 세션을 담아둘 리스트
    List<Map<String, Object>> roomSessionList = new ArrayList<>();
    ChatService chatService;
    @Autowired
    ChatMessageRepository chatMessageRepository;
    SafePaymentsService safePaymentsService;
    @Autowired
    SafePaymentsRepository safePaymentsRepository;
    // 메시지 발송
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        JsonObject obj = jsonToObjectParser(msg);

        // 메시지 요청된 채팅방과 같은 방에 존재하는 세션을 검색
        String rN = obj.get("roomNumber").getAsString();
        Map<String, Object> temp = new HashMap<>();
        if(!roomSessionList.isEmpty()) {
            for(Map<String, Object> roomSession : roomSessionList) {
                String roomNumber = roomSession.get("roomNumber").toString();

                if(roomNumber.equals(rN)) {
                    temp = roomSession;
                    break;
                }
            }

            for (String k : temp.keySet()) {
                if(k.equals("roomNumber")) {
                    continue;
                }


                WebSocketSession wss = (WebSocketSession) temp.get(k);
                if (wss != null) {
                    obj.addProperty("sessionLength", temp.keySet().size());
                    wss.sendMessage(new TextMessage(obj.toString()));

                }
            }

            if (obj.get("type").getAsString().equals("message")) {
                ChatMessage chatMessage;
                if(temp.keySet().size() >= 3) {
                    chatMessage = ChatMessage.builder()
                            .userId(obj.get("userId").getAsLong())
                            .roomId(obj.get("roomNumber").getAsLong())
                            .message(obj.get("msg").getAsString())
                            .readYn("Y")
                            .build();
                } else {
                    chatMessage = ChatMessage.builder()
                            .userId(obj.get("userId").getAsLong())
                            .roomId(obj.get("roomNumber").getAsLong())
                            .message(obj.get("msg").getAsString())
                            .build();
                }
                chatMessageRepository.save(chatMessage);
                System.out.println(obj.get("productId").getAsLong());
                System.out.println(obj.get("sellerId").getAsLong());
                System.out.println(obj.get("buyerId").getAsLong());


                // 안전거래 버튼 클릭 시 DB 저장.
                SafePayments safePaymentsEx = safePaymentsRepository.findByProductIdAndSellerIdAndBuyerId(obj.get("productId").getAsLong(), obj.get("sellerId").getAsLong(), obj.get("buyerId").getAsLong());
                SafePayments safePayments;
                if(safePaymentsEx == null && temp.keySet().size() >= 2 && obj.get("safe").getAsString() == "true") {
                    safePayments = SafePayments.builder()
                            .productId(obj.get("productId").getAsLong())
                            .sellerId(obj.get("sellerId").getAsLong())
                            .buyerId(obj.get("buyerId").getAsLong())
                            .status("SP01")
                            .build();
                    safePaymentsRepository.save(safePayments);
                }
                // 판매자가 안전거래 수락 시 DB status 컬럼 값 변경
                if(temp.keySet().size() >=2 && obj.get("msg").getAsString().equals("상품의 안전거래가 수락되었습니다.") ) {
                    safePaymentsEx.setStatus("SP02");
                    safePaymentsRepository.save(safePaymentsEx);
                }
            }

        }
    }

    // 소켓 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);



        boolean flag = false;

        String url = session.getUri().toString();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>socket {} >>>>>>>>>>>>>>>>>>>>>>>>> ", url);
        String roomNumber = url.split("/chat/")[1];
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>  {} >>>>>>>>>>>>>>>>>>>>>>>>> ", roomNumber);
        int idx = roomSessionList.size();
        if(!roomSessionList.isEmpty()) {

            for(int i=0; i<roomSessionList.size(); i++) {
                String rN = roomSessionList.get(i).get("roomNumber").toString();
                if(rN.equals(roomNumber)) {
                    flag = true;
                    idx = i;
                    break;
                }
            }
        }

        /*
        아래와 같은 구조로 데이터를 관리됨.
            - 하나의 객체에 roomNumberr(방번호)와 연결된 세션 객체들

            [
                {
                    "roomNumber":1,
                    "session_id1": {"id":"id1", "uri":"xxxx"},
                    "session_id2": {"id":"id2", "uri":"xxxx"}
                },
                {
                    "roomNumber":2,
                    "session_id3":{"id":"id3", "uri":"yyyy"}
                }
            ]
         */

        if (flag) {
            Map<String, Object> map = roomSessionList.get(idx);
            map.put(session.getId(), session);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("roomNumber", roomNumber);
            map.put(session.getId(), session);
            roomSessionList.add(map);
        }

        JsonObject obj = new JsonObject();
        obj.addProperty("type", "getId");
        obj.addProperty("sessionId", session.getId());

        session.sendMessage(new TextMessage(obj.toString()));
    }

    // 소켓 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if(!roomSessionList.isEmpty()) {
            for(Map<String, Object> roomSession : roomSessionList) {
                roomSession.remove(session.getId());
            }
        }
        super.afterConnectionClosed(session, status);
    }

    // Json parsing
    private static JsonObject jsonToObjectParser(String jsonStr) {
        JsonParser parser = new JsonParser();
        JsonObject obj = null;

        obj = (JsonObject) parser.parse(jsonStr);
        return obj;
    }
}
