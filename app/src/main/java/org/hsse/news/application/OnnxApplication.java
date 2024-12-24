package org.hsse.news.application;

import ai.onnxruntime.OrtException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.hsse.news.model.OnnxModelRunner;
import org.hsse.news.util.ResourceUtil;

public class OnnxApplication {
  private static final String MODEL_PATH = ResourceUtil.getResource("/onnx_model/trfs-model.onnx");
  private static final String TOKENIZER_PATH = ResourceUtil.getResource("/onnx_model/tokenizer/tokenizer.json");


  private Map<String, Float> start(String text, List<String> labels) throws OrtException, IOException {
    OnnxModelRunner modelRunner = new OnnxModelRunner(OnnxApplication.MODEL_PATH, OnnxApplication.TOKENIZER_PATH);
    return modelRunner.runModel(text, labels);
  }
  /***
   * Simple method that runs the model
   * @throws OrtException when neurone doesn`t work
   * @throws IOException when have some troubles with filepath
   */
  public void start() throws OrtException, IOException {
    String query = "Как написать нейронную сеть, чтобы работала на Java и на Python";
    final var inputText = encodeString(query);
    final var candidateLabels = Arrays.asList("DevOps", "IT", "Backend", "Data Science", "Machine Learning", "Cybersecurity", "Database Admin");

    var result = start(inputText, candidateLabels);

    System.out.println(query);
    System.out.println(result);
  }

  public String encodeString(String input) {
    final byte[] bytes = input.getBytes();
    return new String(bytes, StandardCharsets.UTF_8);
  }

  public void getDataFromJson() {
    // TODO: Можно сделать так, чтобы приложуха получала данные из JSON
  }

  public void convertResponseToJson() {

  }
}