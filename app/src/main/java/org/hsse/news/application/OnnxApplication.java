package org.hsse.news.application;

import ai.onnxruntime.OrtException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.hsse.news.model.OnnxModelRunner;
import org.hsse.news.util.ResourceUtil;

public class OnnxApplication {
  private static final String MODEL_PATH = ResourceUtil.getResource("/onnx_model/trfs-model.onnx");
  private static final String TOKENIZER_PATH = ResourceUtil.getResource("/onnx_model/tokenizer/tokenizer.json");
  private final OnnxModelRunner modelRunner;


  public OnnxApplication() throws IOException, OrtException {
    modelRunner = new OnnxModelRunner(OnnxApplication.MODEL_PATH, OnnxApplication.TOKENIZER_PATH);
  }

  private Map<String, Float> getResult(String text, List<String> labels) throws OrtException {
    return modelRunner.runModel(text, labels);
  }

  public Map<String, Float> predict(String text, List<String> labels) throws OrtException {
    String query = encodeString(text);
    return getResult(query, labels);
  }

  private String encodeString(String input) {
    final byte[] bytes = input.getBytes();
    return new String(bytes, StandardCharsets.UTF_8);
  }
}