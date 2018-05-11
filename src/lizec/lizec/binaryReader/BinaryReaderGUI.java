package lizec.lizec.binaryReader;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class BinaryReaderGUI {
	private File thisFile = new File("~");
	
	private JFrame frame;
	private JTextArea txtBinary = new JTextArea();
	private JTextArea txtText = new JTextArea();
	private JScrollPane scrollBinary = new JScrollPane();
	private JScrollPane scrollText = new JScrollPane();
	private JFileChooser jf = new JFileChooser(thisFile);
	

	private boolean isFileChanged = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
            try {
                BinaryReaderGUI window = new BinaryReaderGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	}

	/**
	 * Create the application.
	 */
	public BinaryReaderGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 858, 414);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 0;
		panel.add(scrollBinary, gbc_scrollPane_1);
		txtBinary.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				isFileChanged = true;
				
				frame.setTitle(thisFile.getAbsolutePath()+"*");
			}
		});
		
		txtBinary.setText("Binary");
		scrollBinary.setViewportView(txtBinary);

		
		
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 0;
		panel.add(scrollText, gbc_scrollPane);
		
		txtText.setText("Text");
		scrollText.setViewportView(txtText);
		
		//使用等宽字体，从而使数据对其
		txtBinary.setFont(new Font("Courier New",Font.PLAIN,20));
		txtText.setFont(new Font("Courier New",Font.PLAIN,20));
		
		//btnNewButton.addActionListener(OpenFile);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(isFileChanged){
					int op = JOptionPane.showConfirmDialog(null, "有还未保存的修改，是否保存？");
					if(op == JOptionPane.OK_OPTION){
						acSaveFile.actionPerformed(null);
					}
					else if(op == JOptionPane.NO_OPTION){
						System.exit(0);
					}
				}
			}			
			
		});
		
		//两个滑动框共用一个滑动条，从而实现滑动的同步，不需要任何额外的代码，方便快捷
		scrollText.setVerticalScrollBar(scrollBinary.getVerticalScrollBar());
		
		initMenus(frame);
	}
	
	private void initMenus(JFrame frame){
		
		JMenuBar menuBar = new JMenuBar();
		
			JMenu mnFile = new JMenu("文件");
			
				JMenuItem miOpen = new JMenuItem("打开文件");
				miOpen.addActionListener(acOpenFile);
			mnFile.add(miOpen);
			
				JMenuItem miCreate = new JMenuItem("创建文件");
				//
			mnFile.add(miCreate);
				
				JMenuItem miSave = new JMenuItem("保存");
				miSave.addActionListener(acSaveFile);
			mnFile.add(miSave);
		
				JMenuItem miSaveAs = new JMenuItem("另存为");
				miSaveAs.addActionListener(acSaveAsFile);
			mnFile.add(miSaveAs);
			
				JMenuItem miNewWindow = new JMenuItem("创建新窗口");
				//
			mnFile.add(miNewWindow);
			
				JMenuItem miExit = new JMenuItem("退出");
				//
			mnFile.add(miExit);
			
			JMenu mnEdit = new JMenu("编辑");
			JMenu mnView = new JMenu("视图");
			
			JMenu mnTools = new JMenu("工具");
				JMenuItem miDecode = new JMenuItem("反编译");
				//
			mnTools.add(miDecode);
			
			
			JMenu mnHelp = new JMenu("帮助");
				JMenuItem miAbout = new JMenuItem("关于此软件");
				//
			mnHelp.add(miAbout);
		
		
		menuBar.add(mnFile);
		menuBar.add(mnEdit);
		menuBar.add(mnView);
		menuBar.add(mnTools);
		menuBar.add(mnHelp);
		
		frame.setJMenuBar(menuBar);
	}
	
	private void clearStar()
	{
		frame.setTitle(thisFile.getAbsolutePath());
	}
	
	private ActionListener acOpenFile = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jf.showOpenDialog(null);
			try {
				//saveFile();
				
				thisFile = jf.getSelectedFile();
				//binaryReader.setSelectFile(lastFile);
				txtBinary.setText(BinaryReader.readAsHex(thisFile));
				txtText.setText(BinaryReader.readAsText(thisFile));
				BinaryReaderGUI.this.frame.setTitle(thisFile.toString());
				new Thread(goToBegin).start();
			} catch (IOException e1) {		
				e1.printStackTrace();
			} catch (NullPointerException e2) {
				//用户点击了取消，什么都不做
			}
			
		}
	};
	
	private ActionListener acSaveFile = e -> {
		if(isFileChanged){
			String dataString = BinaryReaderGUI.this.txtBinary.getText();
			File tempFile = new File(thisFile.getAbsolutePath()+".temp");
			try {
				BinaryReader.writeFileAsHex(dataString,tempFile);
				//java.nio.file.Files.delete(lastFile.toPath());
				if(thisFile.delete()){
					if(!tempFile.renameTo(thisFile)){
						JOptionPane.showMessageDialog(null,
                                "文件重命名失败，请手动删除.temp后缀以恢复文件，并检查相关配置");
					}
				}
				else{
					JOptionPane.showMessageDialog(null,
                            "源文件删除失败，请检查相关配置");
				}
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null,
                        "文件保存失败，请确认后重试");
				e1.printStackTrace();
			}

			isFileChanged = false;
			clearStar();
		}

	};
	
	private ActionListener acSaveAsFile = e -> {
		jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jf.showOpenDialog(null);

		String dataString = BinaryReaderGUI.this.txtBinary.getText();
		File tempFile = jf.getSelectedFile();
		if(tempFile.exists()){
			int op = JOptionPane.showConfirmDialog(null, "文件已存在，确定要覆盖此文件吗");
			// 如果选择了其他选项，则直接返回
			if(op != 0){
				return;
			}
		}

		try {
			BinaryReader.writeFileAsHex(dataString,tempFile);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "文件另存为失败，请确认后重试");
			e1.printStackTrace();
		}

		thisFile = tempFile;
		clearStar();
	};
	
	private Runnable goToBegin = () -> {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		scrollBinary.getVerticalScrollBar().setValue(0);
	};
	
}
