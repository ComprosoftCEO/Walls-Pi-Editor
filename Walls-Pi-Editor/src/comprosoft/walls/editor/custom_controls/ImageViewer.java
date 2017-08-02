package comprosoft.walls.editor.custom_controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ImageViewer extends JPanel
                        implements ItemListener {

	private static final long serialVersionUID = 1066474461507550504L;
    private ScrollablePicture picture;

    public ImageViewer(Image img) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
                        
        //Get the image to use.
        this.picture = new ScrollablePicture(new ImageIcon(img), 5);
        
        
                //Set up the scroll pane.
                JScrollPane pictureScrollPane = new JScrollPane(picture);
                add(pictureScrollPane);
                pictureScrollPane.setPreferredSize(new Dimension(300, 250));
                pictureScrollPane.setViewportBorder(
                        BorderFactory.createLineBorder(Color.black));
    }


    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ImageViewer.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }


	@Override
	public void itemStateChanged(ItemEvent e) {
		//Does nothing		
	}
	
	
	
	/**
	 * Create and show the dialog for the map
	 * 
	 * @param img Image to show
	 */
	public static void buildAndShow(Image img) {
		
        //Create and set up the window.
        JFrame frame = new JFrame("Walls Map Viewer");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        //Create and set up the content pane.
        JComponent newContentPane = new ImageViewer(img);
        newContentPane.setOpaque(true); //content panes must be opaque  
        frame.setContentPane(newContentPane);
		
		
        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}

}