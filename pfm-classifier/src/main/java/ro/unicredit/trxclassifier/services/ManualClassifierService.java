package ro.unicredit.trxclassifier.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.unicredit.trxclassifier.services.dtos.ResponseTransactionDto;

@Service
@RequiredArgsConstructor
public class ManualClassifierService {
    private final RestTemplate restTemplate;
    @Value("${url.transactions}")
    private String BASE_TRANSACTIONS_URL;

    public ResponseTransactionDto classify(Long txId, Long categoryId) {
        String url = BASE_TRANSACTIONS_URL + "/{txId}/{categoryId}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseTransactionDto> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                ResponseTransactionDto.class,
                txId,
                categoryId
        );

        return responseEntity.getBody();
    }
}