import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NumberPlateGUI extends JFrame {

    private static final int TABLE_SIZE = 100;
    private final String[] plates = new String[TABLE_SIZE];
    private final String[] owners = new String[TABLE_SIZE];

    private JTextField plateField;
    private JTextField ownerField;
    private JTextArea outputArea;
    private DefaultTableModel tableModel;

    public NumberPlateGUI() {
        setTitle("Vehicle Number Plate Indexer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Number Plate:"), gbc);
        gbc.gridx = 1;
        plateField = new JTextField(15);
        inputPanel.add(plateField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Owner Name:"), gbc);
        gbc.gridx = 1;
        ownerField = new JTextField(15);
        inputPanel.add(ownerField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        JButton addBtn = new JButton("Add Vehicle");
        JButton searchBtn = new JButton("Search");
        JButton displayBtn = new JButton("Display Table");
        JButton clearBtn = new JButton("Clear");
        buttonPanel.add(addBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(displayBtn);
        buttonPanel.add(clearBtn);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Output"));

        String[] columns = {"Index", "Number Plate", "Owner Name", "Hash Index"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable hashTable = new JTable(tableModel);
        hashTable.setRowHeight(22);
        JScrollPane tableScroll = new JScrollPane(hashTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Hash Table"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, outputScroll, tableScroll);
        splitPane.setDividerLocation(380);

        add(inputPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        addBtn.addActionListener(e -> addVehicle());
        searchBtn.addActionListener(e -> searchVehicle());
        displayBtn.addActionListener(e -> displayTable());
        clearBtn.addActionListener(e -> {
            plateField.setText("");
            ownerField.setText("");
            outputArea.setText("");
        });
    }

    private String normalizePlate(String input) {
        return input.trim().replaceAll("[\\s-]", "").toUpperCase();
    }

    private boolean isValidPlate(String plate) {
        return plate.matches("[A-Z]{2}[0-9]{1,2}[A-Z]{1,3}[0-9]{1,4}");
    }

    private int hashFunction(String plate) {
        String lastTwo = plate.substring(plate.length() - 2);
        return Integer.parseInt(lastTwo);
    }

    private void addVehicle() {
        String plate = normalizePlate(plateField.getText());
        if (!isValidPlate(plate)) {
            output("Invalid plate format. Example: MH02AB1234");
            return;
        }
        String owner = ownerField.getText().trim();
        if (owner.isEmpty()) owner = "Not provided";

        int startIndex = hashFunction(plate);
        int index = startIndex;

        for (int probes = 0; probes < TABLE_SIZE; probes++) {
            if (plates[index] == null) {
                plates[index] = plate;
                owners[index] = owner;
                output("Vehicle added.");
                output("Hash Index   : " + startIndex);
                output("Stored Index : " + index);
                if (index != startIndex) {
                    output("Collision occurred. Linear probing used index " + index + ".");
                }
                plateField.setText("");
                ownerField.setText("");
                return;
            }
            if (plates[index].equals(plate)) {
                output("Plate already registered at index " + index + ".");
                return;
            }
            index = (index + 1) % TABLE_SIZE;
        }
        output("Hash table is full.");
    }

    private void searchVehicle() {
        String plate = normalizePlate(plateField.getText());
        if (!isValidPlate(plate)) {
            output("Invalid plate format. Example: MH02AB1234");
            return;
        }
        int startIndex = hashFunction(plate);
        int index = startIndex;

        for (int probes = 0; probes < TABLE_SIZE; probes++) {
            if (plates[index] == null) {
                output("Vehicle not found.");
                return;
            }
            if (plates[index].equals(plate)) {
                output("Number Plate : " + plates[index]);
                output("Owner Name   : " + owners[index]);
                output("Hash Index   : " + startIndex);
                output("Stored Index : " + index);
                return;
            }
            index = (index + 1) % TABLE_SIZE;
        }
        output("Vehicle not found.");
    }

    private void displayTable() {
        tableModel.setRowCount(0);
        boolean any = false;
        for (int i = 0; i < TABLE_SIZE; i++) {
            if (plates[i] != null) {
                any = true;
                int hashIdx = hashFunction(plates[i]);
                tableModel.addRow(new Object[]{i, plates[i], owners[i], hashIdx});
            }
        }
        if (!any) {
            output("Hash table is empty.");
        } else {
            output("Table refreshed.");
        }
    }

    private void output(String msg) {
        outputArea.append(msg + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NumberPlateGUI().setVisible(true);
        });
    }
}
