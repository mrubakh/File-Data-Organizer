import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.*;



import java.awt.*;
import java.awt.event.*;

public class DataOrganizer extends JPanel implements ActionListener {
  
  
  //Frame Dimensions
  private static final int FRAME_HEIGHT = 750;
  private static final int FRAME_WIDTH = 900;
  private static final int DEFAULT_BOX_WIDTH = 200; //Sets default with for comboboxes that haven't been populated yet
  private static final int BUTTON_WIDTH = 150;
  private static final int BUTTON_HEIGHT = 35;
  private static final int PADDING = 10;
  
  //Number of Filter
  private static final int FILTER_COUNT = 5;
  
  //Action Commands
  private static final String ACTION_BROWSE = "browse";
  private static final String ACTION_ACTIVATE = "activate";  
  private static final String ACTION_FILTER = "filter";
  private static final String ACTION_CLEAR = "clear";
  private static final String ACTION_FILTERTYPE = "filteraction";
  
  //Desired File Extension
  private static final String DESIRED_FILE_EXT = "ssd";
  private static final String SEPARATOR = ";"; //Dillineating character for read-in file
  
  //File Chooser (made a global variable so it will retain last working directory)
  private JFileChooser fc = new JFileChooser();
  
  
  //Panels
  private JPanel controlPanel;  //Contains all controls
  private JPanel filterPanel;  //Filter controls panel
  private JPanel pickerPanel;   //File selector panel
  private JPanel buttonPanel;  //Clear / Filter buttons panel
  
  //File Picker
  private JLabel pickerLabel = new JLabel("Data File: ");
  private JTextField pickerField = new JTextField(FRAME_WIDTH / 50);
  private JButton pickerButton = new JButton("Browse...");
  
  //Filter information
  private JComboBox[] filterType = new JComboBox[FILTER_COUNT]; 
  private JComboBox[] filterAction = new JComboBox[FILTER_COUNT];
  private ArrayList<JComboBox<String>> filterCol = new ArrayList<JComboBox<String>>(); //have to use ArrayList because of Generic here
  private JCheckBox[] filterCheckBox = new JCheckBox[FILTER_COUNT];
  private JTextField filterField[] = new JTextField[FILTER_COUNT];
  private JLabel filterLabel[] = new JLabel[FILTER_COUNT];
  
  private final String SORT_ASC = "sort by (ascend.)";
  private final String SORT_DESC = "sort by (desc.)";
  private final String INCLUDE = "include only if";
  private final String EXCLUDE = "exclude if";
  private final String[] FILTER_ACTIONS = {SORT_ASC, SORT_DESC, INCLUDE, EXCLUDE};
  
  
  private final String STARTS = "starts with";
  private final String ENDS = "ends with";
  private final String CONTAINS = "contains";
  private final String EQUALS = "equals";  
  private final String[] FILTER_TYPES = {STARTS, ENDS, CONTAINS, EQUALS};
  
  //Counter for "number of recoreds found"
  private JLabel resultsCount;
  
  //Filter/Clear Buttons
  private JButton filterButton = new JButton("Filter");
  private JButton clearButton = new JButton("Clear");
  
  // Text area to display result string
  private JTable results = new JTable();
  
