package comprosoft.walls.editor.custom_controls;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class ComboBoxRenderer extends JPanel implements ListCellRenderer<Object>
{

    private static final long serialVersionUID = -1L;
    private Color[] colors;
    private String[] strings;
    private boolean[] disabled;
    
    JPanel textPanel;
    JLabel text;

    public ComboBoxRenderer(JComboBox<?> combo, Color[] col) {

        textPanel = new JPanel();
        textPanel.add(this);
        text = new JLabel();
        text.setOpaque(true);
        text.setFont(combo.getFont());
        textPanel.add(text);
        
        //Also parse out strings
        @SuppressWarnings("unchecked")
		ComboBoxModel<String> m = (ComboBoxModel<String>) combo.getModel();
        String[] strArr = new String[m.getSize()];
        for (int i = 0; i < m.getSize(); i++) {
        	strArr[i] = m.getElementAt(i);
        }
        
        this.setStrings(strArr);
        this.setColors(col);
        this.disabled = new boolean[strArr.length];
    }

    public void setColors(Color[] col)
    {
        colors = col;
    }

    public void setStrings(String[] str)
    {
        strings = str;
    }

    public Color[] getColors()
    {
        return colors;
    }

    public String[] getStrings()
    {
        return strings;
    }

    
    public void enableAll() {
    	for (int i = 0; i < this.disabled.length; i++) {
    		disabled[i] = false;
    	}
    }
    
    public void disableAll() {
    	for (int i = 0; i < this.disabled.length; i++) {
    		disabled[i] = true;
    	}
    }
    
    public void setEnabledState(int index, boolean state) {
    	this.disabled[index] = !state;
    }
    
    public boolean getEnabledState(int index) {
    	return !this.disabled[index];
    }
    
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        if (isSelected)
        {
            setBackground(list.getSelectionBackground());
        }
        else
        {
            //setBackground(new Color(64,64,64));
        	if (index != -1 && this.disabled[index]) {
            	setBackground(new Color(192,192,192));
        	} else {
            	setBackground(Color.white);
        	}
        }

        if (colors.length != strings.length)
        {
            System.out.println("colors.length does not equal strings.length");
            return this;
        }
        else if (colors == null)
        {
            System.out.println("use setColors first.");
            return this;
        }
        else if (strings == null)
        {
            System.out.println("use setStrings first.");
            return this;
        }

        text.setBackground(getBackground());

        text.setText(value.toString());
        if (index>-1) {
            if (this.disabled[index]) {
            	text.setForeground(
            			new Color(colors[index].getRed(), colors[index].getGreen(), colors[index].getBlue(),
            			128)
            	);
            } else {
            	text.setForeground(colors[index]);
            }
        }
        return text;
    }
}