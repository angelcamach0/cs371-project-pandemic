import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.chart.LineChart;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.chart.PieChart;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class GraphsMaker
{
	public static double[] infectedOniteration = new double[120];
	
	public static double[] deadOniteration = new double [120];
	
	public static double[] recoveredOniteration = new double[120];
	
	public static int[] itermult = new int[100];
	
	public static double healthy;
	public static double dead;
	public static double infected;
	
	/*
	@Override
	public void start(Stage primaryStage) throws Exception{
		
		init(primaryStage);
		
		
	}// of start
	*/
	
	protected static void init(Stage primaryStage) {
		
		
		HBox root = new HBox();
		
		Scene scene = new Scene(root, 450, 330);
		
		NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel("Iterations");
		
		NumberAxis yAxis = new NumberAxis(); 
		yAxis.setLabel("Number Infected / 1000");
		
		LineChart <Number, Number> lineGraph = new LineChart <Number, Number>(xAxis, yAxis);
		lineGraph.setTitle("Infected overtime");
		
		XYChart.Series<Number, Number> data = new XYChart.Series<>();
		int j = 0;
		//iterations the num
		while(itermult[j] != 0) {
			data.getData().add(new XYChart.Data<Number, Number>(itermult[j], infectedOniteration[j] / 1000));
			System.out.println("entered data");
			System.out.println(itermult[j] + " " + infectedOniteration[j]);
			j++;
		}
	
	
		
		lineGraph.getData().add(data);
		
		//start of changing color of line
		Node line = data.getNode().lookup(".chart-series-line");

		Color color = Color.RED; // decides color

		String rgb = String.format("%d, %d, %d",
		        (int) (color.getRed() * 255),
		        (int) (color.getGreen() * 255),
		        (int) (color.getBlue() * 255));

		
		line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
		//end of changing line color
		
		root.getChildren().add(lineGraph);
		
		primaryStage.setTitle("Number of people infected over time");
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}// of init
	
	//--------------------------
	
	protected static void CreateDeadChart(Stage chartStage) {
		
		HBox root = new HBox();
		
		Scene scene = new Scene(root, 450, 330);
		
		NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel("Iterations");
		
		NumberAxis yAxis = new NumberAxis(); 
		yAxis.setLabel("Number Dead / 1000");
		
		LineChart <Number, Number> lineGraph = new LineChart <Number, Number>(xAxis, yAxis);
		lineGraph.setTitle("Deaths overtime");
		
		XYChart.Series<Number, Number> data = new XYChart.Series<>();
		int j = 0;
		//iterations the num
		while(itermult[j] != 0) {
			data.getData().add(new XYChart.Data<Number, Number>(itermult[j], deadOniteration[j] / 1000));
			System.out.println("entered data");
			System.out.println(itermult[j] + " " + deadOniteration[j]);
			j++;
		}
	
	
		
		lineGraph.getData().add(data);
		
		//start of changing color of line
		Node line = data.getNode().lookup(".chart-series-line");

		Color color = Color.GREY; // decides color

		String rgb = String.format("%d, %d, %d",
		        (int) (color.getRed() * 255),
		        (int) (color.getGreen() * 255),
		        (int) (color.getBlue() * 255));

		
		line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
		//end of changing line color
		
		root.getChildren().add(lineGraph);
		
		chartStage.setTitle("Number of deaths over time");
		
		chartStage.setScene(scene);
		chartStage.show();
	}// of create death chart
	
	protected static void CreateRecoveredChart(Stage chartStage) {
HBox root = new HBox();
		
		Scene scene = new Scene(root, 450, 330);
		
		NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel("Iterations");
		
		NumberAxis yAxis = new NumberAxis(); 
		yAxis.setLabel("Number Recovered / 1000");
		
		LineChart <Number, Number> lineGraph = new LineChart <Number, Number>(xAxis, yAxis);
		lineGraph.setTitle("Recovered overtime");
		
		XYChart.Series<Number, Number> data = new XYChart.Series<>();
		int j = 0;
		//iterations the num
		while(itermult[j] != 0) {
			data.getData().add(new XYChart.Data<Number, Number>(itermult[j], recoveredOniteration[j] / 1000));
			System.out.println("entered data");
			System.out.println(itermult[j] + " " + recoveredOniteration[j]);
			j++;
		}
	
	
		
		lineGraph.getData().add(data);
		
		//start of changing color of line
		Node line = data.getNode().lookup(".chart-series-line");

		Color color = Color.GREEN; // decides color

		String rgb = String.format("%d, %d, %d",
		        (int) (color.getRed() * 255),
		        (int) (color.getGreen() * 255),
		        (int) (color.getBlue() * 255));

		
		line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");
		//end of changing line color
		
		root.getChildren().add(lineGraph);
		
		chartStage.setTitle("Number of people recovered over time");
		
		chartStage.setScene(scene);
		chartStage.show();
	}// of recovered chart
	
	protected static void CreatePieChart(Stage chartStage) {
		ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
		
		new PieChart.Data("Healthy", healthy),
		new PieChart.Data("Dead", dead),
		new PieChart.Data("Infected", infected)
				);
		//create chart and assign data
		
		PieChart pChart = new PieChart(pieData);
		
		HBox root = new HBox();
		
		Scene scene = new Scene(root, 450, 330);
		
		pChart.setLegendVisible(false);
		
		root.getChildren().add(pChart);
		
		chartStage.setTitle("World levels");
		
		chartStage.setScene(scene);
		chartStage.show();
		
		
	    applyCustomColorSequence(
	    	      pieData, 
	    	      "blue", 
	    	      "grey",  
	    	      "crimson"
	    	    );
	    	  }

	    	

private static void applyCustomColorSequence(ObservableList<PieChart.Data> pieChartData, String... pieColors) {
    int i = 0;
    for (PieChart.Data data : pieChartData) {
      data.getNode().setStyle("-fx-pie-color: " + pieColors[i % pieColors.length] + ";");
      i++;
    }
  }// of apply custom colors to pieChart

protected static void resetGraphs() {
	for(int i = 0; i < itermult.length; i++) {
		itermult[i] = 0;
		infectedOniteration[i] = 0;
		deadOniteration[i] = 0;
		recoveredOniteration[i] = 0;
				
	}
}
	
	/*public static void main(String[] args) {
		launch(args);
	}*/
	
}
