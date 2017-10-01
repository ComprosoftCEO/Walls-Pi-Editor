package comprosoft.walls.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import comprosoft.walls.dungeon.Dungeon;
import comprosoft.walls.dungeon.Room;
import comprosoft.walls.editor.custom_controls.ComboBoxRenderer;
import comprosoft.walls.editor.custom_controls.ImageViewer;
import comprosoft.walls.editor.custom_controls.PanelDisable;
import comprosoft.walls.editor.custom_controls.SizePicker;
import comprosoft.walls.editor.custom_controls.WideComboBox;
import comprosoft.walls.enums.Direction;
import comprosoft.walls.enums.DoorType;
import comprosoft.walls.enums.ItemType;
import comprosoft.walls.enums.LiquidType;
import comprosoft.walls.enums.WallType;

public class MainWindow {

	private JFrame frmWallsPiEditor;
	private JTextField textbox_X;
	private JTextField textbox_Y;
	private JTextField textbox_Z;

	public static Color wallColor = new Color(128,255,0);
	public static Color backColor = new Color(192,192,192);
	
	//Move around the dungeon
	private JButton but_RoomUp;
	private JButton but_RoomDown;
	private JButton but_RoomLeft;
	private JButton but_RoomRight;
	private JButton but_LevelUp;
	private JButton but_LevelDown;
	
	//Modify the room itself
	private JButton but_TopWall;
	private JButton but_BottomWall;
	private JButton but_LeftWall;
	private JButton but_RightWall;
	private int TopWallIndex;
	private int BottomWallIndex;
	private int LeftWallIndex;
	private int RightWallIndex;
	private JComboBox<String> combo_TopDoor;
	private JComboBox<String> combo_TopLiquid;
	private JComboBox<String> combo_BottomDoor;
	private JComboBox<String> combo_BottomLiquid;
	private JComboBox<String> combo_LeftDoor;
	private JComboBox<String> combo_LeftLiquid;
	private JComboBox<String> combo_RightDoor;
	private JComboBox<String> combo_RightLiquid;
	private JComboBox<String> combo_RoomItem;
	private JComboBox<String> combo_RoomLiquid;
	
	//Allows us to enable or disable stairs
	private ComboBoxRenderer item_rend;
	private final int STAIR_UP = 7;
	private final int STAIR_DOWN = 8;
	private final int STAIR_UP_DOWN = 9;
	
	//Used for disable and re-enable panel
	private PanelDisable panelDisabler;
	
	
	//Variables for dungeon control
	private Dungeon dungeon;
	private int roomX = 0;
	private int roomY = 0;
	private int roomZ = 0;
	private JMenuItem item_ResizeDungeon;
	private JPanel panel_AllControls;
	private JMenu mnu_Edit;
	private JMenuItem item_NewColor;
	private JMenuItem item_ClearRoom;
	private JMenuItem item_About;
	private JMenuItem item_SaveFile;
	private JMenuItem item_Export;
	private JMenuItem item_MapImage;
	
	private JFileChooser fc;
	private JCheckBox check_RoomUsed;
	private JButton but_StartRoom;
	private JMenuItem item_ClearDungeon;
	private JMenuItem item_SaveMaps;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmWallsPiEditor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		
		//Disable all items when it first loads
		panelDisabler.disable();
		
