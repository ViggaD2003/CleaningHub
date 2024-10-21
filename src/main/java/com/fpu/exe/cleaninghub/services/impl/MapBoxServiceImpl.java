package com.fpu.exe.cleaninghub.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpu.exe.cleaninghub.dto.response.StaffDistanceInfo;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.services.interfc.MapBoxService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapBoxServiceImpl implements MapBoxService {

    @Value("${mapbox.api.key}")

    private String apiKey;

    @Value("${mapbox.api.url}")
    private String apiUrl;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public MapBoxServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<StaffDistanceInfo> calculateDistanceBetweenTwoLocation(StringBuilder coordinates, List<User> availableStaffs) throws Exception {
        String url = String.format("%s/%s?access_token=%s&annotations=distance,duration",
                apiUrl, coordinates, apiKey);

        Mono<String> jsonRes = this.webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
        return processDistanceMatrix(jsonRes.block(), availableStaffs);
    }


    private List<StaffDistanceInfo> processDistanceMatrix(String jsonResponse, List<User> availableStaffs) throws Exception {

//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode rootNode = objectMapper.readTree(jsonResponse);
//
//            JsonNode durations = rootNode.path("durations");
//            JsonNode distances = rootNode.path("distances");
//
//            if (distances.isArray() && durations.isArray()) {
//                ArrayNode resultArray = objectMapper.createArrayNode();
//
//                ObjectNode node = objectMapper.createObjectNode();
//                node.put("durations", durations.get(0));
//                node.put("distances", distances.get(0));
//                resultArray.add(node);
//
//                return resultArray;
//
//            } else {
//                // Nếu không có distances hoặc durations, trả về mảng rỗng
//                return objectMapper.createArrayNode();
//            }
//        } catch (Exception e) {
//            e.printStackTrace(); // Xử lý lỗi nếu có
//            return objectMapper.createArrayNode(); // Trả về mảng rỗng trong trường hợp lỗi
//        }

        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        JsonNode durationsNode = rootNode.path("durations");
        JsonNode distancesNode = rootNode.path("distances");

        List<StaffDistanceInfo> staffDistanceInfos = new ArrayList<>();

        // Duyệt qua danh sách nhân viên và lấy khoảng cách và thời gian tương ứng
        for (int i = 1; i <= availableStaffs.size(); i++) {
            User staff = availableStaffs.get(i - 1);

            // Kiểm tra xem chỉ số 0 có hợp lệ không
            if (distancesNode.size() > 0 && distancesNode.get(0).isArray() &&
                    distancesNode.get(0).size() > i &&
                    distancesNode.get(0).get(i).isDouble() &&
                    durationsNode.size() > 0 && durationsNode.get(0).isArray() &&
                    durationsNode.get(0).size() > i &&
                    durationsNode.get(0).get(i).isDouble()) {

                double distance = distancesNode.get(0).get(i).asDouble();
                double duration = durationsNode.get(0).get(i).asDouble();
                staffDistanceInfos.add(new StaffDistanceInfo(staff, distance, duration));
            }
        }
        return staffDistanceInfos;
    }
}
