package SQLQT_UI.Graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JTabbedPane;

import org.apache.commons.io.IOUtils;

import SQLQT_UI.Logic.Listeners.*;
import SQLQT_Utilities.ConnectionToBD;
import SQLQT_Utilities.DBManager;
import SQLQT_Utilities.TableManager;

import jsyntaxpane.DefaultSyntaxKit;
/**
 * 
 * @author Anastasiya Goncharova
 *
 */
public class SQLQueryTesterUI extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException 
	{
		Class.forName("org.sqlite.JDBC");
		Connection con = null;
		try
		{
			con = DriverManager.getConnection("jdbc:sqlite:patients.db");
		}
		catch(SQLException e)
		{
			System.err.println(e.getMessage());
	    }
		ConnectionToBD connection = new ConnectionToBD(con);
		DBManager.initializeDatabase();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					SQLQueryTesterUI frame = new SQLQueryTesterUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creates the main frame
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public SQLQueryTesterUI() throws ClassNotFoundException, SQLException 
	{
		setTitle("TestDB");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new MainWindowListener());
		setBounds(100, 100, 921, 562);
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		Font font = new Font("Courier New", Font.PLAIN, 14);
		Font tableHeaderFont = new Font("Courier New", Font.BOLD, 14);
		
		JTable table = new JTable();
		JEditorPane sqlSyntaxPane = new JEditorPane();
		JScrollPane scrollPane_selectResult = new JScrollPane();
		JTextPane textPane_Response = new JTextPane();
		JScrollPane scrollPane_textResponse = new JScrollPane(textPane_Response);
		JTabbedPane tabbedPane = new JTabbedPane();
		StatusBar statusBar = new StatusBar(getWidth());

		//=================== TEXT PANES ==========================
		JPanel textPanesPanel = new JPanel();
		textPanesPanel.setLayout(new BoxLayout(textPanesPanel, BoxLayout.Y_AXIS));

		DefaultSyntaxKit.initKit();

		JScrollPane scrollPane_textQuery = new JScrollPane(sqlSyntaxPane);
		sqlSyntaxPane.setContentType("text/sql");
		sqlSyntaxPane.setFont(font);
		sqlSyntaxPane.setText("Enter query here...");
		sqlSyntaxPane.addMouseListener(new ClickTextPaneMouseListener(sqlSyntaxPane));
		sqlSyntaxPane.addFocusListener(new TextPaneFocusListener(sqlSyntaxPane));

		scrollPane_textQuery.setPreferredSize(new Dimension(416, 162));

		//===================== TABBED PANE =========================
		tabbedPane.setPreferredSize(new Dimension(416, 265));

		scrollPane_selectResult.getViewport().setBackground(Color.WHITE);
		scrollPane_textResponse.getViewport().setBackground(Color.WHITE);

		tabbedPane.addTab("Result output", scrollPane_selectResult);
		tabbedPane.addTab("Messages", scrollPane_textResponse);

		JSplitPaneWithZeroSizeDivider splitPane = new JSplitPaneWithZeroSizeDivider(JSplitPane.VERTICAL_SPLIT);
		splitPane.setResizeWeight(0.5);
		splitPane.setBorder(null);
		splitPane.setTopComponent(scrollPane_textQuery);
		splitPane.setBottomComponent(tabbedPane);
		textPanesPanel.add(splitPane);
				
		//==================== ADD MENU ============================
		JMenuBar menuBar = new JMenuBar();
		menuBar.setMinimumSize(new Dimension(905, 21));
		menuBar.setPreferredSize(new Dimension(905, 21));
		menuBar.setMaximumSize(new Dimension(Short.MAX_VALUE, 21));
		contentPane.add(menuBar);
		
		Font menuFont = new Font("Arial", Font.PLAIN, 12);
		JMenu mnFile = new JMenu("File");
		mnFile.setFont(menuFont);
		String space = "           ";
		JMenuItem menuItem1 = new JMenuItem("Open" + space);
		menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menuItem1.setFont(menuFont);
		menuItem1.addActionListener(new LoadQueryButtonActionListener(this, sqlSyntaxPane, textPane_Response, tabbedPane));
		mnFile.add(menuItem1);
		JMenuItem menuItem2 = new JMenuItem("Save" + space);
		menuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menuItem2.addActionListener(new SaveQueryButtonActionListener(this, sqlSyntaxPane, textPane_Response, tabbedPane));
		menuItem2.setFont(menuFont);
		mnFile.add(menuItem2);
		mnFile.addSeparator();
		JMenuItem menuItem3 = new JMenuItem("Exit" + space);
		menuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		menuItem3.addActionListener(new ExitMenuActionListener(this));
		menuItem3.setFont(menuFont);
		mnFile.add(menuItem3);
		menuBar.add(mnFile);
		
		JMenu mnTable = new JMenu("Table");
		mnTable.setFont(menuFont);
		JMenuItem menuItem4 = new JMenuItem("Reset table");
		menuItem4.setFont(menuFont);
		ResetTableActionListener resetTableActionListener = new ResetTableActionListener(table, textPane_Response, scrollPane_textResponse, tabbedPane, statusBar);
		menuItem4.addActionListener(resetTableActionListener);
		mnTable.add(menuItem4);
		menuBar.add(mnTable);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setFont(menuFont);
		JMenuItem menuItem5 = new JMenuItem("Table description");
		menuItem5.setFont(menuFont);
		InfoButtonActionListener infoButtonActionListener = new InfoButtonActionListener(this, tabbedPane, textPane_Response);
		menuItem5.addActionListener(infoButtonActionListener);
		mnHelp.add(menuItem5);
		menuBar.add(mnHelp);
		
		//==================== ADD TOOL BAR ========================
		JToolBar toolBar = new JToolBar();
		toolBar.setMinimumSize(new Dimension(905, 29));
		toolBar.setPreferredSize(new Dimension(905, 29));
		toolBar.setMaximumSize(new Dimension(Short.MAX_VALUE, 29));
		toolBar.setFloatable(false);
		
		//================= TOOL_BAR_BUTTON ========================
		JSeparator sep1 = new JSeparator(SwingConstants.VERTICAL);
		sep1.setMaximumSize(new Dimension(2, 29));
		sep1.setForeground(Color.lightGray);
		JSeparator sep2 = new JSeparator(SwingConstants.VERTICAL);
		sep2.setMaximumSize(new Dimension(2, 29));
		sep2.setForeground(Color.lightGray);
		
		//load button icons
		byte[] resultArray1 = null;
		byte[] resultArray2 = null;
		byte[] resultArray3 = null;
		byte[] resultArray4 = null;
		byte[] resultArray5 = null;
		InputStream is1 = null;
		InputStream is2 = null;
		InputStream is3 = null;
		InputStream is4 = null;
		InputStream is5 = null;
		try
		{
			is1 = getClass().getResourceAsStream("/images/icon_transparent.png");
			resultArray1 = IOUtils.toByteArray(is1);
			is2 = getClass().getResourceAsStream("/images/load_sql.png");
			resultArray2 = IOUtils.toByteArray(is2);
			is3 = getClass().getResourceAsStream("/images/save_sql.png");
			resultArray3 = IOUtils.toByteArray(is3);
			is4 = getClass().getResourceAsStream("/images/reset_table.png");
			resultArray4 = IOUtils.toByteArray(is4);
			is5 = getClass().getResourceAsStream("/images/i_table.png");
			resultArray5 = IOUtils.toByteArray(is5);
		}
		catch(Exception e)
		{
			e.printStackTrace();
        } 
		finally
		{
			try 
			{
				is1.close();
				is2.close();
				is3.close();
				is4.close();
				is5.close();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		}
		
		ImageIcon ic = new ImageIcon(resultArray1);
		
		ToolBarButton btnNewButton_1 = new ToolBarButton(ic);
		btnNewButton_1.setFocusPainted(false);
		UIManager.put("ToolTip.background",new ColorUIResource(255, 247, 200));
		Font toolTipFont = new Font("Arial", Font.PLAIN, 12);
		UIManager.put("ToolTip.font", toolTipFont);
		btnNewButton_1.setToolTipText("Run query");
		btnNewButton_1.addActionListener(new RunQueryButtonActionListener(this, table, sqlSyntaxPane, textPane_Response, scrollPane_selectResult, scrollPane_textResponse, tabbedPane, statusBar));
		toolBar.addSeparator(new Dimension(3,0));
		toolBar.add(btnNewButton_1);
		toolBar.addSeparator(new Dimension(5,0));
		toolBar.add(sep1);
		toolBar.addSeparator(new Dimension(5,0));
		
		ImageIcon ic2 = new ImageIcon(resultArray2);
		
		ToolBarButton btnNewButton_2 = new ToolBarButton(ic2);
		btnNewButton_2.setFocusPainted(false);
		btnNewButton_2.setToolTipText("Load SQL from file");
		btnNewButton_2.addActionListener(new LoadQueryButtonActionListener(this, sqlSyntaxPane, textPane_Response, tabbedPane));
		toolBar.add(btnNewButton_2);
		
		ImageIcon ic3 = new ImageIcon(resultArray3);
		
		ToolBarButton btnNewButton_3 = new ToolBarButton(ic3);
		btnNewButton_3.setFocusPainted(false);
		btnNewButton_3.setToolTipText("Save SQL into file");
		btnNewButton_3.addActionListener(new SaveQueryButtonActionListener(this, sqlSyntaxPane, textPane_Response, tabbedPane));
		toolBar.add(btnNewButton_3);
		toolBar.addSeparator(new Dimension(5,0));
		toolBar.add(sep2);
		toolBar.addSeparator(new Dimension(5,0));
		
		ImageIcon ic4 = new ImageIcon(resultArray4);
		
		ToolBarButton btnNewButton_4 = new ToolBarButton(ic4);
		btnNewButton_4.setFocusPainted(false);
		btnNewButton_4.setToolTipText("Reset table");
		btnNewButton_4.addActionListener(resetTableActionListener);
		toolBar.add(btnNewButton_4);
				
		ImageIcon ic5 = new ImageIcon(resultArray5);
		
		ToolBarButton btnNewButton_5 = new ToolBarButton(ic5);
		btnNewButton_5.setFocusPainted(false);
		btnNewButton_5.setToolTipText("Table description");
		btnNewButton_5.addActionListener(infoButtonActionListener);
		toolBar.add(btnNewButton_5);
		
		contentPane.add(Box.createRigidArea(new Dimension(0,1)));
		contentPane.add(toolBar);
		contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
				
		//=================== TABLE ================================
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.X_AXIS));
		
		table.getTableHeader().setFont(tableHeaderFont);
		table.setFont(font);
		TableManager.displayRowsFromDB(table);
		//table.getColumnModel().getColumn(4).setCellRenderer(table.getDefaultRenderer(ImageIcon.class));
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		((DefaultTableModel)table.getModel()).fireTableDataChanged();
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setMinimumSize(new Dimension(100, 0));
		scrollPane.setPreferredSize(new Dimension(450, 440));

		TableMouseListener mouseListener = new TableMouseListener(table, this);
		table.addMouseListener(mouseListener);
		table.addMouseMotionListener(mouseListener);
		
		JSplitPaneWithZeroSizeDivider splitPane2 = new JSplitPaneWithZeroSizeDivider(JSplitPane.HORIZONTAL_SPLIT);
		splitPane2.setResizeWeight(0.5);
		splitPane2.setBorder(null);
		splitPane2.setLeftComponent(textPanesPanel);
		splitPane2.setRightComponent(scrollPane);
		tablePanel.add(splitPane2);
		
		contentPane.add(tablePanel);
				
		//====================== STATUS BAR =========================
		contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
		contentPane.add(statusBar);
	}
}