		//Set up file chooser
		 fc = new JFileChooser() {

			private static final long serialVersionUID = 6072028571688228988L;

			@Override
		    public void approveSelection(){
		        File f = getSelectedFile();
		        
		        if (this.getFileFilter().accept(f)) {
		        if(f.exists() && getDialogType() == SAVE_DIALOG){
		            int result = JOptionPane.showConfirmDialog(this,"The file exists, overwrite?","Existing file",JOptionPane.YES_NO_CANCEL_OPTION);
		            switch(result){
		                case JOptionPane.YES_OPTION:
		                    super.approveSelection();
		                    return;
		                case JOptionPane.NO_OPTION:
		                    return;
		                case JOptionPane.CLOSED_OPTION:
		                    return;
		                case JOptionPane.CANCEL_OPTION:
		                    cancelSelection();
		                    return;
		            }
		        }
		        super.approveSelection();
		        } else {
		        	
		        	//Notify that this is an invalid file
		        	int result = JOptionPane.showConfirmDialog(this, "Output must be a " + this.getFileFilter().getDescription() + "!","Invalid File",JOptionPane.OK_CANCEL_OPTION,JOptionPane.ERROR_MESSAGE);
		        	if (result == JOptionPane.CANCEL_OPTION) {
		        		cancelSelection();
		        		return;
		        	}
		        }
		    }        
		};
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	}

	

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWallsPiEditor = new JFrame();
		frmWallsPiEditor.setResizable(false);
		frmWallsPiEditor.setTitle("Walls Pi Editor");
		frmWallsPiEditor.setBounds(100, 100, 542, 430);
		frmWallsPiEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWallsPiEditor.getContentPane().setLayout(new BorderLayout(0, 0));
		frmWallsPiEditor.addKeyListener(new KeyEvents());
		
		JMenuBar menuBar = new JMenuBar();
		frmWallsPiEditor.getContentPane().add(menuBar, BorderLayout.NORTH);
		
		JMenu mnu_File = new JMenu("File") {

			private static final long serialVersionUID = -5511446516396712941L;

			//Fix a small graphical glitch
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				panel_AllControls.repaint();
				combo_LeftDoor.repaint();
			}
			
		};
		menuBar.add(mnu_File);
		
		JMenuItem item_NewDungeon = new JMenuItem("New Dungeon");
		item_NewDungeon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newDungeon();
			}
		});
		item_NewDungeon.addMouseListener(new MouseAdapter() {

		});
		mnu_File.add(item_NewDungeon);
		
		item_ResizeDungeon = new JMenuItem("Resize Dungeon");
		item_ResizeDungeon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resizeDungeon();
			}
		});
		item_ResizeDungeon.setEnabled(false);
		mnu_File.add(item_ResizeDungeon);
		
		JSeparator separator = new JSeparator();
		mnu_File.add(separator);
		
		JMenuItem item_OpenFile = new JMenuItem("Open Dungeon");
		item_OpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openDungeon();
			}
		});
		mnu_File.add(item_OpenFile);
		
		item_SaveFile = new JMenuItem("Save Dungeon");
		item_SaveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveDungeon();
			}
		});
		item_SaveFile.setEnabled(false);
		mnu_File.add(item_SaveFile);
		
		item_Export = new JMenuItem("Export to Python");
		item_Export.setEnabled(false);
		item_Export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportPython();
			}
		});
		mnu_File.add(item_Export);
		
		JSeparator separator_1 = new JSeparator();
		mnu_File.add(separator_1);
		
		item_MapImage = new JMenuItem("View Map");
		item_MapImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveToMemory();
				ImageViewer.buildAndShow(dungeon.buildImage(roomZ));
			}
		});
		item_MapImage.setEnabled(false);
		mnu_File.add(item_MapImage);
		
		item_SaveMaps = new JMenuItem("Save Maps");
		item_SaveMaps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportMapImages();
			}
		});
		item_SaveMaps.setEnabled(false);
		mnu_File.add(item_SaveMaps);
		
		mnu_Edit = new JMenu("Edit");
		menuBar.add(mnu_Edit);
		
		item_ClearRoom = new JMenuItem("Clear Room");
		item_ClearRoom.setEnabled(false);
		item_ClearRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dungeon.clearRoom(roomX, roomY, roomZ);
				loadFromMemory();
			}
		});
		mnu_Edit.add(item_ClearRoom);
		
		item_ClearDungeon = new JMenuItem("Clear Dungeon");
		item_ClearDungeon.setEnabled(false);
		item_ClearDungeon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null,"Clear the whole dungeon?","Clear Dungeon",JOptionPane.YES_NO_CANCEL_OPTION);
			
				if (result == JOptionPane.YES_OPTION) {
					dungeon.clearDungeon();
					loadFromMemory();
				}

			}
		});
		mnu_Edit.add(item_ClearDungeon);
		
		JSeparator separator_2 = new JSeparator();
		mnu_Edit.add(separator_2);
		
		item_NewColor = new JMenuItem("New Wall Color");
		item_NewColor.setEnabled(false);
		item_NewColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCurrentRoom().newWallColor();
				loadFromMemory();
			}
		});
		mnu_Edit.add(item_NewColor);
		
		JMenu mnu_Help = new JMenu("Help");
		menuBar.add(mnu_Help);
		
		item_About = new JMenuItem("About");
		item_About.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(null, "Walls for Raspberry Pi\n"
						+ "							   Dungeon Editor\n\n"
						+ "							Created by Bryan McClain\n"
						+ "							(C) Comprosoft 2017", "About:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnu_Help.add(item_About);
		
		panel_AllControls = new JPanel() {
			
			private static final long serialVersionUID = -5098447685308080456L;

			@Override
			  public void paintComponent(Graphics g) {
				paintBackground(g);
		    }
		};
		panel_AllControls.setFocusable(false);
		panel_AllControls.setOpaque(false);
		panel_AllControls.setEnabled(false);
		frmWallsPiEditor.getContentPane().add(panel_AllControls, BorderLayout.CENTER);
		panel_AllControls.setLayout(null);
		
		panelDisabler = new PanelDisable(panel_AllControls, null);
		
		but_RoomUp = new JButton("⏶");
		but_RoomUp.setFocusable(false);
		but_RoomUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveToMemory();
				roomY-=1;
				loadFromMemory();
			}
		});
		but_RoomUp.setMargin(new Insets(2, 2, 2, 2));
		but_RoomUp.setFont(new Font("Dialog", Font.PLAIN, 18));
		but_RoomUp.setBounds(448, 190, 32, 32);
		panel_AllControls.add(but_RoomUp);
		
		but_RoomDown = new JButton("⏷");
		but_RoomDown.setFocusable(false);
		but_RoomDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveToMemory();
				roomY+=1;
				loadFromMemory();
			}
		});
		but_RoomDown.setMargin(new Insets(2, 2, 2, 2));
		but_RoomDown.setFont(new Font("Dialog", Font.PLAIN, 18));
		but_RoomDown.setBounds(448, 276, 32, 32);
		panel_AllControls.add(but_RoomDown);
		
		but_RoomLeft = new JButton("⏴");
		but_RoomLeft.setFocusable(false);
		but_RoomLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveToMemory();
				roomX-=1;
				loadFromMemory();
			}
		});
		but_RoomLeft.setMargin(new Insets(2, 2, 2, 2));
		but_RoomLeft.setFont(new Font("Dialog", Font.PLAIN, 18));
		but_RoomLeft.setBounds(408, 234, 32, 32);
		panel_AllControls.add(but_RoomLeft);
		
		but_RoomRight = new JButton("⏵");
		but_RoomRight.setFocusable(false);
		but_RoomRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveToMemory();
				roomX+=1;
				loadFromMemory();
			}
		});
		but_RoomRight.setMargin(new Insets(2, 2, 2, 2));
		but_RoomRight.setFont(new Font("Dialog", Font.PLAIN, 18));
		but_RoomRight.setBounds(488, 234, 32, 32);
		panel_AllControls.add(but_RoomRight);
		
		but_LevelUp = new JButton("⇑");
		but_LevelUp.setFocusable(false);
		but_LevelUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveToMemory();
				roomZ+=1;
				loadFromMemory();
			}
		});
		but_LevelUp.setBackground(new Color(255, 105, 180));
		but_LevelUp.setMargin(new Insets(2, 2, 2, 2));
		but_LevelUp.setFont(new Font("Dialog", Font.PLAIN, 18));
		but_LevelUp.setBounds(408, 330, 32, 32);
		panel_AllControls.add(but_LevelUp);
		
		
		but_LevelDown = new JButton("⇓");
		but_LevelDown.setFocusable(false);
		but_LevelDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveToMemory();
				roomZ-=1;
				loadFromMemory();
			}
		});
		but_LevelDown.setBackground(new Color(176, 224, 230));
		but_LevelDown.setMargin(new Insets(2, 2, 2, 2));
		but_LevelDown.setFont(new Font("Dialog", Font.PLAIN, 18));
		but_LevelDown.setBounds(488, 330, 32, 32);
		panel_AllControls.add(but_LevelDown);
		
		JLabel lbl_RoomX = new JLabel("X Room:");
		lbl_RoomX.setFocusable(false);
		lbl_RoomX.setBounds(400, 36, 70, 15);
		panel_AllControls.add(lbl_RoomX);
		
		JLabel lbl_RoomY = new JLabel("Y Room:");
		lbl_RoomY.setFocusable(false);
		lbl_RoomY.setBounds(400, 68, 70, 15);
		panel_AllControls.add(lbl_RoomY);
		
		JLabel lbl_RoomLevel = new JLabel("Level:");
		lbl_RoomLevel.setFocusable(false);
		lbl_RoomLevel.setBounds(400, 102, 70, 15);
		panel_AllControls.add(lbl_RoomLevel);
		
		textbox_X = new JTextField();
		textbox_X.setFocusable(false);
		textbox_X.setEditable(false);
		textbox_X.setBounds(478, 34, 32, 19);
		panel_AllControls.add(textbox_X);
		textbox_X.setColumns(10);
		
		textbox_Y = new JTextField();
		textbox_Y.setFocusable(false);
		textbox_Y.setEditable(false);
		textbox_Y.setColumns(10);
		textbox_Y.setBounds(478, 66, 32, 19);
		panel_AllControls.add(textbox_Y);
		
		textbox_Z = new JTextField();
		textbox_Z.setFocusable(false);
		textbox_Z.setEditable(false);
		textbox_Z.setColumns(10);
		textbox_Z.setBounds(478, 100, 32, 19);
		panel_AllControls.add(textbox_Z);
		
		but_TopWall = new JButton("") {

			private static final long serialVersionUID = 9138982711609022242L;

			@Override
	        protected void paintComponent(Graphics g) {
				paintWallButton(g,this,WallType.fromIndex(TopWallIndex));
	            super.paintComponent(g);
	        }
		};
		but_TopWall.setOpaque(false);
		but_TopWall.setBorderPainted(false);
		but_TopWall.setFocusPainted(false);
		but_TopWall.setFocusable(false);
		but_TopWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room r = getCurrentRoom();
				r.setWall(WallType.getNext(r.getWall(Direction.TOP), r.getLockedWall(Direction.TOP)), Direction.TOP);
				
				but_TopWall.setBackground(r.getWall(Direction.TOP).getColor());
				but_TopWall.setText(r.getWall(Direction.TOP).getName(false));
				TopWallIndex = r.getWall(Direction.TOP).getIndex();
			
				panel_AllControls.repaint();
				combo_TopDoor.repaint();
			}
		});
		but_TopWall.setMargin(new Insets(2, 2, 2, 2));
		but_TopWall.setBounds(115, 12, 150, 25);
		but_TopWall.setBackground(new Color(0,0,0,0));
		panel_AllControls.add(but_TopWall);
		
		
		but_BottomWall = new JButton("") {

			private static final long serialVersionUID = -965076877261803265L;

			@Override
	        protected void paintComponent(Graphics g) {
				paintWallButton(g,this,WallType.fromIndex(BottomWallIndex));
	            super.paintComponent(g);
	        }
		};
		but_BottomWall.setOpaque(false);
		but_BottomWall.setBorderPainted(false);
		but_BottomWall.setFocusPainted(false);
		but_BottomWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room r = getCurrentRoom();
				r.setWall(WallType.getNext(r.getWall(Direction.BOTTOM), r.getLockedWall(Direction.BOTTOM)), Direction.BOTTOM);
				
				but_BottomWall.setBackground(r.getWall(Direction.BOTTOM).getColor());
				but_BottomWall.setText(r.getWall(Direction.BOTTOM).getName(false));
				BottomWallIndex = r.getWall(Direction.BOTTOM).getIndex();
				
				panel_AllControls.repaint();
				combo_BottomDoor.repaint();
			}
		});
		but_BottomWall.setFocusable(false);
		but_BottomWall.setMargin(new Insets(2, 2, 2, 2));
		but_BottomWall.setBounds(115, 342, 150, 25);
		but_BottomWall.setBackground(new Color(0,0,0,0));
		panel_AllControls.add(but_BottomWall);
		
		but_LeftWall = new JButton(""){
	
			private static final long serialVersionUID = -6131167511808695765L;

			@Override
	        protected void paintComponent(Graphics g) {
				paintWallButton(g,this,WallType.fromIndex(LeftWallIndex));
	            super.paintComponent(g);
	        }
		};
		but_LeftWall.setOpaque(false);
		but_LeftWall.setBorderPainted(false);
		but_LeftWall.setFocusPainted(false);
		but_LeftWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room r = getCurrentRoom();
				r.setWall(WallType.getNext(r.getWall(Direction.LEFT), r.getLockedWall(Direction.LEFT)), Direction.LEFT);
				
				but_LeftWall.setBackground(r.getWall(Direction.LEFT).getColor());
				but_LeftWall.setText(r.getWall(Direction.LEFT).getName(true));
				LeftWallIndex = r.getWall(Direction.LEFT).getIndex();
				
				panel_AllControls.repaint();
				combo_LeftDoor.repaint();
			}
		});
		but_LeftWall.setFocusable(false);
		but_LeftWall.setMargin(new Insets(2, 2, 2, 2));
		but_LeftWall.setBounds(12, 115, 25, 150);
		but_LeftWall.setBackground(new Color(0,0,0,0));
		panel_AllControls.add(but_LeftWall);
		
		but_RightWall = new JButton(""){

			private static final long serialVersionUID = 1929924081316629423L;

			@Override
	        protected void paintComponent(Graphics g) {
				paintWallButton(g,this,WallType.fromIndex(RightWallIndex));
	            super.paintComponent(g);
	        }
		};
		but_RightWall.setOpaque(false);
		but_RightWall.setFocusPainted(false);
		but_RightWall.setBorderPainted(false);
		but_RightWall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Room r = getCurrentRoom();
				r.setWall(WallType.getNext(r.getWall(Direction.RIGHT), r.getLockedWall(Direction.RIGHT)), Direction.RIGHT);
				
				but_RightWall.setBackground(r.getWall(Direction.RIGHT).getColor());
				but_RightWall.setText(r.getWall(Direction.RIGHT).getName(true));
				RightWallIndex = r.getWall(Direction.RIGHT).getIndex();
				
				panel_AllControls.repaint();
				combo_RightDoor.repaint();
			}
		});
		but_RightWall.setFocusable(false);
		but_RightWall.setMargin(new Insets(2, 2, 2, 2));
		but_RightWall.setBounds(342, 115, 25, 150);
		but_RightWall.setBackground(new Color(0,0,0,0));
		panel_AllControls.add(but_RightWall);
		
		combo_TopDoor = new JComboBox<String>() {
			
			private static final long serialVersionUID = 7774813460339843962L;

			@Override
			protected void paintComponent(Graphics g) {
				paintDoorComboBox(g, this, false);
			}
		};
		combo_TopDoor.removeAll();
		combo_TopDoor.setFocusable(false);
		combo_TopDoor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCurrentRoom().setDoor(DoorType.fromIndex(combo_TopDoor.getSelectedIndex()), Direction.TOP);
				panel_AllControls.repaint();
			}
		});
		combo_TopDoor.setModel(new DefaultComboBoxModel<String>(DoorType.getStringsArray()));
		combo_TopDoor.setBounds(115, 40, 150, 25);
		panel_AllControls.add(combo_TopDoor);
		
		combo_TopLiquid = new JComboBox<String>() {

			private static final long serialVersionUID = 9223230080957454278L;

			@Override
			protected void paintComponent(Graphics g) {
				paintLiquidComboBox(g,this,false);
			}
			
		};
		combo_TopLiquid.setFocusable(false);
		combo_TopLiquid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCurrentRoom().setLiquid(LiquidType.fromIndex(combo_TopLiquid.getSelectedIndex()), Direction.TOP);
				panel_AllControls.repaint();
			}
		});
		combo_TopLiquid.setModel(new DefaultComboBoxModel<String>(LiquidType.getStringArray()));
		combo_TopLiquid.setBounds(115, 70, 150, 25);
		combo_TopLiquid.removeAll();
		panel_AllControls.add(combo_TopLiquid);
		
		combo_BottomDoor = new JComboBox<String>() {
			
			private static final long serialVersionUID = 9223230080957454278L;

			@Override
			protected void paintComponent(Graphics g) {
				paintDoorComboBox(g, this, false);
			}
		};
		combo_BottomDoor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCurrentRoom().setDoor(DoorType.fromIndex(combo_BottomDoor.getSelectedIndex()), Direction.BOTTOM);
				panel_AllControls.repaint();
			}
		});
		combo_BottomDoor.setFocusable(false);
		combo_BottomDoor.removeAll();
		combo_BottomDoor.setModel(new DefaultComboBoxModel<String>(DoorType.getStringsArray()));
		combo_BottomDoor.setBounds(115, 315, 150, 25);
		panel_AllControls.add(combo_BottomDoor);
		
		combo_BottomLiquid = new JComboBox<String>() {
			
			private static final long serialVersionUID = -6600362220265671303L;

			@Override
			protected void paintComponent(Graphics g) {
				paintLiquidComboBox(g,this,false);
			}
		};
		combo_BottomLiquid.setFocusable(false);
		combo_BottomLiquid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCurrentRoom().setLiquid(LiquidType.fromIndex(combo_BottomLiquid.getSelectedIndex()), Direction.BOTTOM);
				panel_AllControls.repaint();
			}
		});
		combo_BottomLiquid.setModel(new DefaultComboBoxModel<String>(LiquidType.getStringArray()));
		combo_BottomLiquid.setBounds(115, 285, 150, 25);
		combo_BottomLiquid.removeAll();
		panel_AllControls.add(combo_BottomLiquid);
		
		combo_LeftDoor = new WideComboBox<String>() {

			private static final long serialVersionUID = 9223230080957454278L;

			@Override
			protected void paintComponent(Graphics g) {
				paintDoorComboBox(g, this, true);
			}
		};
		combo_LeftDoor.setFocusable(false);
		combo_LeftDoor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCurrentRoom().setDoor(DoorType.fromIndex(combo_LeftDoor.getSelectedIndex()), Direction.LEFT);
				panel_AllControls.repaint();
			}
		});
		combo_LeftDoor.removeAll();
		combo_LeftDoor.setModel(new DefaultComboBoxModel<String>(DoorType.getStringsArray()));
		combo_LeftDoor.setBounds(40, 115, 25, 150);
		panel_AllControls.add(combo_LeftDoor);
		
		combo_LeftLiquid = new WideComboBox<String>() {
	
			private static final long serialVersionUID = 4481008756210511596L;

			@Override
			protected void paintComponent(Graphics g) {
				paintLiquidComboBox(g,this,true);
			}
		};
		combo_LeftLiquid.setFocusable(false);
		combo_LeftLiquid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCurrentRoom().setLiquid(LiquidType.fromIndex(combo_LeftLiquid.getSelectedIndex()), Direction.LEFT);
				panel_AllControls.repaint();
			}
		});
		combo_LeftLiquid.setModel(new DefaultComboBoxModel<String>(LiquidType.getStringArray()));
		combo_LeftLiquid.setBounds(70, 115, 25, 150);
		combo_LeftLiquid.removeAll();
		panel_AllControls.add(combo_LeftLiquid);
		
		combo_RightDoor = new WideComboBox<String>() {

			private static final long serialVersionUID = 4915174556576554683L;
			
			@Override
			protected void paintComponent(Graphics g) {
				paintDoorComboBox(g, this, true);
			}
		};
		combo_RightDoor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCurrentRoom().setDoor(DoorType.fromIndex(combo_RightDoor.getSelectedIndex()), Direction.RIGHT);
				panel_AllControls.repaint();
			}
		});
		combo_RightDoor.setFocusable(false);
		combo_RightDoor.removeAll();
		combo_RightDoor.setModel(new DefaultComboBoxModel<String>(DoorType.getStringsArray()));
		combo_RightDoor.setBounds(315, 115, 25, 150);
		panel_AllControls.add(combo_RightDoor);
		
		combo_RightLiquid = new WideComboBox<String>() {

			private static final long serialVersionUID = 2134798994439080754L;

			@Override
			protected void paintComponent(Graphics g) {
				paintLiquidComboBox(g,this,true);
			}
		};
		combo_RightLiquid.setFocusable(false);
		combo_RightLiquid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCurrentRoom().setLiquid(LiquidType.fromIndex(combo_RightLiquid.getSelectedIndex()), Direction.RIGHT);
				panel_AllControls.repaint();
			}
		});
		combo_RightLiquid.setModel(new DefaultComboBoxModel<String>(LiquidType.getStringArray()));
		combo_RightLiquid.setBounds(286, 115, 25, 150);
		combo_RightLiquid.removeAll();
		panel_AllControls.add(combo_RightLiquid);
		
		combo_RoomItem = new WideComboBox<String>() {

			private static final long serialVersionUID = 6561618088737110392L;

			@Override
			protected void paintComponent(Graphics g) {
				if (ItemType.fromIndex(getSelectedIndex()) == ItemType.NO_ITEM) {
					g.setColor(Color.white);
					g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
				}
				g.drawImage(ItemType.fromIndex(getSelectedIndex()).getImage(), 0, 0, getWidth(), getHeight(), null);
			}
			
		};
		combo_RoomItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ComboBoxRenderer rend = (ComboBoxRenderer) combo_RoomItem.getRenderer();
				if (!rend.getEnabledState(combo_RoomItem.getSelectedIndex())) {
					String item = (String) combo_RoomItem.getSelectedItem();
					
					combo_RoomItem.setSelectedIndex(0);
					combo_RoomItem.repaint();
					combo_RoomItem.hidePopup();
					JOptionPane.showMessageDialog(null, "Item \""+ item + "\" isn't allowed in this room.", "Item not Allowed!", JOptionPane.WARNING_MESSAGE);
				} else {
					getCurrentRoom().setRoomItem(ItemType.fromIndex(combo_RoomItem.getSelectedIndex()));
				}
			}
		});
		combo_RoomItem.setFocusable(false);
		combo_RoomItem.removeAll();
		combo_RoomItem.setModel(new DefaultComboBoxModel<String>(new String[] {"(Item)", "Red Key", "Yellow Key", "Green Key", "Blue Key", "Boots", "Torch", "Stair Up", "Stair Down", "Stair Up/Down","Energy"}));
		combo_RoomItem.setBounds(160, 158, 64, 64);
		panel_AllControls.add(combo_RoomItem);
		
		combo_RoomLiquid = new WideComboBox<String>(){
			
			private static final long serialVersionUID = -1463787135768823982L;

			@Override
			protected void paintComponent(Graphics g) {
				paintLiquidComboBox(g,this,false);
			}
		};
		combo_RoomLiquid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCurrentRoom().setRoomLiquid(LiquidType.fromIndex(combo_RoomLiquid.getSelectedIndex()));
				panel_AllControls.repaint();
			}
		});
		combo_RoomLiquid.setFocusable(false);
		combo_RoomLiquid.removeAll();
		combo_RoomLiquid.setModel(new DefaultComboBoxModel<String>(new String[] {"(Center Liquid)", "Water", "Lava"}));
		combo_RoomLiquid.setBounds(159, 234, 64, 25);
		panel_AllControls.add(combo_RoomLiquid);
		
		but_StartRoom = new JButton("Start Room");
		but_StartRoom.setFocusPainted(false);
		but_StartRoom.setFocusable(false);
		but_StartRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dungeon.setStartPosition(roomX, roomY, roomZ);
				but_StartRoom.setBackground(Color.red);
				but_StartRoom.repaint();
			}
		});
		but_StartRoom.setBounds(400, 130, 117, 25);
		panel_AllControls.add(but_StartRoom);
		
		check_RoomUsed = new JCheckBox("Not Used");
		check_RoomUsed.setFocusable(false);
		check_RoomUsed.setFocusPainted(false);
		check_RoomUsed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCurrentRoom().setUsed(check_RoomUsed.isSelected());;
			}
		});
		check_RoomUsed.setBounds(410, 158, 117, 23);
		panel_AllControls.add(check_RoomUsed);
		
		
		
		
		//Set up custom renderers
		ComboBoxRenderer door_rend, liquid_rend;
		door_rend = new ComboBoxRenderer(combo_TopDoor, DoorType.getColorArray());
		liquid_rend = new ComboBoxRenderer(combo_TopLiquid, LiquidType.getColorArray());
		item_rend = new ComboBoxRenderer(combo_RoomItem, ItemType.getColorArray());
		
		combo_TopDoor.setRenderer(door_rend);
		combo_BottomDoor.setRenderer(door_rend);
		combo_LeftDoor.setRenderer(door_rend);	
		combo_RightDoor.setRenderer(door_rend);
		
		combo_TopLiquid.setRenderer(liquid_rend);
		combo_BottomLiquid.setRenderer(liquid_rend);
		combo_LeftLiquid.setRenderer(liquid_rend);
		combo_RightLiquid.setRenderer(liquid_rend);
		combo_RoomLiquid.setRenderer(liquid_rend);
		
		combo_RoomItem.setRenderer(item_rend);

	}
	
	
	
	
	
	/**
	 * Actions that paint the background of the room.<br>
	 * <br>
	 * <i>Handles things such as walls, doors, items, etc.</i>
	 * 
	 * @param g The grapics to paint to...
	 */
	private void paintBackground(Graphics g) {
		//Draw the backgrounds for the room
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(192,192,192));
        g2.fillRect(10,10,360,360);
        
        g2.setColor(wallColor);
        g2.fillRect(10, 10, 30, 100);
        g2.fillRect(10, 10, 100, 30);
        g2.fillRect(370-30,370-100, 30, 100);
        g2.fillRect(370-100,370-30, 100, 30);
        g2.fillRect(10,370-100, 30, 100);
        g2.fillRect(10,370-30, 100, 30);
        g2.fillRect(370-30,10, 30, 100);
        g2.fillRect(370-100,10, 100, 30);
        
        //Draw liquids:
        LiquidType top_l, bot_l, left_l, right_l, cent_l;
        top_l = LiquidType.fromIndex(this.combo_TopLiquid.getSelectedIndex());
        bot_l = LiquidType.fromIndex(this.combo_BottomLiquid.getSelectedIndex());
        left_l = LiquidType.fromIndex(this.combo_LeftLiquid.getSelectedIndex());
        right_l = LiquidType.fromIndex(this.combo_RightLiquid.getSelectedIndex());
        cent_l = LiquidType.fromIndex(this.combo_RoomLiquid.getSelectedIndex());
        
        if (top_l != LiquidType.NO_LIQUID) {
        	g2.setColor(top_l.getColor());
        	g2.fillRect(40, 40, 300, 65);
        }
        
        if (bot_l != LiquidType.NO_LIQUID) {
        	g2.setColor(bot_l.getColor());
        	g2.fillRect(40, 275, 300, 65);
        }
        
        if (left_l != LiquidType.NO_LIQUID) {
        	g2.setColor(left_l.getColor());
        	g2.fillRect(40, 40, 65, 300);
        }
        
        if (right_l != LiquidType.NO_LIQUID) {
        	g2.setColor(right_l.getColor());
        	g2.fillRect(275, 40, 65, 300);
        }
        
        //Draw corners
        g2.setColor(LiquidType.getDominantLiquid(top_l, left_l).getColor());
        g2.fillRect(40, 40, 65, 65);
        g2.setColor(LiquidType.getDominantLiquid(top_l, right_l).getColor());
        g2.fillRect(275, 40, 65, 65);      
        g2.setColor(LiquidType.getDominantLiquid(bot_l, left_l).getColor());
        g2.fillRect(40, 275, 65, 65);
        g2.setColor(LiquidType.getDominantLiquid(bot_l, right_l).getColor());
        g2.fillRect(275, 275, 65, 65);
        
        
        //Draw center liquid
        if (cent_l != LiquidType.NO_LIQUID) {
        	g2.setColor(cent_l.getColor());
        	
        	g2.fillRect(40+32, 40+32, 300-66, 33);
        	g2.fillRect(40+32, 40+32, 33, 300-66);
        	g2.fillRect(275, 40+32, 33, 300-66);
        	g2.fillRect(40+32, 275, 300-66, 33);
        	
        	
        }
        
        
	}
	
	
	
	/**
	 * Get a rainbow paint object
	 * 
	 * @param width X Width of the rainbow paint
	 * @param height Y Height of the rainbow paint
	 * @return Rainbow
	 */
	public static Paint getRainbow(int width, int height) {
		Color[] allColors = new Color[]{Color.red, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PINK};
    	float[] fractions = new float[]{0.0f,0.2f,0.4f,0.6f,0.8f,0.9f};
    	
    	return new LinearGradientPaint(0, 0, width, height, fractions, allColors);
	}
	

	
	/**
	 * Draw a String centered in the middle of a Rectangle.
	 *
	 * @param g The Graphics instance.
	 * @param text The String to draw.
	 * @param rect The Rectangle to center the text in.
	 */
	private static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font
	    g.setFont(font);
	    // Draw the String
	    g.drawString(text, x, y);
	}

	
	
	/**
	 * Draw the text as a rotated string<br>
	 * <br>
	 * <i>This function does NOT WORK 100%</i>
	 * 
	 * @param g2d Graphics2D to draw to
	 * @param x Width of the rectangle
	 * @param y Height of the rectangle
	 * @param angle Angle to draw text
	 * @param text The text to draw
	 */
	private static void drawRotate(Graphics2D g2d, double x, double y, int angle, String text)  {    
	    g2d.translate((float)x,(float)y);
	    g2d.rotate(Math.toRadians(angle));

	    //Custom code to draw centered:
	    Rectangle rect = new Rectangle(0,0,(int) y, (int) x);
	    FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
	    int text_x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    int text_y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    g2d.drawString(text, 
	    		(angle == 180 || angle == 270) ? (text_x) : (-text_x),
	    		(angle == 90) || (angle == 180) ? text_y : -(text_y / 2));
	  
	    
	    g2d.rotate(-Math.toRadians(angle));
	    g2d.translate(-(float)x,-(float)y);
	}    

	
	
	/**
	 * Paint the four wall buttons in the room
	 * 
	 * @param g Graphics object to draw to
	 * @param but Button object calling this function
	 * @param t Wall Type to draw to this button
	 */
	private static void paintWallButton(Graphics g, JButton but, WallType t) {
        if (!but.isOpaque()) {
            g.setColor(but.getBackground());
            g.fillRect(0, 0, but.getWidth(), but.getHeight());
        }
        
        //Test for rainbow
        if (t == WallType.EXIT) {
        	Graphics2D g2 = (Graphics2D) g.create();
        	g2.setPaint(getRainbow(but.getWidth(), but.getHeight()));
        	g2.fillRect(0, 0, but.getWidth(), but.getHeight());
        }
        
	}
	

	/**
	 * Paint the four door combo boxes in this room
	 * 
	 * @param g The graphics to draw to
	 * @param box The combo box calling this function
	 * @param isVertical Is this top/bottom or left/right button?
	 */
	private static void paintDoorComboBox(Graphics g, JComboBox<String> box, boolean isVertical) {

		//When to make rectangle transparent?
		if (DoorType.fromIndex(box.getSelectedIndex()) == DoorType.NO_DOOR) {
			g.setColor(new Color(0,0,0,0));
		} else {
			g.setColor(DoorType.fromIndex(box.getSelectedIndex()).getColor());
		}
		g.fillRect(0, 0, box.getWidth(), box.getHeight());
		
		if (box.isEnabled()) {
			g.setColor(Color.black);
		} else {
			g.setColor(new Color(220,220,220));
		}
		
		//When to draw vertical or horizontal
		if (isVertical) {
			drawRotate((Graphics2D) g, box.getWidth(), box.getHeight(),
					270, DoorType.fromIndex(box.getSelectedIndex()).getName());	
		} else {
			drawCenteredString(g, DoorType.fromIndex(box.getSelectedIndex()).getName(),
				new Rectangle(0,0,box.getWidth(),box.getHeight()), box.getFont());
		}
		
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, box.getWidth()-1, box.getHeight()-1);
	}
	
	
	
	
	/**
	 * Paint the four liquid combo boxes
	 * 
	 * @param g The graphics to draw to
	 * @param box The combo box calling this function
	 * @param isVertical Is this top/bottom or left/right combo box?
	 */
	private static void paintLiquidComboBox(Graphics g, JComboBox<String> box, boolean isVertical) {
		
		if (LiquidType.fromIndex(box.getSelectedIndex()) == LiquidType.NO_LIQUID) {
			g.setColor(new Color(0,0,0,0));
		} else {
			g.setColor(LiquidType.fromIndex(box.getSelectedIndex()).getColor());
		}
		g.fillRect(0, 0, box.getWidth(), box.getHeight());
		
		if (box.isEnabled()) {
			g.setColor(Color.black);
		} else {
			g.setColor(new Color(220,220,220));
		}
		if (isVertical) {
			drawRotate((Graphics2D) g, box.getWidth(), box.getHeight(), 
				270, LiquidType.fromIndex(box.getSelectedIndex()).getName());
			
		} else {
			drawCenteredString(g, LiquidType.fromIndex(box.getSelectedIndex()).getName(),
				new Rectangle(0,0,box.getWidth(),box.getHeight()), box.getFont());
		}
		
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, box.getWidth()-1, box.getHeight()-1);
	}
	
	

	

	/**
	 * Create a new dungeon in the room
	 */
	private void newDungeon() {
		
		
		SizePicker p = new SizePicker();
		
		int result = JOptionPane.showConfirmDialog(null,p,"Create New Dungeon:", JOptionPane.OK_CANCEL_OPTION);
		
		//Make sure they clicked okay
		if (result == JOptionPane.OK_OPTION) {
			
			//Make the new dungeon
			int length = p.getChosenLength();
			int width = p.getChosenWidth();
			int height = p.getChosenHeight();
			
			this.dungeon = new Dungeon(length, width, height);
			
			//Reset X,Y,Z
			this.roomX = 0;
			this.roomY = 0;
			this.roomZ = 0;
			
			unlockControls();

			
			//And do update
			this.loadFromMemory();
			
		}
	}
	
	
	
	/**
	 * Unlock the controls that are disabled when the room starts
	 */
	private void unlockControls() {
		//Enable all controls (if not enabled)
		if (this.panel_AllControls.isEnabled() == false) {
			this.panel_AllControls.setEnabled(true);
			panelDisabler.enable();
			this.item_ResizeDungeon.setEnabled(true);
			this.item_SaveFile.setEnabled(true);
			this.item_Export.setEnabled(true);
			this.item_MapImage.setEnabled(true);
			this.item_SaveMaps.setEnabled(true);
			this.item_ClearRoom.setEnabled(true);
			this.item_ClearDungeon.setEnabled(true);
			this.item_NewColor.setEnabled(true);
			
		}
		
	}
	
	
	/**
	 * Resize the existing dungeon
	 */
	private void resizeDungeon() {
		
		
		SizePicker p = new SizePicker();
		p.setChosenLength(this.dungeon.getLength());
		p.setChosenWidth(this.dungeon.getWidth());
		p.setChosenHeight(this.dungeon.getHeight());
		
		int result = JOptionPane.showConfirmDialog(null,p,"Resize Dungeon:", JOptionPane.OK_CANCEL_OPTION);
		
		//Make sure they clicked okay
		if (result == JOptionPane.OK_OPTION) {
			
			//Make the new dungeon
			int length = p.getChosenLength();
			int width = p.getChosenWidth();
			int height = p.getChosenHeight();
			
			this.dungeon.resizeDungeon(length, width, height);
			
			//Reset X,Y,Z
			this.roomX = 0;
			this.roomY = 0;
			this.roomZ = 0;
			
			
			//And do update
			this.loadFromMemory();
			
		}
		
	}
	
	
	
	/**
	 * Get the currently selected room
	 * 
	 * @return Current Room
	 */
	private Room getCurrentRoom() {
		return this.dungeon.getRoom(this.roomX, this.roomY, this.roomZ);
	}

	
	
	/**
	 * Download all controls from memory into form controls
	 */
	private void loadFromMemory() {
		
		Room r = getCurrentRoom();
		
		//Figure out when to disable stairs
		if (this.roomZ == 0) {
			item_rend.setEnabledState(STAIR_DOWN, false);
		} else {
			item_rend.setEnabledState(STAIR_DOWN, true);
		}
		
		if (this.roomZ == this.dungeon.getHeight() - 1) {
			item_rend.setEnabledState(STAIR_UP, false);
		} else {
			item_rend.setEnabledState(STAIR_UP, true);
		}
		
		
		if (item_rend.getEnabledState(STAIR_UP) && item_rend.getEnabledState(STAIR_DOWN)) {
			item_rend.setEnabledState(STAIR_UP_DOWN, true);
		} else {
			item_rend.setEnabledState(STAIR_UP_DOWN, false);	
		}
		
		
		
		//Update colors first
		MainWindow.wallColor = r.getWallColor();
		this.panel_AllControls.repaint();
		
		//Do walls
		this.but_TopWall.setBackground(r.getWall(Direction.TOP).getColor());
		this.but_TopWall.setText(r.getWall(Direction.TOP).getName(false));
		this.TopWallIndex = r.getWall(Direction.TOP).getIndex();
		this.but_BottomWall.setBackground(r.getWall(Direction.BOTTOM).getColor());
		this.but_BottomWall.setText(r.getWall(Direction.BOTTOM).getName(false));
		this.BottomWallIndex = r.getWall(Direction.BOTTOM).getIndex();
		this.but_LeftWall.setBackground(r.getWall(Direction.LEFT).getColor());
		this.but_LeftWall.setText(r.getWall(Direction.LEFT).getName(true));
		this.LeftWallIndex = r.getWall(Direction.LEFT).getIndex();
		this.but_RightWall.setBackground(r.getWall(Direction.RIGHT).getColor());
		this.but_RightWall.setText(r.getWall(Direction.RIGHT).getName(true));
		this.RightWallIndex = r.getWall(Direction.RIGHT).getIndex();
		
		//Do Doors
		this.combo_TopDoor.setSelectedIndex(r.getDoor(Direction.TOP).getIndex());
		this.combo_BottomDoor.setSelectedIndex(r.getDoor(Direction.BOTTOM).getIndex());
		this.combo_LeftDoor.setSelectedIndex(r.getDoor(Direction.LEFT).getIndex());
		this.combo_RightDoor.setSelectedIndex(r.getDoor(Direction.RIGHT).getIndex());
		
		//Do Liquids
		this.combo_TopLiquid.setSelectedIndex(r.getLiquid(Direction.TOP).getIndex());
		this.combo_BottomLiquid.setSelectedIndex(r.getLiquid(Direction.BOTTOM).getIndex());
		this.combo_LeftLiquid.setSelectedIndex(r.getLiquid(Direction.LEFT).getIndex());
		this.combo_RightLiquid.setSelectedIndex(r.getLiquid(Direction.RIGHT).getIndex());
		
		//Other liquids
		this.combo_RoomItem.setSelectedIndex(r.getRoomItem().getIndex());
		this.combo_RoomLiquid.setSelectedIndex(r.getRoomLiquid().getIndex());
		
		
		//Update XYZ labels
		this.textbox_X.setText(Integer.toString(this.roomX));
		this.textbox_Y.setText(Integer.toString(this.roomY));
		this.textbox_Z.setText(Integer.toString(this.roomZ));
		
		
		//Update the buttons
		if (this.roomY > 0) {
			this.but_RoomUp.setEnabled(true);
		} else {
			this.but_RoomUp.setEnabled(false);
		}
		
		if (this.roomY < this.dungeon.getWidth() - 1) {
			this.but_RoomDown.setEnabled(true);
		} else {
			this.but_RoomDown.setEnabled(false);
		}
		
		if (this.roomX > 0) {
			this.but_RoomLeft.setEnabled(true);
		} else {
			this.but_RoomLeft.setEnabled(false);
		}
		
		if (this.roomX < this.dungeon.getLength() - 1) {
			this.but_RoomRight.setEnabled(true);
		} else {
			this.but_RoomRight.setEnabled(false);
		}
		
		if (this.roomZ < this.dungeon.getHeight() - 1) {
			this.but_LevelUp.setEnabled(true);
		} else {
			this.but_LevelUp.setEnabled(false);
		}
		
		if (this.roomZ > 0) {
			this.but_LevelDown.setEnabled(true);
		} else {
			this.but_LevelDown.setEnabled(false);
		}
		
		
		this.check_RoomUsed.setSelected(getCurrentRoom().notUsed());
		
		//Set the background color based on the selected start room
		if (this.roomX == this.dungeon.getStartX() &&
				this.roomY == this.dungeon.getStartY() &&
				this.roomZ == this.dungeon.getStartZ()) {
			this.but_StartRoom.setBackground(Color.RED);
		} else {
			this.but_StartRoom.setBackground(new Color(238,238,238));
		}
		
	}
	
	
	
	/**
	 * Save the current combo box controls to memory
	 */
	private void saveToMemory() {
		
		Room r = getCurrentRoom();
		
		//Do walls
		r.setWall(WallType.fromIndex(this.TopWallIndex), Direction.TOP);
		r.setWall(WallType.fromIndex(this.BottomWallIndex), Direction.BOTTOM);
		r.setWall(WallType.fromIndex(this.LeftWallIndex), Direction.LEFT);
		r.setWall(WallType.fromIndex(this.RightWallIndex), Direction.RIGHT);
		
		//Do Doors
		r.setDoor(DoorType.fromIndex(this.combo_TopDoor.getSelectedIndex()), Direction.TOP);
		r.setDoor(DoorType.fromIndex(this.combo_BottomDoor.getSelectedIndex()), Direction.BOTTOM);
		r.setDoor(DoorType.fromIndex(this.combo_LeftDoor.getSelectedIndex()), Direction.LEFT);
		r.setDoor(DoorType.fromIndex(this.combo_RightDoor.getSelectedIndex()), Direction.RIGHT);

		
		//Do Liquids
		r.setLiquid(LiquidType.fromIndex(this.combo_TopLiquid.getSelectedIndex()), Direction.TOP);
		r.setLiquid(LiquidType.fromIndex(this.combo_BottomLiquid.getSelectedIndex()), Direction.BOTTOM);
		r.setLiquid(LiquidType.fromIndex(this.combo_LeftLiquid.getSelectedIndex()), Direction.LEFT);
		r.setLiquid(LiquidType.fromIndex(this.combo_RightLiquid.getSelectedIndex()), Direction.RIGHT);
		
		
		//Other liquids
		r.setRoomItem(ItemType.fromIndex(this.combo_RoomItem.getSelectedIndex()));
		r.setRoomLiquid(LiquidType.fromIndex(this.combo_RoomLiquid.getSelectedIndex()));
	}
	
	
	/**
	 * Export to Python file
	 */
	private void exportPython() {

		this.saveToMemory();
		
		//Configure the file browser
		
		FileFilter filter = new FileNameExtensionFilter("Python File (.py)","py");
		fc.setFileFilter(filter);
		
		int result = fc.showSaveDialog(null);
	
		if (result == JFileChooser.APPROVE_OPTION) {
		
			try {
				FileWriter fw = new FileWriter(forceExtension(fc.getSelectedFile(),".py"));
				fw.write(this.dungeon.export());
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	/**
	 * Force a file to have a specified extension
	 * 
	 * @param input File to check
	 * @param suffix The file suffix to force
	 * @return File with extension
	 */
	private static File forceExtension(File input, String suffix) {
		if(!input.getAbsolutePath().endsWith(suffix)){
		    return new File(input.getAbsolutePath() + suffix);
		} else {
			return input;
		}
	}
	
	
	
	/**
	 * Save all dungeon map images to various files
	 */
	private void exportMapImages() {
	
		saveToMemory();
		
		//Configure the file browser
		FileFilter filter = new FileNameExtensionFilter("PNG File (.png)","png");
		fc.setFileFilter(filter);
		
		int result = fc.showSaveDialog(null);
	
		if (result == JFileChooser.APPROVE_OPTION) {	
			try {
				
				String name = fc.getSelectedFile().getAbsolutePath();
				name = name.substring(0, name.lastIndexOf("."));

				System.out.println(name);
				
				for (int i = 0; i < this.dungeon.getHeight(); i++) {
				
					File img = forceExtension(new File(name + "-Lvl_" + i), ".png");
					
					ImageIO.write((RenderedImage) this.dungeon.buildImage(i),"png",img);
				}

				JOptionPane.showConfirmDialog(null, "All map images have been saved", "Images Saved", JOptionPane.OK_CANCEL_OPTION);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	
	/**
	 * Open a dungeon file and import
	 */
	private void openDungeon() {
		
		FileFilter filter = new FileNameExtensionFilter("Walls Dungeon Map File (.wdm)","wdm");
		fc.setFileFilter(filter);
		
		int result = fc.showOpenDialog(null);
	
		if (result == JFileChooser.APPROVE_OPTION) {
		
			//Fix dungeon null pointer
			if (this.dungeon == null) {
				this.dungeon = new Dungeon(1,1,1);
			}
			
			
			try {

				FileInputStream fin = new FileInputStream(fc.getSelectedFile());
				
				if (Dungeon.loadDungeon(fin, this.dungeon) == true) {
					fin.close();
				
					this.roomX = 0;
					this.roomY = 0;
					this.roomZ = 0;
					
					unlockControls();
					loadFromMemory();
					
					JOptionPane.showConfirmDialog(null, "Dungeon was successfully opened", "Dungeon Opened", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
				} else {
					fin.close();
					JOptionPane.showConfirmDialog(null, "Error opening dungeon file", "File Error", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	/**
	 * Save the dungeon to a file
	 */
	public void saveDungeon() {
	
		saveToMemory();
		
		FileFilter filter = new FileNameExtensionFilter("Walls Dungeon Map File (.wdm)","wdm");
		fc.setFileFilter(filter);
		
		int result = fc.showSaveDialog(null);
	
		if (result == JFileChooser.APPROVE_OPTION) {
		
			try {
				FileWriter fw = new FileWriter(forceExtension(fc.getSelectedFile(),".wdm"));
				this.dungeon.saveDungeon(fw);
				fw.close();
				
				JOptionPane.showConfirmDialog(null, "Your dungeon has been saved", "File Saved", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	
	
	/**
	 * Handles all key events for the form
	 */
	private class KeyEvents implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
          	if (e.getKeyCode() == KeyEvent.VK_UP) {but_RoomUp.doClick();}
          	if (e.getKeyCode() == KeyEvent.VK_DOWN) {but_RoomDown.doClick();}
          	if (e.getKeyCode() == KeyEvent.VK_LEFT) {but_RoomLeft.doClick();}
          	if (e.getKeyCode() == KeyEvent.VK_RIGHT) {but_RoomRight.doClick();}
          	if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {but_LevelUp.doClick();}
          	if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {but_LevelDown.doClick();}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
    }
}
