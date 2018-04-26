package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Graphical interface class for Metropolis
 * It works with MetropolisDB class to pass
 * and search by certain data criteria
 */

public class MetropolisFrame extends JFrame {

    /*
        MetropolisDB Object which communicates
        with database and updates MTable object
        MTable object is needed here just for
        adding it as TableModel in JTable
     */
    private MetropolisDB MetropolisDB;
    MTable tableModel;
    JTable table;

    private final int DEFAULT_WIDTH = 640;
    private final int DEFAULT_HEIGHT = 480;


    //North Components
    private JLabel MetropolisLabel;
    private JLabel ContinentLabel;
    private JLabel PopulationLabel;
    private JTextField MetropolisField;
    private JTextField ContinentField;
    private JTextField PopulationField;

    //East Components
    private JButton AddButton;
    private JButton SearchButton;
    private TitledBorder SearchOptions;
    private JComboBox <String> PopulationCBox;
    private JComboBox <String> MatchCBox;



    MetropolisFrame () {
        //Initialise whole graphic
        super("Metropolis");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setVisible(true);
        setupNorth();
        setupEast();
        setupCenter();

        //Initialise Database, pass tableModel to it
        MetropolisDB = new MetropolisDB(tableModel);
        setupListeners();
    }

    /**
     * Setup listeners for Add and search buttons
     */

    private void setupListeners () {

        AddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Extract text from text fields
                String MetropolisVal = MetropolisField.getText();
                String ContinentVal = ContinentField.getText();
                String PopulationVal = PopulationField.getText();

                //If any text fields are empty I won't do anything
                if (MetropolisVal.length()>0 && ContinentVal.length()>0 && PopulationVal.length()>0) {
                    MetropolisDB.addNewEntry (MetropolisVal, ContinentVal, PopulationVal);
                }
            }
        });

        SearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Extract text from text fields
                String MetropolisVal = MetropolisField.getText();
                String ContinentVal = ContinentField.getText();
                String PopulationVal = PopulationField.getText();


                //Extract data from boxes
                String PBoxVal = (String)PopulationCBox.getSelectedItem();
                String MBoxVal = (String)MatchCBox.getSelectedItem();

                //Build query for this data
                String selectQuery = MetropolisDB.selectBuilder(MetropolisVal, ContinentVal, PopulationVal, PBoxVal, MBoxVal);
                //Execute built query
                MetropolisDB.executeQuery (selectQuery);
            }
        });
    }

    /**
     * Setup center region
     */

    private void setupCenter () {
        String[] columnNames = {"Metropolis", "Continent", "Population"};
        //Initialise new MTable with appropriate column names
        tableModel = new MTable(columnNames);
        table = new JTable(tableModel);
        add(new JScrollPane(table) , BorderLayout.CENTER);

    }

    /**
     * Setups north side
     */

    private void setupNorth () {
        Box north = new Box(BoxLayout.X_AXIS);


        MetropolisLabel = new JLabel("Metropolis");
        MetropolisField = new JTextField();
        ContinentLabel = new JLabel(" Continent");
        ContinentField = new JTextField();
        PopulationLabel = new JLabel(" Population");
        PopulationField = new JTextField();

        north.add(MetropolisLabel);
        north.add(MetropolisField);
        north.add(ContinentLabel);
        north.add(ContinentField);
        north.add(PopulationLabel);
        north.add(PopulationField);

        add (north, BorderLayout.NORTH);
    }

    /**
     * Setups east side
     */

    private void setupEast () {
        Box east = new Box (BoxLayout.Y_AXIS);
        AddButton = new JButton("Add");
        SearchButton = new JButton("Search");
        SearchOptions = new TitledBorder("Search Options");
        String[] PopulationCBoxVal = {"Population Larger Than", "Population Smaller Than or Equal"};
        String[] MatchCBoxVal = {"Exact Match", "Partial Match"};


        Dimension CBoxDimension = new Dimension(200, 30);
        Box SearchOptionsBox = new Box(BoxLayout.PAGE_AXIS);
        PopulationCBox = new JComboBox<>(PopulationCBoxVal);
        MatchCBox = new JComboBox<>(MatchCBoxVal);
        PopulationCBox.setMaximumSize(CBoxDimension);
        MatchCBox.setMaximumSize(CBoxDimension);

        SearchOptionsBox.add(PopulationCBox);
        SearchOptionsBox.add(MatchCBox);
        SearchOptionsBox.setBorder(SearchOptions);


        //Some basic alignments for simple cosmetics
        east.add(AddButton);
        AddButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        AddButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        east.add(SearchButton);
        SearchButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        SearchButton.setAlignmentY(Component.CENTER_ALIGNMENT);
        east.add(SearchOptionsBox);
        SearchOptionsBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
        SearchOptionsBox.setAlignmentY(Component.CENTER_ALIGNMENT);


        add(east, BorderLayout.EAST);

    }

}
