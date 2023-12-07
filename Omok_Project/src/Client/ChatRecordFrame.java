package Client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ChatRecordFrame extends JFrame {

    private JPanel contentPane;
    private JTable table;

    public ChatRecordFrame(String userchatrecord) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 844, 559);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 20, 806, 460);
        contentPane.add(scrollPane);

        // Define column names
        String[] columnNames = {"Nickname", "Chat Content", "Chat Time"};

        // Create a DefaultTableModel with no data
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Set all cells to be non-editable
            }
        };

        table = new JTable(model);
        scrollPane.setViewportView(table);

        // Parse and add data to the table
        parseAndAddDataToTable(userchatrecord);
    }

    private void parseAndAddDataToTable(String userchatrecord) {
        // Split the userchatrecord by "&&" to get individual chat records
        String[] chatRecords = userchatrecord.split("&&");

        for (String record : chatRecords) {
            // Split each record by "$$" to get nickname, chat content, and chat time
            String[] recordData = record.split("\\$\\$");

            // Add a new row to the table with the parsed data
            ((DefaultTableModel) table.getModel()).addRow(recordData);
        }
    }
}