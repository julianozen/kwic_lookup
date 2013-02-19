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
import java.awt.Color;
import javax.swing.AbstractListModel;

public class PhrasesEditor extends JFrame {

	public MainWindows main;
	public Word title; //Word passed in associated with set of phrases being edited
	public KWIC kwic;
	protected PropertyChangeSupport pcs;
	private JList list;
	private JPanel contentPane;
	private JTextField addPhraseField;
	private JButton Delete;
	private JButton doneButton;
	private JButton addButton;
	private JLabel lblEditPhrasesIn;

	/**
	 * Create the frame.
	 */
	public PhrasesEditor(Word w) {
		this.title=w;
		this.kwic = MainWindows.instance().kwic; //Use MainWindow's kiwc
		this.pcs = kwic.getPCS();

		setResizable(false); //keeps the edit window size static
		setAlwaysOnTop(true); //keeps edit window above main to help maintain user focus
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 434, 270);
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

		lblEditPhrasesIn = new JLabel(title.getMatchWord()); //Sets Title Equal to Word value being changed
		GridBagConstraints gbc_lblEditPhrasesIn = new GridBagConstraints();
		gbc_lblEditPhrasesIn.insets = new Insets(0, 0, 5, 5);
		gbc_lblEditPhrasesIn.gridx = 0;
		gbc_lblEditPhrasesIn.gridy = 0;
		contentPane.add(lblEditPhrasesIn, gbc_lblEditPhrasesIn);

		addPhraseField = new JTextField(); //Creates Field to add new phrases.
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		contentPane.add(addPhraseField, gbc_textField);
		addPhraseField.setColumns(10);

		JButton addButton = new JButton("Add");//Creates a button that will be used to add phrases to the field
		GridBagConstraints gbc_Add = new GridBagConstraints();
		gbc_Add.fill = GridBagConstraints.HORIZONTAL;
		gbc_Add.anchor = GridBagConstraints.SOUTH;
		gbc_Add.insets = new Insets(0, 0, 5, 0);
		gbc_Add.gridx = 1;
		gbc_Add.gridy = 1;
		contentPane.add(addButton, gbc_Add);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		contentPane.add(scrollPane, gbc_scrollPane);

		list = new JList();//List of phrases currently in kwic associated with passed in word
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		scrollPane.setViewportView(list);


		Delete = new JButton("Delete");

		GridBagConstraints gbc_Delete = new GridBagConstraints();
		gbc_Delete.insets = new Insets(0, 0, 5, 0);
		gbc_Delete.fill = GridBagConstraints.HORIZONTAL;
		gbc_Delete.anchor = GridBagConstraints.SOUTH;
		gbc_Delete.gridx = 1;
		gbc_Delete.gridy = 2;
		contentPane.add(Delete, gbc_Delete);


		Set wordSet = kwic.getPhrases(title);
		Object[] labels   = wordSet.toArray();
		list.setListData(labels); //fills phrases list

		doneButton = new JButton("Done"); //This button will be used to close the edit window. 
		GridBagConstraints gbc_doneButton = new GridBagConstraints();
		gbc_doneButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_doneButton.gridx = 1;
		gbc_doneButton.gridy = 3;
		contentPane.add(doneButton, gbc_doneButton);

		doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); 
				
			}
		});
		final MainWindows newMain = main;
		final KWIC finalKWIC = kwic;
		addButton.addActionListener(new ActionListener() { //Add phrases from the addPhraseField when the user clicks "Add"
			public void actionPerformed(ActionEvent e) {
				if (addPhraseField.getText() != ""){
					finalKWIC.forceAssoc(title, new Phrase(addPhraseField.getText())); //If the word oject has been deleted in the background, it readds the word to kwic
					Set wordSet = finalKWIC.getPhrases(title);
					Object[] labels = wordSet.toArray();
					list.setListData(labels);
					MainWindows.instance().refresh();//refreshes the main window
				}
			}
		});


		Delete.addActionListener(new ActionListener() {//deletes phrase from the list when uses clicks "Delete"
			public void actionPerformed(ActionEvent e) {		
				finalKWIC.dropAssoc(title, (Phrase) list.getSelectedValue());
				Set wordSet = finalKWIC.getPhrases(title);
				Object[] labels   = wordSet.toArray();
				list.setListData(labels);
				MainWindows.instance().refresh();//refreshes the main window
			}
		});
	}
}
