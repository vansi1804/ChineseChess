package com.nvs.config.logging;

import com.nvs.util.SensitiveDataMasker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggingService {

  private static final String REQUEST_ID = "request_id";

  public void logRequest(HttpServletRequest httpServletRequest, Object body) {
    String traceId = UUID.randomUUID().toString();
    httpServletRequest.setAttribute(REQUEST_ID, traceId);

    // Mask dữ liệu nhạy cảm trước khi ghi log
    String maskedRequestBody = SensitiveDataMasker.maskSensitiveData(body);

    String data = String.format(
        "\nLOGGING REQUEST BODY-----------------------------------\n" +
            "[TRACE-ID]: %s\n" +
            "[REQUEST-ID]: %s\n" +
            "[BODY REQUEST]: \n%s\n" +
            "LOGGING REQUEST BODY-----------------------------------\n",
        traceId,
        httpServletRequest.getAttribute(REQUEST_ID),
        maskedRequestBody
    );
    log.info(data);
  }

  public void logResponse(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object body) {
    String traceId = (String) httpServletRequest.getAttribute(REQUEST_ID);

    // Mask dữ liệu nhạy cảm trước khi ghi log
    String maskedResponseBody = SensitiveDataMasker.maskSensitiveData(body);

    String data = String.format(
        "\nLOGGING RESPONSE-----------------------------------\n" +
            "[TRACE-ID]: %s\n" +
            "[REQUEST-ID]: %s\n" +
            "[BODY RESPONSE]: \n%s\n" +
            "LOGGING RESPONSE-----------------------------------\n",
        traceId,
        httpServletRequest.getAttribute(REQUEST_ID),
        maskedResponseBody
    );
    log.info(data);
  }

}
