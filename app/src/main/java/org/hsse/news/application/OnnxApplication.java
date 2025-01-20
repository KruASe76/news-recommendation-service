package org.hsse.news.application;

import ai.onnxruntime.OrtException;
import org.hsse.news.model.OnnxModelRunner;
import org.hsse.news.util.ResourceUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class OnnxApplication {
  public static final String MODEL_PATH = ResourceUtil.getResource("/onnx_model/trfs-model.onnx");
  public static final String TOKENIZER_PATH = ResourceUtil.getResource("/onnx_model/tokenizer/tokenizer.json");
  private final OnnxModelRunner modelRunner;


  public OnnxApplication(final OnnxModelRunner modelRunner) {
    this.modelRunner = modelRunner;
  }

  private Map<String, Float> getResult(final String text, final List<String> labels) throws OrtException {
    return modelRunner.runModel(text, labels);
  }

  public Map<String, Float> predict(final String text, final List<String> labels) throws OrtException {
    final String query = encodeString(text);
    return getResult(query, labels);
  }

  private String encodeString(final String input) {
    final byte[] bytes = input.getBytes();
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
