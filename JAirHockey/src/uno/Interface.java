package uno;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Interface extends JPanel{

	  public Interface() {

	    super(true);
	    this.setLayout(new BorderLayout());
	   // setSize(new Dimension(300, 400));
	    JSlider slider = new JSlider(JSlider.HORIZONTAL, -1024, 1024, -1024);

	    slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				MotorController.setSpeed(slider.getValue() == 0 ? 0 : slider.getValue() < 0 ? -1024 : 1024);
				System.out.println("Slider: " + slider.getValue());
			}
		});
	    
	 
	    slider.setMajorTickSpacing(512);
	    slider.setPaintTicks(true);

	    
	    slider.setLabelTable(slider.createStandardLabels(10));
	    
	    //BUTTON
	    JButton StopButton = new JButton("STOP");
		  StopButton.addActionListener(new ActionListener(){			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MotorController.setSpeed(0);
				slider.setValue(0);
			}
		});
		  
		  
		 
		  
	    
		  
		add(StopButton, BorderLayout.NORTH);
		StopButton.setSize(100, 40);
	    add(slider, BorderLayout.CENTER);
	  }

	  
	  public void SliderBar() {
	    JFrame frame = new JFrame("Controll");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setContentPane(new Interface());
	    frame.setSize(800, 300);
	    frame.setVisible(true);
	    
	    
	  }
	
}
