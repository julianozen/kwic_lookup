package kwic;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.Set;
import javax.swing.JList;
import javax.swing.JLabel;

import sun.applet.Main;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import java.awt.SystemColor;
import java.awt.Color;

public class EditWords extends JFrame {

	public MainWindows main;
	public KWIC kwic;
	protected PropertyChangeSupport pcs;
	private JPanel contentPane;
	private JTextField addWordField;
	private JList list;
	private JButton deleteButton;
	private JButton doneButton;
	private JToolBar toolBar;
	private JTextField searchField;
	private JButton btnFind;
	private JLabel lblEdit;

	/**
	 * Launch the application.
	 */


	/**
	 * Create the frame.
	 */
	public EditWords() {

		this.kwic = MainWindows.instance().kwic; //Use MainWindow's kiwc
		this.pcs = kwic.getPCS();

		setResizable(false); //keeps the edit window size static
		setAlwaysOnTop(true); //keeps edit window above main to help maintain user focus
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 438, 286);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{309, 117, 0};
		gbl_contentPane.rowHeights = new int[]{0, 30, 155, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);

		lblEdit = new JLabel("Edit Words"); //Nice Title at the top
		GridBagConstraints gbc_lblEdit = new GridBagConstraints();
		gbc_lblEdit.insets = new Insets(0, 0, 5, 5);
		gbc_lblEdit.gridx = 0;
		gbc_lblEdit.gridy = 0;
		contentPane.add(lblEdit, gbc_lblEdit);

		addWordField = new JTextField(); //Creates Field to add new words.
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		contentPane.add(addWordField, gbc_textField);
		addWordField.setColumns(10);

		JButton Add = new JButton("Add"); //Creates a button that will be used to add words to the field
		GridBagConstraints gbc_Add = new GridBagConstraints();
		gbc_Add.fill = GridBagConstraints.HORIZONTAL;
		gbc_Add.anchor = GridBagConstraints.SOUTH;
		gbc_Add.insets = new Insets(0, 0, 5, 0);
		gbc_Add.gridx = 1;
		gbc_Add.gridy = 1;
		contentPane.add(Add, gbc_Add);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		contentPane.add(scrollPane, gbc_scrollPane);

		list = new JList(); //List of words currently in kwic
		scrollPane.setViewportView(list);


		deleteButton = new JButton("Delete"); //this button  will be used to delete words from the field

		GridBagConstraints gbc_Delete = new GridBagConstraints();
		gbc_Delete.insets = new Insets(0, 0, 5, 0);
		gbc_Delete.fill = GridBagConstraints.HORIZONTAL;
		gbc_Delete.anchor = GridBagConstraints.SOUTH;
		gbc_Delete.gridx = 1;
		gbc_Delete.gridy = 2;
		contentPane.add(deleteButton, gbc_Delete);


		Set wordSet = kwic.getWords(); 
		Object[] labels   = wordSet.toArray();
		list.setListData(labels); //fills word list

		toolBar = new JToolBar(); //toolbar for the search bar
		toolBar.setBackground(Color.GRAY);
		toolBar.setRollover(true);
		toolBar.setFloatable(false);
		scrollPane.setColumnHeaderView(toolBar);

		searchField = new JTextField(); //searchbar
		toolBar.add(searchField);
		searchField.setColumns(10);
		searchField.putClientProperty( "JTextField.variant", "search");


		searchField.addActionListener(new ActionListener(){ //makes searchbar react to user click return
			public void actionPerformed(ActionEvent e){
				Set<Phrase> phraseset =kwic.getPhrases(new Word(searchField.getText()));
				Object[] labels = phraseset.toArray();
				list.setSelectedValue(new Word(searchField.getText()), true);
			}});

		doneButton = new JButton("Done"); //This button will be used to close the edit window. 
		GridBagConstraints gbc_doneButton = new GridBagConstraints();
		gbc_doneButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_doneButton.gridx = 1;
		gbc_doneButton.gridy = 3;
		contentPane.add(doneButton, gbc_doneButton);

		doneButton.addActionListener(new ActionListener() {//When button is clicks, close the edit window. Note: the regular close button quits the whole application.
			public void actionPerformed(ActionEvent e) {
				dispose(); 
			}
		});
		final MainWindows newMain = main;
		final KWIC finalKWIC = kwic;
		Add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //Add words from the addField when the user clicks "Add"
				if (addWordField.getText() != ""){
					finalKWIC.forceAssoc(new Word(addWordField.getText()), null);
					Set wordSet = finalKWIC.getWords();
					Object[] labels   = wordSet.toArray();
					list.setListData(labels);
					MainWindows.instance().refresh(); //refreshes the main window.
				}
			}
		});



		deleteButton.addActionListener(new ActionListener() { //deletes words from the list when uses clicks "Delete"
			public void actionPerformed(ActionEvent e) {
				finalKWIC.deleteWord((Word) list.getSelectedValue());
				Set wordSet = finalKWIC.getWords();
				Object[] labels   = wordSet.toArray();
				list.setListData(labels);
				MainWindows.instance().refresh(); //refreshes the main window
			}
		});
	}
}
