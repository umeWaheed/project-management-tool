package application;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ReqController extends startController{
	@FXML
	Button wbs,wTitle;
	static HashMap<String,String> reqs = new HashMap<String,String>();
	static int col = 0;
	static int row = 2;
	
	@FXML
	public void addRow() {
		GridPane gp = getGrid();
		TextArea r = new TextArea();
		r.setId("r"+row);
		TextArea r1 = new TextArea();
		r1.setId("w"+row);
		gp.add(r, col++, row);
		gp.add(r1, col++, row);
		TextField tf = new TextField();
		tf.setId("r"+row+"1");
		GridPane gp1 = new GridPane();
		gp1.setVgap(5);
		gp1.add(tf, 0, 0);
		tf = new TextField();
		tf.setId("r"+row+"2");
		gp1.add(tf, 0, 1);
		tf = new TextField();
		tf.setId("r"+row+"3");
		gp1.add(tf, 0, 2);
		gp.add(gp1, col++, row);
		TextArea ta = new TextArea();
		ta.setId("m"+row);
		gp.add(ta, col++, row);
		GridPane gp2 = new GridPane();
		gp2.setVgap(15);
		DatePicker dp = new DatePicker();
		dp.setId("s"+row);
		dp.setPromptText("start");
		gp2.add(dp, 0, 0);
		DatePicker dp1 = new DatePicker();
		dp1.setId("e"+row);
		dp1.setPromptText("end");
		gp2.add(dp1, 0, 1);
		gp.add(gp2,col++,row);
		row++;
		col=0;
	}
	
	@FXML
	public void addContents(ActionEvent event) {
		File f = new File(folder+"//reqs.txt");
		try {
			PrintWriter pw = new PrintWriter(f);
			GridPane gp = getGrid();
			for (Node n : gp.getChildren())	{
				print(n,pw);
				}
		pw.close();
		printFile(f,event);
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void print(Node n, PrintWriter pw) {
		if (n instanceof TextField) {
			reqs.put(n.getId(), ((TextField)n).getText());	
			pw.println(n.getId()+"#"+((TextField)n).getText());
			System.out.println(n.getId()+" : "+reqs.get(n.getId()));
		}
		if (n instanceof TextArea) {
			reqs.put(n.getId(), ((TextArea)n).getText());
			pw.println(n.getId()+"#"+((TextArea)n).getText());
			System.out.println(n.getId()+" : "+reqs.get(n.getId()));
		}
		if (n instanceof Pane) {
			for (Node n1 : ((Pane)n).getChildren()) {
				print(n1,pw);
			}
		}
		if (n instanceof DatePicker && ((DatePicker) n).getValue()!=null) {
			reqs.put(n.getId(), ((DatePicker)n).getValue().toString());
			pw.println(n.getId()+"#"+((DatePicker)n).getValue().toString());
			System.out.println(n.getId()+" : "+reqs.get(n.getId()));
		}
	}
	
	@FXML
	public void generate(ActionEvent e) {
		GridPane gp = getGrid();
		gp.setHgap(10);
		gp.add(new TextField(folder), 2, 0);
		int c;
		int srow,scol;
		//System.out.println(reqs.keySet());
		for (String key : reqs.keySet()) {
			//System.out.println("here");
			if (key.startsWith("r") && key.length()==2) {//main requirement	e.g r2
				c = Integer.parseInt(key.charAt(1)+"")-1;
				if (reqs.get(key)!=null)
					gp.add(new TextField(reqs.get(key)), c, 1);
			}
			if (key.startsWith("r") && key.length()==3) {//sub req
				srow = Integer.parseInt(key.charAt(2)+"")+1;
				scol = Integer.parseInt(key.charAt(1)+"")-1;
				gp.add(new Label(reqs.get(key)), scol, srow);
			}
		}
	}
}