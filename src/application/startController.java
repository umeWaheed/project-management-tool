package application;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;

import javax.swing.JFileChooser;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class startController {
	static String folder;
	static File file;
	HashMap<String,String> pageOne = new HashMap<String,String>();
	@FXML
	Button resume,next,back,submit,add,next1,addReq,next2,next3,next4;
	@FXML
	TextField name,sponsor,manager,time,quality,budget,scope,duration;
	@FXML
	TextArea description,scope1,charter;
	@FXML
	DatePicker start,end;
	@FXML
	CheckBox confirm;
	static BorderPane root;

	@FXML
	public void resumeProject(ActionEvent event) {
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			folder = file.getParent().substring(file.getParent().lastIndexOf('\\')+1);
			printFile(file,event);
		}
	}
	
	public void printFile(File f, ActionEvent event) {
		try {
			switchPage(event);
			int r=1,c=0;
	        BufferedReader br = new BufferedReader(new FileReader(f));
	        String line = br.readLine();
	        GridPane table = new GridPane();
	        table.add(new TextField("sr#"),0,0);
	        table.add(new TextField("title"),1,0);
	        table.add(new TextField("description"),2,0);
	        table.setHgap(10);
	        table.setVgap(10);
	        table.setPadding(new Insets(10,10,10,10));
	        while (line!=null) {
	        	String[] sp = line.split("[#]");
	        	if(sp.length>1) {
	        	table.add(new Label(""+r+"-"), c++, r);
	        	table.add(new Label(sp[0]), c++, r);
	        	table.add(new Label(sp[1]), c++, r);
	        	r++;
	        	c=0;
	        	}
	        	line = br.readLine();
				}
	        ScrollPane sp = new ScrollPane();
	        sp.setContent(table);
	        root.setCenter(sp);
	        br.close();
		}catch(IOException e) {
			System.out.println(e.getMessage());
			}
	}
	
	@FXML
	public void checkInputText(ActionEvent e) {
		if (e.getSource() instanceof TextField) {
		TextField tf = ((TextField)(e.getSource()));
		tf.focusedProperty().addListener((arg0,oldvalue,newvalue)->{
			if (!newvalue) { // when focus lost
                if (tf.getText().matches("[a-zA-Z ]*")) {
                    tf.setStyle("-fx-border-color:green");
                }
                else {
                	tf.setStyle("-fx-border-color:red");
                }
            }
        });	
		}
		else if (e.getSource() instanceof DatePicker) {
			LocalDate eDate = end.getValue();
			LocalDate sDate = start.getValue();
			
			if (eDate.compareTo(sDate)<0){
				end.setStyle("-fx-border-color:red");
			}
			else {
				start.setStyle("-fx-border-color:green");
				end.setStyle("-fx-border-color:green");
				Period dura = Period.between(sDate, eDate);
				
				duration.setText(dura.getDays()+(dura.getMonths()*30)+(dura.getYears()*365)+" days");
			}
			
		}
	}
	
	public GridPane getGrid() {
		Pane p = (Pane)root.getCenter();
		Pane p1 = (Pane)p.getChildren().get(0);
		for (Node node : p1.getChildren()) {//tabpane and simple pane 
			if (node instanceof TabPane) {
				TabPane tp = (TabPane)node;
				Tab t = tp.getTabs().get(0);
				AnchorPane ap = (AnchorPane)t.getContent();
				ScrollPane sp = (ScrollPane)ap.getChildren().get(0);
				GridPane gp = (GridPane)sp.getContent();
				return gp;
			}
		}
		return null;
		}
	
	@FXML
	public void addContents(ActionEvent event) {
		File f=null;
		boolean proceed = true;
			if (event.getSource()==submit) {
				if (!confirm.isSelected()) {
					confirm.setStyle("-fx-background-color:red");
					proceed = false;
				}
				else {
					folder = name.getText();
					File dir = new File(name.getText());
					dir.mkdir();
					f = new File(folder+"//charter.txt");
					proceed = true;
				}
			}
			
		if (proceed) {	
		Pane p = (Pane)root.getCenter();
		Pane p1 = (Pane)p.getChildren().get(0);
		try {
			PrintWriter pw = new PrintWriter(f);
		for (Node node : p1.getChildren()) {//tabpane and simple pane 
			if (node instanceof TabPane) {
				TabPane tp = (TabPane)node;
				ObservableList<Tab> tabs = tp.getTabs();
				for (Tab inner : tabs) {
					AnchorPane ap = (AnchorPane)inner.getContent();
					Pane gp = (Pane)ap.getChildren().get(0);
					for (Node n : gp.getChildren())	{
						print(n,pw);
					}
				}
			}
		}
		pw.close();
		printFile(f,event);
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
		}
	}
			
	public void print (Node n, PrintWriter pw) {
		if (n instanceof TextField) {
			pw.println(n.getId()+"#"+((TextField)n).getText());
		}
		if (n instanceof TextArea) {
			pw.println(n.getId()+"#"+((TextArea)n).getText());
		}
		if (n instanceof Pane) {
			for (Node n1 : ((Pane)n).getChildren()) {
				print(n1,pw);
			}
		}
		if (n instanceof DatePicker && ((DatePicker) n).getValue()!=null) {
			pw.println(n.getId()+"#"+((DatePicker)n).getValue().toString());
		}
	}
	
	@FXML
	public void switchPage(ActionEvent event) {
		String fxml=null,title=null;
		if (event.getSource() == next) {
			fxml = "req.fxml";
			title = "Requirements";
		}
		else if (event.getSource() == back) {
			fxml = "startpage.fxml";
			title = "Welcome Page";
		}
		else if (event.getSource() == resume) {
			fxml = "showCharter.fxml";
			title = "Resume Project";
		}
		else if (event.getSource() == add) {
			fxml = "AddNewProject.fxml";
			title = "Start New Project";
		}
		else if (event.getSource()==next1) {
			fxml = "showReq.fxml";
			title = "Requirements";
		}
		else if (event.getSource()==next2) {
			fxml = "wbs.fxml";
			title = "Work Break Down Structure";
		}
		else if (event.getSource()==next3) {
			fxml = "monitor.fxml";
			title = "Monitoring";
		}
		else if (event.getSource()==next4) {
			fxml = "closing.fxml";
			title = "Closing";
		}
		else {//submit
			fxml = "showCharter.fxml";
			title = "Project Charter";
		}
		try {
			root = (BorderPane)FXMLLoader.load(getClass().getResource(fxml));
	        Stage stage = new Stage();
	        stage.setTitle(title);
	        stage.setScene(new Scene(root, 625, 600));
	        stage.show();
	        // Hide this current window (if this is what you want)
	        ((Node)(event.getSource())).getScene().getWindow().hide();
			}
	    catch (IOException e) {
	        e.printStackTrace();
	    	}
	}
	
}
