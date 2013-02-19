package kwic;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Color;
import java.awt.SystemColor;
/** 
 * My application look better on a Mac. I doubt this would affect my grade, 
 * but I use a style made by apple that is Mac-only to make my search boxes have rounded sides 
 * and search bar icon. There is no window equivalent. 
 * 
 * I added mutliple windows to make editing simpler for the user.
 * 
 * I sorted the list and made the window scalable. 
 * 
 * Force Associate cannot just be arbitarily called on any word or phrase. 
 * This is by design. A user must add the word to kwic in one menu before he can add phrases to it
 * 
 *
 */
public class MainWindows extends JFrame {
	
	private static MainWindows instance;
	static final JList jl = new JList();
	final JList phrases = new JList();
	public KWIC kwic = new KWIC();
	protected PropertyChangeSupport pcs = kwic.getPCS();
	private JPanel contentPane;	
	private JTextField searchFieldBox;
	/** 
	 * This is used for my multi-window approach. This ensures that multiple instances of main are never running.
	 */
	public static MainWindows instance(){
		if (instance == null){  //Checks to make sure no other MainWindow Exists. 
			instance = new MainWindows(); //If not, it creates one.
		}
		return instance; //This allows other windows to class instance() and access methods and variables within MainWindow without launching multiple applications.

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindows frame = instance();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private MainWindows() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 627, 303);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY); //Nice color
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 2, 0, 0));

		JScrollPane wordScrollPane = new JScrollPane(); //Scroll Pane for Word List
		JScrollPane phraseScrollPane = new JScrollPane();//Scroll Pane for Phrase List
		
		
		contentPane.add(wordScrollPane);
		contentPane.add(phraseScrollPane);
		
		jl.setBackground(SystemColor.window); //Word list. 
		jl.setMinimumSize(new Dimension(100, 100));
        jl.setMaximumSize(new Dimension(1000, 1000));
        phrases.setBackground(SystemColor.window);

		wordScrollPane.setViewportView(jl);
		phraseScrollPane.setViewportView(phrases);
		
		JToolBar wordToolbar = new JToolBar(); //Create and style wordToolbar (ToolBar at top of word list)
		wordToolbar.setFloatable(false);
		wordToolbar.setBackground(Color.GRAY);
		wordToolbar.setForeground(Color.RED);
		wordScrollPane.setColumnHeaderView(wordToolbar);
		
		JButton wordEditButton = new JButton("Edit Word List");
		wordToolbar.add(wordEditButton);
		wordEditButton.addActionListener(new ActionListener() { //Makes "Edit Words List" open the Edit Word Windows
			public void actionPerformed(ActionEvent e) {	
				EditWords edit = new EditWords();
				edit.setVisible(true);
			}
		});
		
		searchFieldBox = new JTextField();
		searchFieldBox.putClientProperty( "JTextField.variant", "search"); //Makes search box look rounded on a Mac only. There is no windows equivalent method.
		
		searchFieldBox.setColumns(10);
		searchFieldBox.addActionListener(new ActionListener(){ //Makes search box respond to the user clicking enter by jumping to the value in the word list, and showing asociated phrases in the phrase list
            public void actionPerformed(ActionEvent e){
            	Set<Phrase> phraseSet =kwic.getPhrases(new Word(searchFieldBox.getText()));
				Object[] labels = phraseSet.toArray();
				jl.setSelectedValue(new Word(searchFieldBox.getText()), true);
				phrases.setListData(labels);
            }});
		wordToolbar.add(searchFieldBox);
		
		JToolBar phrasesToolbar = new JToolBar(); //Create and style 
		phrasesToolbar.setBackground(SystemColor.inactiveCaption);
		phrasesToolbar.setFloatable(false);
		phraseScrollPane.setColumnHeaderView(phrasesToolbar);
		Component horizontalGlue = Box.createHorizontalGlue(); //Horizontal glue is used to move Edit Phrase Box to the right side of the window
		phrasesToolbar.add(horizontalGlue);

		
		
		JButton editPhrasesButton = new JButton("Edit Phrases");
		phrasesToolbar.add(editPhrasesButton); //Edit phrases is in located within the ToolBar
		editPhrasesButton.addActionListener(new ActionListener() { //Makes "Edit Phrases" open the Edit Phrases Windows
			public void actionPerformed(ActionEvent e) {
				if (jl.getSelectedValue() != null){ //Makes sure something is selected in the word list before it opens
					PhrasesEditor editPhrase = new PhrasesEditor(new Word(jl.getSelectedValue().toString())); //Passes selected word associated with set to be edited
					editPhrase.setVisible(true);
				}
			}
		});
	

		kwic.addPhrases(new File("fortunes.txt")); //Load the file
		Set wordset = kwic.getWords();
		Object[] labels   = wordset.toArray();
		jl.setListData(labels); //Set the words list to the words in the file

		MouseListener mouseListener = new MouseAdapter() {  //When user clicks on values in word list, show the associated phrases in the phrase list
			public void mouseClicked(MouseEvent mouseEvent) {
				Set<Phrase> phraseset = kwic.getPhrases((Word)jl.getSelectedValue());
				Object[] labels = phraseset.toArray();
				phrases.setListData(labels);

			}
		};
		jl.addMouseListener(mouseListener);


		pcs.addPropertyChangeListener("Phrase Added",
				new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// react to the list of words changing
			}
		}
				);

	}

	static void refresh(){ //This method allows other windows to refresh the main window
		Set wordset = MainWindows.instance().kwic.getWords();
		Object[] labels   = wordset.toArray();
		jl.setListData(labels);

	}
}
