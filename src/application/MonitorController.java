package application;

import java.awt.Desktop;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

import javax.swing.JFileChooser;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MonitorController extends startController{
	static HashMap<String,String> reqs = ReqController.reqs;
	static HashMap<Integer,String> start = new HashMap<Integer,String>();
	static HashMap<Integer,String> end = new HashMap<Integer,String>();
	static HashMap<Integer,String> status = new HashMap<Integer,String>();
	static int col=0;
	static GridPane gp;
	
	@FXML
	public void monitor() {
		gp = getGrid();
		Button b;
		for (String s : reqs.keySet()) {
			int row = 0;
			if (s.startsWith("r") && s.length()==2) {
				row = Integer.parseInt(s.substring(1));
				System.out.println(gp.getId()+row+reqs.get(s));
				gp.add(new TextField(reqs.get(s)), 0, row);
				b = new Button("select file");
				b.setId("select"+row);
				b.setOnAction(new EventHandler<ActionEvent>() {
		            @Override
		            public void handle(ActionEvent event) {
		                selectFile(event);
		            }
		        });
				gp.add(b, 4, row);
			}
			if (s.startsWith("s")) {
				row = Integer.parseInt(s.substring(1));
				start.put(row,reqs.get(s));
				gp.add(new TextField(reqs.get(s)), 1, row);
			}
			if (s.startsWith("e")) {
				row = Integer.parseInt(s.substring(1));
				end.put(row,reqs.get(s)); // add end date to location of row
				gp.add(new TextField(reqs.get(s)), 2, row);
			}
		}
		updateStatus();
	}
	
	public void updateStatus() {
		for (int i : start.keySet()) {
			LocalDate s = LocalDate.parse(start.get(i));
			LocalDate e = LocalDate.parse(end.get(i));
			LocalDate current  = LocalDate.now();
			
			if (s.compareTo(current)>0) {//to be started
					gp.add(new TextField("to be started"), 3, i);	
					status.put(i, "to be started");
			}
			else if (s.compareTo(current)<0 && e.compareTo(current)>0) {
				gp.add(new TextField("in progress"), 3, i);
				status.put(i, "in progress");
			}
			else  if (e.compareTo(current)<0) {
				gp.add(new TextField("completed"), 3, i);	
				status.put(i, "completed");
			}
			else
				gp.add(new TextField(""), 3, i);	
		}
	}
	
	@FXML
	public void selectFile(ActionEvent e) {
		TextField tf;
		int row = Integer.parseInt(((Button)(e.getSource())).getId().charAt(6)+""); //get row of button
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			tf = new TextField(file.getName());
			tf.setEditable(false);
			tf.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					try {
					Desktop.getDesktop().open(file);
					}catch(IOException ex) {
						System.out.println(ex.getMessage());
					}
				}
			});
			gp.add(tf, 4, row);
		}
	}
	
	@FXML
	public void drawChart() {
		ChartGenerator.start(new Stage());
	}
}
