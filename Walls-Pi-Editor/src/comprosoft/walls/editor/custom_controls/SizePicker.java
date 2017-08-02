package comprosoft.walls.editor.custom_controls;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class SizePicker extends JPanel {

	private static final long serialVersionUID = -3566392003672868935L;
	
	private JSpinner spinner_Length;
	private JSpinner spinner_Width;
	private JSpinner spinner_Height;

	/**
	 * Create the panel.
	 */
	public SizePicker() {
		setLayout(new BorderLayout(0, 0));
		
		Box verticalBox = Box.createVerticalBox();
		add(verticalBox, BorderLayout.CENTER);
		
		Box horizontalBox_Length = Box.createHorizontalBox();
		horizontalBox_Length.setAlignmentX(Component.LEFT_ALIGNMENT);
		horizontalBox_Length.setAlignmentY(Component.CENTER_ALIGNMENT);
		verticalBox.add(horizontalBox_Length);
		
		JLabel lbl_Length = new JLabel(" Length (X):");
		horizontalBox_Length.add(lbl_Length);
		
		Component horizontalGlue_Length = Box.createHorizontalGlue();
		horizontalBox_Length.add(horizontalGlue_Length);
		
		spinner_Length = new JSpinner();
		spinner_Length.setModel(new SpinnerNumberModel(1,1,100,1));
		spinner_Length.setValue(new Integer(3));
		spinner_Length.setAlignmentX(Component.RIGHT_ALIGNMENT);
		horizontalBox_Length.add(spinner_Length);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut);
		
		Box horizontalBox_Width = Box.createHorizontalBox();
		horizontalBox_Width.setAlignmentY(Component.CENTER_ALIGNMENT);
		horizontalBox_Width.setAlignmentX(Component.LEFT_ALIGNMENT);
		verticalBox.add(horizontalBox_Width);
		
		JLabel lbl_Width = new JLabel(" Width (Y):  ");
		horizontalBox_Width.add(lbl_Width);
		
		Component horizontalGlue_Width = Box.createHorizontalGlue();
		horizontalBox_Width.add(horizontalGlue_Width);
		
		spinner_Width = new JSpinner();
		spinner_Width.setModel(new SpinnerNumberModel(1,1,100,1));
		spinner_Width.setValue(new Integer(3));
		spinner_Width.setAlignmentX(Component.RIGHT_ALIGNMENT);
		horizontalBox_Width.add(spinner_Width);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut_1);
		
		Box horizontalBox_Height = Box.createHorizontalBox();
		horizontalBox_Height.setAlignmentX(Component.LEFT_ALIGNMENT);
		verticalBox.add(horizontalBox_Height);
		
		JLabel lbl_Height = new JLabel(" Height (Z):");
		horizontalBox_Height.add(lbl_Height);
		
		Component horizontalGlue_Height = Box.createHorizontalGlue();
		horizontalBox_Height.add(horizontalGlue_Height);
		
		spinner_Height = new JSpinner();
		spinner_Height.setModel(new SpinnerNumberModel(1,1,100,1));
		spinner_Height.setValue(new Integer(3));
		spinner_Height.setAlignmentX(Component.RIGHT_ALIGNMENT);
		horizontalBox_Height.add(spinner_Height);

	}

	/**
	 * Get the length chosen
	 */
	public int getChosenLength() {
		return (Integer) spinner_Length.getValue();
	}
	
	
	/**
	 * Set the initial length
	 * 
	 * @param length Initial length
	 */
	public void setChosenLength(int length) {
		this.spinner_Length.setValue(new Integer(length));
	}
	
	
	/**
	 * Get the width chosen
	 */
	public int getChosenWidth() {
		return (Integer) spinner_Width.getValue();
	}
	
	
	/**
	 * Set the initial width
	 * 
	 * @param width Initial width
	 */
	public void setChosenWidth(int width) {
		this.spinner_Width.setValue(new Integer(width));
	}
	
	
	
	/**
	 * Get the height chosen
	 */
	public int getChosenHeight() {
		return (Integer) spinner_Height.getValue();
	}

	
	/**
	 * Set the initial height
	 * 
	 * @param height Initial height
	 */
	public void setChosenHeight(int height) {
		this.spinner_Height.setValue(new Integer(height));
	}
	
	
	
}
