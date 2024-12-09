package org.hsse.news.model;

import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import ai.onnxruntime.OrtSession.SessionOptions;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OnnxModelRunner {
  private final OrtSession session;
  private final OrtEnvironment environment;
  private final HuggingFaceTokenizer tokenizer;

  public OnnxModelRunner(String modelPath, String tokenizerPath) throws OrtException, IOException {
    environment = OrtEnvironment.getEnvironment();
    SessionOptions options = new SessionOptions();

    session = environment.createSession(modelPath, options);
    tokenizer = HuggingFaceTokenizer.newInstance(Paths.get(tokenizerPath));
  }

  public Map<String, Float> runModel(String text, List<String> labels) throws OrtException {
    final var logits = new ArrayList<Float>();

    for (String label : labels) {
      final var encode = tokenizer.encode(List.of(text, label));
      final var inputTokens = encode.getIds();
      final var attentionMask = encode.getAttentionMask();

      OnnxTensor t1 = OnnxTensor.createTensor(environment, new long[][]{inputTokens});
      OnnxTensor t2 = OnnxTensor.createTensor(environment, new long[][]{attentionMask});

      var inputs = Map.of("input_ids", t1, "attention_mask", t2);
      try (var result = session.run(inputs)) {
        float[][] resultLogits = (float[][]) result.get(0).getValue();
        logits.add(resultLogits[0][0]);
      }
    }

    return softmax(labels, logits);
  }

  private static Map<String, Float> softmax(List<String> labels, List<Float> logits) {
    float maxLogit = logits.stream().max(Float::compareTo).orElse(Float.NEGATIVE_INFINITY);
    float sumExp = 0.0f;
    float[] expLogits = new float[logits.size()];
    Map<String, Float> results = new HashMap<>();

    for (int i = 0; i < logits.size(); i++) {
      expLogits[i] = (float) Math.exp(logits.get(i) - maxLogit);
      sumExp += expLogits[i];
    }
    for (int i = 0; i < expLogits.length; i++) {
      results.put(labels.get(i), expLogits[i] / sumExp);
    }

    return results.entrySet().stream()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (e1, e2) -> e1,
            LinkedHashMap::new
            )
        );
  }
}
