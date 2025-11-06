abstract class MLPipeline {

    public final void runPipeline() {
        prepareData();
        preprocessData();
        splitData();
        trainModel();
        tuneModel();
        evaluateModel();
        generateReport();
    }

    /**
     * step 1
     */
    protected void prepareData() {
        System.out.println("[Pipeline] Preparing data");
    }

    /**
     * step 2
     */
    protected void preprocessData() {
        System.out.println("[Pipeline] Preprocessing data");
    }

    /**
     * step 3
     */
    protected void splitData() {
        System.out.println("[Pipeline] Splitting data: 70% Train, 15% Validation, 15% Test");
    }

    /**
     * step 4: model specific construction techniques
     * 
     * implemented by subclasses
     */
    protected abstract void trainModel();

    /**
     * step 5
     */
    protected void tuneModel() {
        System.out.println("[Pipeline] Fine-tuning model");
    }

    /**
     * Step 6: model specific evaluation technique
     * 
     * implemented by subclasses
     * 
     */
    protected abstract void evaluateModel();

    /**
     * step 7
     */
    protected void generateReport() {
        System.out.println("[Pipeline] Generating final performance report");
    }
}

class LinearRegressionPipeline extends MLPipeline {
    @Override
    protected void trainModel() {
        System.out.println("[LinearRegressionPipeline] Training model");
    }

    @Override
    protected void evaluateModel() {
        System.out.println(
                "[LinearRegressionPipeline] eports the performance in terms of root mean squared error (RMSE)");
    }
}

class LogisticRegressionPipeline extends MLPipeline {
    @Override
    protected void trainModel() {
        System.out.println("[LogisticRegressionPipeline] Training model");
    }

    @Override
    protected void evaluateModel() {
        System.out.println(
                "[LogisticRegressionPipeline] eports the performance in terms of accuracy and F1 score, or ROC-AUC");
    }
}

class RandomForestPipeline extends MLPipeline {
    @Override
    protected void trainModel() {
        System.out.println("[RandomForestPipeline] Training model");
    }

    @Override
    protected void evaluateModel() {
        System.out.println(
                "[RandomForestPipeline] eports the performance in terms of accuracy and F1 score, or ROC-AUC");
    }
}

class CNNPipeline extends MLPipeline {
    @Override
    protected void trainModel() {
        System.out.println("[CNNPipeline] Training model");
    }

    @Override
    protected void evaluateModel() {
        System.out.println(
                "[CNNPipeline] eports the performance in terms of accuracy and F1 score, or ROC-AUC");
    }
}

class RNNPipeline extends MLPipeline {
    @Override
    protected void trainModel() {
        System.out.println("[RNNPipeline] Training model");
    }

    @Override
    protected void evaluateModel() {
        System.out.println(
                "[RNNPipeline] eports the performance in terms of accuracy and F1 score, or ROC-AUC");
    }
}

public class client {
    public static void main(String[] args) {

        MLPipeline regressionPipeline = new LinearRegressionPipeline();
        regressionPipeline.runPipeline();

        MLPipeline logisticPipeline = new LogisticRegressionPipeline();
        logisticPipeline.runPipeline();

        MLPipeline forestPipeline = new RandomForestPipeline();
        forestPipeline.runPipeline();

        MLPipeline cnnPipeline = new CNNPipeline();
        cnnPipeline.runPipeline();

        MLPipeline rnnPipeline = new RNNPipeline();
        rnnPipeline.runPipeline();
    }
}