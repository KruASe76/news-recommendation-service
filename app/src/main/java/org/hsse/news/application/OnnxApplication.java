package org.hsse.news.application;

import ai.onnxruntime.OrtException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.hsse.news.model.OnnxModelRunner;

public class OnnxApplication {
  private static final String MODEL_PATH = OnnxApplication.class
      .getResource("/onnx_model/trfs-model.onnx").getPath();
  private static final String TOKENIZER_PATH = OnnxApplication.class
      .getResource("/onnx_model/tokenizer/tokenizer.json").getPath();

  private Map<String, Float> start(String text, List<String> labels) throws OrtException, IOException {
    OnnxModelRunner modelRunner = new OnnxModelRunner(MODEL_PATH, TOKENIZER_PATH);
    return modelRunner.runModel(text, labels);
  }
  /***
   * Simple method that runs the model
   * @throws OrtException when neurone doesn`t work
   * @throws IOException when have some troubles with filepath
   */
  public void start() throws OrtException, IOException {
    final var inputText = "Как написать нейронную сеть, чтобы работала на Java и на Python";
    final var candidateLabels = Arrays.asList("DevOps", "IT", "Backend", "Data Science", "Machine Learning", "Cybersecurity", "Database Admin");

    var result = start(inputText, candidateLabels);

    System.out.println(inputText);
    System.out.println(result);
  }

  public void getDataFromJson() {
    // TODO: Можно сделать так, чтобы приложуха получала данные из JSON
  }

  public void convertResponseToJson() {
    // TODO: И возвращала в подобном же формате
  }
}