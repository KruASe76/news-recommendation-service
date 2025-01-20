package org.hsse.news.model;

import ai.djl.huggingface.tokenizers.Encoding;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class OnnxModelRunner {
  private final OrtSession session;
  private final OrtEnvironment environment;
  private final HuggingFaceTokenizer tokenizer;

  public OnnxModelRunner(final String modelPath, final String tokenizerPath) throws OrtException, IOException {
    environment = OrtEnvironment.getEnvironment();
    final SessionOptions options = new SessionOptions(); // NOPMD

    session = environment.createSession(modelPath, options);
    tokenizer = HuggingFaceTokenizer.newInstance(Paths.get(tokenizerPath));
  }


  public Map<String, Float> runModel(final String text, final List<String> labels) throws OrtException {
    final var logits = new ArrayList<Float>();
    Map<String, OnnxTensor> inputs;
    OnnxTensor tensor1; // NOPMD - Always close
    OnnxTensor tensor2; // NOPMD - Always close

    Encoding encode;
    long[] inputTokens;
    long[] attentionMask;
    float[][] resultLogits;

    for (final String label : labels) {
      encode = tokenizer.encode(List.of(text, label));
      inputTokens = encode.getIds();
      attentionMask = encode.getAttentionMask();
      tensor1 = OnnxTensor.createTensor(environment, new long[][]{inputTokens}); // NOPMD
      tensor2 = OnnxTensor.createTensor(environment, new long[][]{attentionMask}); // NOPMD

      inputs = Map.of("input_ids", tensor1, "attention_mask", tensor2);
      try (var result = session.run(inputs)) {
        resultLogits = (float[][]) result.get(0).getValue();
        logits.add(resultLogits[0][0]);
      }

      tensor1.close();
      tensor2.close();
    }

    return softmax(labels, logits);
  }

  private static Map<String, Float> softmax(final List<String> labels, final List<Float> logits) {
    final float maxLogit = logits.stream().max(Float::compareTo).orElse(Float.NEGATIVE_INFINITY);
    float sumExp = 0.0f;
    final float[] expLogits = new float[logits.size()];
    final ConcurrentHashMap<String, Float> results = new ConcurrentHashMap<>();

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
