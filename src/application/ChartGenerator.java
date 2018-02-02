package application;

import java.util.HashMap;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
	
public class ChartGenerator{
	    final static String s = "To be started";
	    final static String ip = "In progress";
	    final static String c = "Completed";
	    static int start,comp,prog=0;
	   
	    public static void start(Stage stage) {
	        stage.setTitle("Bar Chart");
	        final CategoryAxis xAxis = new CategoryAxis();
	        final NumberAxis yAxis = new NumberAxis();
	        final BarChart<String,Number> bc = 
	            new BarChart<String,Number>(xAxis,yAxis);
	        bc.setTitle("Project Monitoring");
	        xAxis.setLabel("Status");       
	        yAxis.setLabel("No. of Reqs");
	 
	        XYChart.Series series1 = new XYChart.Series();
	        series1.setName("reqs");       
	        getNumber();
	        series1.getData().add(new XYChart.Data(s, start));
	        series1.getData().add(new XYChart.Data(ip, prog));
	        series1.getData().add(new XYChart.Data(c, comp));
	        
	        Scene scene  = new Scene(bc,800,600);
	        bc.getData().add(series1);
	        stage.setScene(scene);
	        stage.show();
	    }
	 
	    public static void getNumber() {
	    	HashMap<Integer,String> s = MonitorController.status;
	    	for (int i : s.keySet()) {
	    		if (s.get(i).equals("to be started"))
	    			start++;
	    		else if (s.get(i).equals("in progress"))
	    			prog++;
	    		else
	    			comp++;
	    			
	    	}
	    }
}
