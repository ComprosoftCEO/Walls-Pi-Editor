package comprosoft.walls.editor.custom_controls;

import java.awt.Dimension;

import javax.swing.JComboBox; 

// got this workaround from the following bug: 
//	      http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4618607 
public class WideComboBox<E> extends JComboBox<E>{ 

	private static final long serialVersionUID = -7002867176847891807L;

	public WideComboBox() { } 
	
	public WideComboBox(final E items[]){ 
	    super(items); 
	} 



    private boolean layingOut = false; 

    public void doLayout(){ 
        try{ 
            layingOut = true; 
                super.doLayout(); 
        }finally{ 
            layingOut = false; 
        } 
    } 

    public Dimension getSize(){ 
        Dimension dim = super.getSize(); 
        if(!layingOut) 
            dim.width = Math.max(dim.width, getPreferredSize().width); 
        return dim; 
    } 
}