  //Dataset
  DataSet set;
  
  
  
  
  // Constructor;  creates window elements
  public DataOrganizer() {
    setLayout(new BorderLayout());
    
    // Control panel, which includes the fields for the search and the search button itself
    controlPanel = new JPanel();
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));   // top-down layout
    controlPanel.setBorder(new EmptyBorder(PADDING, PADDING, 0, PADDING));
    
    
    initPickerPanel();
    initFilterPanel();                
    initButtonPanel();
    initResultsCountPanel();
    
    add(controlPanel, BorderLayout.NORTH);
    add(new JScrollPane(results, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) );
    results.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    
    results.setModel(new DefaultTableModel() {
      
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    });
    setPanel(filterPanel, false);    
    setPanel(buttonPanel, false);
    
    
    
    // Turn on automatically adding gaps between components
    //layout.setAutoCreateGaps(true);
    
    // Turn on automatically creating gaps between components that touch
    // the edge of the container and the container.
    //layout.setAutoCreateContainerGaps(true);
    
    
  }
  
  private void initResultsCountPanel(){
    
    JPanel resultsCountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    resultsCount = new JLabel();
    resultsCount.setVisible(false);
    resultsCountPanel.add(resultsCount);
    controlPanel.add(resultsCountPanel);    
    
    
    
  }
  
  
  private void initPickerPanel(){
    
    // Search fields -- nice layout where labels and data fields are aligned vertically & horizontally
    pickerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pickerPanel.add(pickerLabel);
    pickerField.setEnabled(false);
    pickerPanel.add(pickerField);
    pickerPanel.add(pickerButton);
    
    pickerButton.addActionListener(this);
    pickerButton.setActionCommand(ACTION_BROWSE);
    
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Semicolon Separated Data (." + DESIRED_FILE_EXT + ")", DESIRED_FILE_EXT);
    fc.setDialogTitle("Select your " + DESIRED_FILE_EXT + " file!");
    
    fc.setFileFilter(filter);
    
    controlPanel.add(pickerPanel);
    
  }
  
  
  //Initializes the filter controls
  private void initFilterPanel(){
    
    filterPanel = new JPanel();
    filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.PAGE_AXIS));
    
    JPanel filterRow[] = new JPanel[FILTER_COUNT];  //individual row of filter controls
    for (int i = 0; i < filterRow.length; i++){
      filterRow[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
      
      filterCheckBox[i] = new JCheckBox();
      filterRow[i].add(filterCheckBox[i]);
      filterCheckBox[i].addActionListener(this);
      filterCheckBox[i].setActionCommand(ACTION_ACTIVATE + i);
      
      filterLabel[i] = new JLabel("Filter " + (i+1) + ":");
      filterRow[i].add(filterLabel[i]);
      
      
      filterAction[i] = new JComboBox<String>(FILTER_ACTIONS);
      filterAction[i].addActionListener(this);
      filterAction[i].setActionCommand(ACTION_FILTERTYPE + i);
      filterRow[i].add(filterAction[i]);
      
      JComboBox<String> filterTemp = new JComboBox<String>();
      Dimension boxSize = filterTemp.getPreferredSize();
      boxSize.width = DEFAULT_BOX_WIDTH;
      filterTemp.setPreferredSize(boxSize);      
      filterRow[i].add(filterTemp);
      filterCol.add(filterTemp);
      
      filterType[i] = new JComboBox<String>(FILTER_TYPES);
      filterType[i].setVisible(false);
      filterRow[i].add(filterType[i]);
      
      filterField[i] = new JTextField(FRAME_WIDTH / 65);
      filterField[i].setVisible(false);
      filterRow[i].add(filterField[i]);
      
      filterPanel.add(filterRow[i]);
      
    }
    controlPanel.add(filterPanel);
  }
  
  
  private void initButtonPanel(){
    
    buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.setBorder(new EmptyBorder(0, PADDING, PADDING, PADDING));
    
    Dimension buttonSize = clearButton.getPreferredSize();
    buttonSize.width = BUTTON_WIDTH;
    buttonSize.height = BUTTON_HEIGHT;
    clearButton.setPreferredSize(buttonSize);
    filterButton.setPreferredSize(buttonSize);
    
    buttonPanel.add(clearButton);
    buttonPanel.add(filterButton);
    
    clearButton.addActionListener(this);
    clearButton.setActionCommand(ACTION_CLEAR);
    
    
    filterButton.addActionListener(this);
    filterButton.setActionCommand(ACTION_FILTER);
    
    controlPanel.add(buttonPanel);    
    
    
  }
  
  
  //Disables all components in a Panel
  private static void setPanel(JPanel p, boolean state){
    for (Component cp : p.getComponents() ){
      if (cp instanceof JPanel)
        setPanel((JPanel)cp, state);
      else
        cp.setEnabled(state);
    }    
  }
  
  
  //enables or disables a specific row of filter controls
  private void toggleFilterRow(int row, boolean toggle){
    filterType[row].setEnabled(toggle);
    filterCol.get(row).setEnabled(toggle);
    filterField[row].setEnabled(toggle);
    filterAction[row].setEnabled(toggle);
    filterLabel[row].setEnabled(toggle);
    
  }
  
  // event handler
  public void actionPerformed(ActionEvent e) {
    String action = e.getActionCommand();
    if (action.equals(ACTION_BROWSE)) {      
      int returnVal = fc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File f = fc.getSelectedFile();
        ArrayList<String[]> data = loadData(f);
        if (data != null){
          pickerField.setText(f.getName());          
          String[] colNames = data.remove(0);
          initTable(colNames);
          setTable(data);
          initFilters(colNames);
          setPanel(buttonPanel, true);
          set = new DataSet(colNames, data);
        }
      }
    }
    else if (action.startsWith(ACTION_ACTIVATE)){
      int filterNum = Integer.parseInt(action.substring(action.length()-1));
      toggleFilterRow(filterNum, filterCheckBox[filterNum].isSelected());
    }
    else if (action.equals(ACTION_CLEAR)){
      resetFilters();
      setTable(set.applyFilters(new ArrayList<Filter>())); 
    }
    else if (action.startsWith(ACTION_FILTERTYPE)){
      int filterNum = Integer.parseInt(action.substring(action.length()-1));
      if (filterAction[filterNum].getSelectedItem().toString().startsWith("sort")){
        filterType[filterNum].setVisible(false);
        filterField[filterNum].setVisible(false);     
      }
      else{
        filterType[filterNum].setVisible(true);
        filterField[filterNum].setVisible(true); 
        
      }
      
    }
    else if(action.equals(ACTION_FILTER)){
      ArrayList<Filter> filters = new ArrayList<Filter>();
      for (int i = 0; i < FILTER_COUNT; i++)
      {
        if (filterCheckBox[i].isSelected())
          filters.add(buildFilter(i));
      }
      ArrayList<String[]> filteredSet = set.applyFilters(filters);         
      setTable(filteredSet);      
    }    
  }
  
  
  private Filter buildFilter(int filterNum){
     Filter.FilterAction a;
     String selectedAction = ((String)filterAction[filterNum].getSelectedItem());
     switch(selectedAction){
       case SORT_ASC:
         a = Filter.FilterAction.SORT_BY_ASCENDING;
         break;
       case SORT_DESC:
         a = Filter.FilterAction.SORT_BY_DESCENDING;
         break;
       case INCLUDE:
         a = Filter.FilterAction.INCLUDE_IF;
         break;
       default:
         a = Filter.FilterAction.EXCLUDE_IF;             
     }
     if (a == Filter.FilterAction.SORT_BY_ASCENDING || a == Filter.FilterAction.SORT_BY_DESCENDING)
       return new Filter(a, ((String)filterCol.get(filterNum).getSelectedItem()));
     
     Filter.FilterType ft;
     String selectedFilterType = ((String)filterType[filterNum].getSelectedItem());
     switch(selectedFilterType){
       case STARTS:
         ft = Filter.FilterType.STARTS_WITH;
         break;
       case ENDS:
         ft = Filter.FilterType.ENDS_WITH;
         break;
       case CONTAINS:
         ft = Filter.FilterType.CONTAINS;
         break;
       default:
         ft = Filter.FilterType.EQUALS;  
         break;
     }
    return new Filter(a, ((String)filterCol.get(filterNum).getSelectedItem()), ft, filterField[filterNum].getText().toUpperCase());
  }
  
  
  
  private void initTable(String[] colNames){
    DefaultTableModel dtm = (DefaultTableModel)results.getModel();
    dtm.setRowCount(0);
    dtm.setColumnCount(0);
    for (String s : colNames)
      dtm.addColumn(s);
    
    
    for (int i = 0; i < colNames.length; i++)
      results.getColumnModel().getColumn(i).setPreferredWidth((FRAME_WIDTH - PADDING) / colNames.length); 
    results.getTableHeader().setReorderingAllowed(false);
  }
  
  private void updateResultsCount(int count){
   resultsCount.setText(count + " Records Found");
   resultsCount.setVisible(true);    
  }
  
  
  private void setTable(ArrayList<String[]> rowData){
    DefaultTableModel dtm = (DefaultTableModel)results.getModel();
    dtm.setRowCount(0);    
    if (rowData != null){
    for (String[] row : rowData)
      dtm.addRow(row);
    }
    updateResultsCount(rowData.size());
    dtm.fireTableDataChanged();
  }
  
  private void initFilters(String[] colNames){
    for (int i = 0; i < filterField.length; i++)
    {
      filterField[i].setText("");
      JComboBox<String> tempBox = filterCol.get(i);
      tempBox.removeAllItems();
      for (String s: colNames)
        tempBox.addItem(s);
      toggleFilterRow(i, false);
      filterAction[i].setSelectedIndex(0);
      filterType[i].setSelectedIndex(0);
      filterCheckBox[i].setEnabled(true);
      filterCheckBox[i].setSelected(false);
      filterType[i].setVisible(false);
      filterField[i].setVisible(false);      
    }
  }
  
  
  private void resetFilters(){
    
    for (int i = 0; i < filterField.length; i++)
    {
      filterField[i].setText("");
      filterCol.get(i).setSelectedIndex(0);
      filterType[i].setSelectedIndex(0);
      filterAction[i].setSelectedIndex(0);
      filterCheckBox[i].setSelected(false);
      filterType[i].setVisible(false);
      filterField[i].setVisible(false);
      toggleFilterRow(i,false);
    }   
    repaint();
  }
  
  private ArrayList<String[]> loadData(File f){
    
    try{
      Scanner sc = new Scanner(f);
      ArrayList<String[]> data;
      int filterLen;
      
      if (sc.hasNextLine()){
        String line[] = sc.nextLine().split(SEPARATOR);
        filterLen = line.length;
        data = new ArrayList<String[]>();
        data.add(line);  
      }
      else{
        JOptionPane.showMessageDialog(this, "Error - Selected file is empty!", "File Read Error", JOptionPane.ERROR_MESSAGE);
        return null;
      }
      int lineNum = 1;
      while (sc.hasNextLine()){
        lineNum++;
        String[] line = sc.nextLine().split(SEPARATOR);
        if (line.length == filterLen)
          data.add(line);
        else if (line.length < filterLen){
          JOptionPane.showMessageDialog(this, "Error - Line #" + lineNum + " has insufficient columns!  (Expected: " + filterLen + ", found: " + line.length + ")", "File Read Error", JOptionPane.ERROR_MESSAGE);
          return null;
        }
        else{
          String[] temp = new String[filterLen];
          for (int i = 0; i < filterLen; i++)
            temp[i] = line[i];
          data.add(temp);
        }
      }
      return data;
      
    }
    catch(FileNotFoundException fnfe){
      JOptionPane.showMessageDialog(this, "Error - Could not open " + f.getName() + "!", "File Read Error", JOptionPane.ERROR_MESSAGE);
      return null;
    }
    
  }
  
  
  
  
  /* main()
   * 
   * Starts the program.
   */
  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable() {
      
      public void run() {
        JFrame frame = new JFrame("Data Organizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.add(new DataOrganizer());
        
        // frame.pack();
        frame.setVisible(true);
        frame.toFront();
      }
    });
  }
  
  
  private static void printArr(ArrayList<String[]> arr){
    for (int i = 0; i < arr.size(); i++)
      System.out.println(Arrays.toString(arr.get(i)));
    
  }
  
  
}